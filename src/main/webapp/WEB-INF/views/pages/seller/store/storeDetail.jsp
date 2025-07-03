<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="${cpath}/resources/js/address.js"></script>
<script
	src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/productDetail.css">

<div class="main-title-box">
	<div class="main-title">
		새롭게 운영하실 상점의 이름, 주소, 전화번호 등을 입력해 주세요<br /> 모든 정보를 다 입력하신 후 아래 [등록하기]
		버튼을 눌러주시면 등록이 완료됩니다
	</div>
</div>

<div class="content-box">
	<div class="content-text">상점 이름</div>
	<input type="text" id="storeName" placeholder="상점 이름을 입력하세요"
		class="content-input" />
</div>

<div class="content-box">
	<div class="content-text">상점 주소</div>
	<div class="address-section">
		<p class="address-row">
			<button type="button" class="auth-btn" onclick="execDaumPostcode()">주소
				검색</button>
			<button type="button" class="auth-btn" id="clearAddressBtn">지우기</button>
			<input type="text" name="postcode" id="postcode"
				class="content-input" placeholder="우편번호" readonly>
		</p>
		<input type="hidden" id="sido" name="sido"> 
		<input type="hidden" id="sigungu" name="sigungu">
		<input type="hidden" id="bname" name="bname">
		<input type="hidden" id="jibunAddress" name="jibunAddress">
		<p>
			<strong>&nbsp&nbsp&nbsp&nbsp&nbsp</strong> <input type="text"
				name="roadAddress" id="roadAddress" class="content-input"
				placeholder="도로명 주소" readonly>
		</p>
		<input type="text" id="detailAddr" class="content-input" placeholder="상세 주소를 입력하세요">
	</div>
</div>

<div class="content-box">
	<div class="content-text">상점 소개</div>
	<textarea id="storeDescription" class="content-textarea"
		placeholder="상점 설명은 비워도 괜찮아요&#13;꼭 작성하지 않아도 등록할 수 있어요"></textarea>
</div>

<div class="content-box">
	<div class="content-text">상점 카테고리를 선택해 주세요(한 가지만 선택 가능합니다)</div>
	<div class="category-box" id="storeCategory"></div>
</div>

<div class="content-box">
	<div class="content-text">계좌 번호</div>
	<input type="text" id="accountNumber" placeholder="계좌번호를 입력하세요"
		class="content-input" />
</div>

<div class="content-box">
	<div class="content-text">사업자등록번호</div>
	<input type="text" id="businessRegistrationNumber"
		placeholder="사업자등록번호를 입력하세요" class="content-input" />
</div>

<div class="complete-back-btn-box">
	<div class="complete-back-btn" onclick="history.back()">이전</div>
	<button onclick="submitStore()" class="complete-back-btn">수정
		완료</button>
</div>

<input type="hidden" id="selectedCategoryId" value="">

<script>
window.onload = function () {
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "<%=request.getContextPath()%>/resources/data/categories.json", true);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      var data = JSON.parse(xhr.responseText);
      var container = document.getElementById("storeCategory");
      container.innerHTML = "";

      for (var i = 0; i < data.length; i++) {
        var category = data[i];
        if (category.id === 0) continue; 

        var card = document.createElement("div");
        card.className = "category-card";
        card.setAttribute("data-category-id", category.id);

        var imgDiv = document.createElement("div");
        imgDiv.className = "category-card-img";
        var img = document.createElement("img");
        img.src = "<%=request.getContextPath()%>" + category.image;
        img.alt = category.name;
        imgDiv.appendChild(img);

        var nameDiv = document.createElement("div");
        nameDiv.className = "category-card-name";
        nameDiv.innerText = category.name;

        card.appendChild(imgDiv);
        card.appendChild(nameDiv);

        card.onclick = function () {
          var allCards = document.getElementsByClassName("category-card");
          for (var j = 0; j < allCards.length; j++) {
            allCards[j].classList.remove("selected");
          }
          this.classList.add("selected");

          document.getElementById("selectedCategoryId").value = this.getAttribute("data-category-id");
        };

        container.appendChild(card);
      }
    }
  };
  xhr.send();
};
</script>

<script>
function submitStore() {
	console.log(document.getElementById('bname'))
	console.log(document.getElementById('sido'))
	console.log(document.getElementById('sigungu'))
	console.log(document.getElementById('detailAddr'))
	console.log(document.getElementById('storeDescription'))
	const storeData = {
			  storeName: document.getElementById('storeName').value,
			  sido: document.getElementById('sido').value,
			  sigungu: document.getElementById('sigungu').value,
			  dong : document.getElementById('bname').value,
			  addressDetail: document.getElementById('detailAddr').value,
			  description: document.getElementById('storeDescription').value,
			  userId: 1,
			  businessNumber: document.getElementById('businessRegistrationNumber').value,
			  bankAccount: document.getElementById('accountNumber').value,
			  categoryId: parseInt(document.getElementById('selectedCategoryId').value),
			};


  fetch("${cpath}/seller/store/insert", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(storeData)
  })
  .then(res => res.text())
  .then(msg => alert("전송 완료: " + msg))
  .catch(err => alert("전송 오류: " + err));
}
</script>
