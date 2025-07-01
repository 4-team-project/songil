<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>step2. 기간 및 이미지</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<h2>상점이름: ${store.storeName }</h2>
	<h2>펀딩 시작일과 종료일을 입력해 주세요</h2>
	<form action="${pageContext.request.contextPath}/seller/create-step4" method="post"
		onsubmit="return checkConfirmed();">

		<!-- 날짜 입력 영역: form 제거, 새로고침 방지 -->
		<div id="dateArea">
			<label for="startDate">시작일</label> <input type="date" id="startDate"
				name="startDate" required /> <label for="endDate">종료일</label> <input
				type="date" id="endDate" name="endDate" required />

			<button type="button" onclick="submitDate()">확인</button>
		</div>

		<!-- 날짜 출력 -->
		<div id="dateInfo" style="margin-top: 10px;"></div>

		<h2>펀딩 사진을 선택해 주세요</h2>
		<!-- 사진 추가 -->
		<div class="menuPicture">
			<div class="menu-label">
				메뉴 사진을 넣어주세요! <strong>사진 추가하기</strong> 버튼을 누르면 사진을 선택할 수 있어요. <br>
				사진을 삭제하려면, 사진 밑에 있는 <strong>취소하기</strong> 버튼을 눌러주세요.
			</div>

			<button type="button" id="btnAddPhoto">펀딩 사진 추가하기</button>
			<input type="file" id="inputPhoto" accept="image/*" multiple
				style="display: none" />

			<button type="button" id="btnDefaultPhoto">메뉴 사진과 동일</button>

			<!-- 사진 미리보기 -->
			<div class="preview-container" id="previewContainer"></div>
		</div>

		<!-- 다음 단계 -->
		<button type="submit">다음</button>
	</form>

	<!-- 이전 -->
	<button type="button" onclick="goBack()">이전</button>

	<script>
let isDateConfirmed = false;

function submitDate() {
  const start = document.getElementById("startDate").value;
  const end = document.getElementById("endDate").value;

  if (!start || !end) {
    alert("시작일과 종료일을 모두 입력해 주세요.");
    return;
  }

  // 날짜 포맷 변환 (YYYY년 M월 D일)
  const startDateObj = new Date(start);
  const endDateObj = new Date(end);

  const formattedStart = `\${startDateObj.getFullYear()}년 \${startDateObj.getMonth() + 1}월 \${startDateObj.getDate()}일`;
  const formattedEnd = `\${endDateObj.getFullYear()}년 \${endDateObj.getMonth() + 1}월 \${endDateObj.getDate()}일`;

  document.getElementById("dateInfo").innerText =
    `\${formattedStart} 0시 ~ \${formattedEnd} 23시 59분까지 펀딩이 진행됩니다.`;

  isDateConfirmed = true;
  alert("날짜가 확인되었습니다!");
}

function checkConfirmed() {
  if (!isDateConfirmed) {
    alert("먼저 '확인' 버튼을 눌러 날짜를 제출해 주세요.");
    return false;
  }
  return true;
}

function goBack() {
  window.history.back();
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
</script>

</body>
</html>
