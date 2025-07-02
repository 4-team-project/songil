<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/pages/seller/createFunding_insertDetail.css">
<form
	action="${pageContext.request.contextPath}/seller/fundings/create-step4"
	method="post" onsubmit="return checkConfirmed();">

	<div class="fundingDate">
		<div class="menu-label">펀딩 시작일과 종료일을 입력해 주세요.</div>

		<div id="dateArea">
			<span style="font-size: 20px;">시작일</span> <input type="date"
				id="startDate" name="startDate" required /> <span
				style="font-size: 20px;">종료일</span> <input type="date" id="endDate"
				name="endDate" required />

			<button type="button" class="btn-check" onclick="submitDate()">확인</button>
		</div>

		<!-- 날짜 출력 -->
		<div id="dateInfo"
			style="margin-top: 10px; font-size: 20px; color: #ff9670; font-weight: bold;"></div>
	</div>


	<!-- 사진 추가 -->
	<div class="menuPicture">
		<div class="menu-label">펀딩 사진을 선택해 주세요.</div>
		<p>
			<strong>최대 2개까지</strong> 메뉴 사진을 넣어주세요! <strong>사진 추가하기</strong> 버튼을
			누르면 사진을 선택할 수 있어요. <br> 사진을 삭제하려면, 사진 밑에 있는 <strong>취소하기</strong>
			버튼을 눌러주세요.
		</p>


		<button type="button" id="btnAddPhoto">펀딩 사진 추가하기</button>
		<input type="file" id="inputPhoto" accept="image/*" multiple
			style="display: none" />

		<button type="button" id="btnDefaultPhoto">메뉴 사진과 동일</button>

		<!-- 사진 미리보기 -->
		<div id="previewContainer" class="preview-container"></div>
	</div>

	<div class="btn-container">
		<c:set var="type" value="${sessionScope.fundingType}" />
		<button class="btn" type="button"
			onclick="location.href='${pageContext.request.contextPath}/seller/fundings/create-step2?type=${type}'">이전</button>
		<button class="btn" type="submit">다음</button>
	</div>
</form>

<!-- 모달 영역 -->
<div id="resultModal">
	<p id="modalMsg"></p>
	<button id="closeModalBtn">확인</button>
</div>


<script>
let isDateConfirmed = false;

function showModalMessage(message) {
	  $("#modalMsg").text(message);
	  $("#resultModal").fadeIn();
	}

function submitDate() {
	  const start = document.getElementById("startDate").value;
	  const end = document.getElementById("endDate").value;

	  if (!start || !end) {
		showModalMessage("시작일과 종료일을 모두 입력해 주세요.");
	    return;
	  }

	  const startDateObj = new Date(start);
	  const endDateObj = new Date(end);

	  // 종료일이 시작일보다 이전일 경우
	  if (endDateObj < startDateObj) {
		showModalMessage("종료일은 시작일보다 이후여야 합니다.");
	    return;
	  }

	  const formattedStart = `\${startDateObj.getFullYear()}년 \${startDateObj.getMonth() + 1}월 \${startDateObj.getDate()}일`;
	  const formattedEnd = `\${endDateObj.getFullYear()}년 \${endDateObj.getMonth() + 1}월 \${endDateObj.getDate()}일`;

	  document.getElementById("dateInfo").innerText =
	    `\${formattedStart} 0시 ~ \${formattedEnd} 23시 59분까지 펀딩이 진행됩니다.`;

	  isDateConfirmed = true;
	  showModalMessage("날짜가 확인되었습니다!");
	}


function checkConfirmed() {
	  if (!isDateConfirmed) {
	    showModalMessage("먼저 '확인' 버튼을 눌러 날짜를 제출해 주세요.");
	    return false;
	  }
	  return true;
	}


// 사진 미리보기
const btnAddPhoto = document.getElementById('btnAddPhoto');
const inputPhoto = document.getElementById('inputPhoto');
const previewContainer = document.getElementById('previewContainer');

btnAddPhoto.addEventListener('click', () => inputPhoto.click());

inputPhoto.addEventListener('change', (e) => {
  const files = e.target.files;
  for (let i = 0; i < files.length; i++) {
    const file = files[i];
    if (!file.type.startsWith('image/')) continue;

    const reader = new FileReader();
    reader.onload = function (event) {
      const div = document.createElement('div');
      div.classList.add('preview-item');

      const img = document.createElement('img');
      img.src = event.target.result;
      img.alt = file.name;

      const btnCancel = document.createElement('button');
      btnCancel.textContent = '취소하기';
      btnCancel.classList.add('btn-cancel');

      btnCancel.addEventListener('click', () => {
        div.remove();
      });

      div.appendChild(img);
      div.appendChild(btnCancel);
      previewContainer.appendChild(div);
    };
    reader.readAsDataURL(file);
  }
});

$(function () {
	  $("#closeModalBtn").on("click", function () {
	    $("#resultModal").fadeOut();
	  });
	});

</script>

