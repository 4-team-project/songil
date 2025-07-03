<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/resources/css/pages/seller/createFunding_insertDetail.css">
    
<!-- 날짜 및 이미지 업로드 영역 -->
<input type="hidden" id="fundingId" value="${sessionScope.fundingDTO.fundingId}" />

<!-- 날짜 입력 -->
<div class="fundingDate">
  <div class="menu-label">펀딩 시작일과 종료일을 입력해 주세요.</div>
  <div id="dateArea">
    <span style="font-size: 20px;">시작일</span>
    <input type="date" id="startDate" required />
    <span style="font-size: 20px;">종료일</span>
    <input type="date" id="endDate" required />
    <button type="button" class="btn-check" onclick="submitDate()">확인</button>
  </div>
  <div id="dateInfo" style="margin-top: 10px; font-size: 20px; color: #ff9670; font-weight: bold;"></div>
</div>

<!-- 이미지 업로드 -->
<div class="menuPicture">
  <div class="menu-label">펀딩 사진을 선택해 주세요.</div>
  <p><strong>최대 2개까지</strong> 사진을 등록해 주세요!</p>
  <div class="content-input" id="image-preview-container">
    <div class="menu-img-upload-wrapper">
      <label for="images" class="menu-img-btn">사진 추가하기</label>
      <input type="file" id="images" accept="image/*" multiple onchange="handleFiles(this.files)" />
      <button type="button" id="btnDefaultPhoto">메뉴 사진과 동일</button>
    </div>
    <div id="hiddenImageInputs" style="display: none;"></div>
    <div id="previewContainer" class="preview-list"></div>
    <div id="file-count-text" class="file-count-text" style="margin-top: 8px; color: #888; font-size: 15px;">선택한 사진</div>
  </div>
</div>

<!-- 다음 단계 -->
<div class="btn-container">
  <c:set var="type" value="${sessionScope.fundingType}" />
  <button class="btn" type="button" onclick="location.href='${pageContext.request.contextPath}/seller/fundings/create-step2?type=${type}'">이전</button>
  <button class="btn" type="button" onclick="submitAndMove()">다음</button>
</div>

<!-- 모달 -->
<div id="resultModal">
  <p id="modalMsg"></p>
  <button id="closeModalBtn">확인</button>
</div>

<!-- 스크립트 -->
<script>
let isDateConfirmed = false;

function submitDate() {
  const start = document.getElementById("startDate").value;
  const end = document.getElementById("endDate").value;
  if (!start || !end) return showModalMessage("시작일과 종료일을 모두 입력해 주세요.");

  const startObj = new Date(start);
  const endObj = new Date(end);
  if (endObj < startObj) return showModalMessage("종료일은 시작일 이후여야 합니다.");

  document.getElementById("dateInfo").innerText = `${startObj.getFullYear()}년 ${startObj.getMonth()+1}월 ${startObj.getDate()}일 0시 ~ ${endObj.getFullYear()}년 ${endObj.getMonth()+1}월 ${endObj.getDate()}일 23시 59분까지 펀딩이 진행됩니다.`;
  isDateConfirmed = true;
  showModalMessage("날짜가 확인되었습니다!");
}

function showModalMessage(msg) {
  $("#modalMsg").text(msg);
  $("#resultModal").fadeIn();
}

$("#closeModalBtn").click(() => $("#resultModal").fadeOut());

function submitAndMove() {
  if (!isDateConfirmed) return showModalMessage("날짜를 확인해 주세요.");

  const imageUrls = Array.from(document.querySelectorAll("#hiddenImageInputs input")).map(i => i.value);
  if (imageUrls.length === 0) return showModalMessage("이미지를 1장 이상 선택해 주세요.");

  const payload = {
    fundingId: document.getElementById("fundingId").value,
    startDate: document.getElementById("startDate").value,
    endDate: document.getElementById("endDate").value,
    images: imageUrls
  };

  fetch(`${cpath}/seller/fundings/create-step4`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  }).then(res => {
    if (!res.ok) throw new Error("전송 실패");
    return res.text();
  }).then(() => {
    location.href = `${cpath}/seller/fundings/create-step4`;
  }).catch(err => {
    alert("전송 실패: " + err.message);
  });
}

function handleFiles(fileList) {
  const preview = document.getElementById("previewContainer");
  const hiddenInputs = document.getElementById("hiddenImageInputs");
  const fileCountText = document.getElementById("file-count-text");

  const files = Array.from(fileList);
  const max = 2 - hiddenInputs.children.length;
  if (files.length > max) return alert("최대 2개의 이미지만 선택 가능합니다.");

  files.forEach(file => {
    const formData = new FormData();
    formData.append("file", file);

    fetch(`${cpath}/seller/fundings/uploadImage`, {
      method: "POST",
      body: formData
    })
    .then(res => res.text())
    .then(url => {
      const wrapper = document.createElement("div");
      wrapper.className = "preview-item";
      const img = document.createElement("img");
      img.src = url;
      const delBtn = document.createElement("div");
      delBtn.className = "delete-btn";
      delBtn.innerText = "x";
      delBtn.onclick = () => {
        wrapper.remove();
        hiddenInputs.querySelector(`input[value='${url}']`)?.remove();
        fileCountText.textContent = `선택한 사진 ${hiddenInputs.children.length} / 2`;
      };
      wrapper.appendChild(img);
      wrapper.appendChild(delBtn);
      preview.appendChild(wrapper);

      const hiddenInput = document.createElement("input");
      hiddenInput.type = "hidden";
      hiddenInput.value = url;
      hiddenInputs.appendChild(hiddenInput);
      fileCountText.textContent = `선택한 사진 ${hiddenInputs.children.length} / 2`;
    })
    .catch(err => alert("이미지 업로드 실패: " + err.message));
  });

  document.getElementById("images").value = "";
}

btnDefaultPhoto.addEventListener("click", () => {
  const fundingId = document.getElementById("fundingId").value;
  $.post("${pageContext.request.contextPath}/seller/fundings/loadDefaultImages", { fundingId }, (response) => {
    const preview = document.getElementById("previewContainer");
    const hiddenInputs = document.getElementById("hiddenImageInputs");
    preview.innerHTML = "";
    hiddenInputs.innerHTML = "";
    if (!response || response.length === 0) return alert("기본 이미지 없음");

    response.forEach(url => {
      const wrapper = document.createElement("div");
      wrapper.className = "preview-item";
      const img = document.createElement("img");
      img.src = url;
      const btn = document.createElement("button");
      btn.innerText = "취소하기";
      btn.className = "btn-cancel";
      btn.onclick = () => {
        wrapper.remove();
        hiddenInputs.querySelector(`input[value='${url}']`)?.remove();
      };
      wrapper.appendChild(img);
      wrapper.appendChild(btn);
      preview.appendChild(wrapper);

      const input = document.createElement("input");
      input.type = "hidden";
      input.value = url;
      hiddenInputs.appendChild(input);
    });
  });
});
</script>