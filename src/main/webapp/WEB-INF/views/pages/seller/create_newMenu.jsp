<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>새로운 메뉴 등록</title>
</head>
<body>
	<h3>새로운 메뉴에 대한 정보를 입력해 주세요</h3>
	<form action="${pageContext.request.contextPath}/seller/store/create-step3" method="post">
		
		<!-- 메뉴 이름 입력 -->
		<div class="menuName">
			<div class="menu-label">메뉴 이름을 적어주세요</div>
			<div class="menu-input">
				<input type="text" class="menu-input" placeholder="예: 불고기 정식" name="menuName" required>
			</div>
		</div>

		<!-- 메뉴의 정가 입력 -->
		<div class="menuPrice">
			<div class="menu-label">메뉴의 원래 가격(정가)을 입력해 주세요</div>
			<div class="menu-input">
				<input type="number" class="price-input" placeholder="예: 12000" name="menuPrice" required>
			</div>
		</div>

		<!-- 메뉴의 설명 입력 -->
		<div class="menuDescription">
			<div class="menu-label">메뉴에 대한 설명을 적어주세요</div>
			<div class="menu-input">
				<input type="text" class="description-input" placeholder="예: 상품 설명은 비워도 괜찮아요. 꼭 작성하지 않아도 등록할 수 있어요."
					name="menuDescription">
			</div>
		</div>

		<!-- 사진 추가 -->
		<div class="menuPicture">
			<div class="menu-label">
				메뉴 사진을 넣어주세요! <strong>사진 추가하기</strong> 버튼을 누르면 사진을 선택할 수 있어요. <br>
				사진을 삭제하려면, 사진 밑에 있는 <strong>취소하기</strong> 버튼을 눌러주세요.
			</div>

			<button id="btnAddPhoto">사진 추가하기</button>
			<input type="file" id="inputPhoto" accept="image/*" multiple style="display: none" />

			<!-- 사진 미리보기 -->
			<div class="preview-container" id="previewContainer"></div>
		</div>

		<!-- 이동 버튼 -->
		<button type="button" onclick="goBack()">뒤로가기</button>
		<button type="submit">등록하기</button>
	</form>


<script>
 const btnAddPhoto = document.getElementById('btnAddPhoto'); //사진 추가하기
 const inputPhoto = document.getElementById('inputPhoto'); //숨겨진 파일 선택 input -> 사진 추가하기 버튼을 누르면 실제 파일이 선택되도록
 const previewContainer = document.getElementById('previewContainer'); //이미지 미리보기 넣을 컨테이너

 // 사진 추가하기 버튼 클릭 시 파일 선택창 열기
 btnAddPhoto.addEventListener('click', () => {
   inputPhoto.click();
 });

 // 파일 선택 시 미리보기 생성
 inputPhoto.addEventListener('change', (e) => {
   const files = e.target.files;
   for(let i=0; i<files.length; i++){
     const file = files[i];

     // 이미지 파일인지 체크
     if(!file.type.startsWith('image/')) continue;

     const reader = new FileReader();
     reader.onload = function(event) {
       // 미리보기 박스 생성
       const div = document.createElement('div');
       div.classList.add('preview-item');

       // 이미지 엘리먼트
       const img = document.createElement('img');
       img.src = event.target.result;
       img.alt = file.name;

       // 취소하기 버튼 생성
       const btnCancel = document.createElement('button');
       btnCancel.textContent = '취소하기';
       btnCancel.classList.add('btn-cancel');

       // 취소 버튼 클릭 시 해당 미리보기 삭제
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

function goBack() {
	window.history.back();
}
</script>
</body>
</html>