<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet" href="${cpath}/resources/css/layout/seller-header.css" />

<div class="box"></div>
<div class="header-box">
  <div class="logo" onclick="location.href='${cpath}/seller/home'">
    <img src="${cpath}/resources/images/logo.svg" alt="logo" />
  </div>
  <div class="lower-box">
    <div class="store-box">
      <span class="store-text">
        <c:choose>
          <c:when test="${not empty currentStore}">
            ${currentStore.storeName}
          </c:when>
          <c:otherwise>상점 없음</c:otherwise>
        </c:choose>
      </span>
      <div class="dropdown">
        <img 
          src="${cpath}/resources/images/icons/drop-down.svg" 
          class="dropdown-icon" 
          onclick="toggleDropdown()" 
        />
        <ul class="dropdown-menu" id="storeDropdown" style="display: none;">
          <c:forEach var="store" items="${storeList}">
            <li onclick="changeStore('${store.storeId}')">${store.storeName}</li>
          </c:forEach>
        </ul>
      </div>
    </div>
    <div class="nav-box">
      <div class="nav-text" onclick="location.href='${cpath}/auth/login'">로그아웃</div>
    </div>
  </div>
</div>

<script>
  const storeList = [
    <c:forEach var="store" items="${storeList}" varStatus="status">
      {
        storeId: ${store.storeId},
        storeName: "${store.storeName}"
      }<c:if test="${!status.last}">,</c:if>
    </c:forEach>
  ];
  console.log("storeList:", storeList);
</script>


<script>
let dropdownVisible = false;

function toggleDropdown() {
  const dropdown = document.getElementById('storeDropdown');
  console.log("toggleDropdown 호출됨");

  if (!dropdown) {
    console.warn("dropdown 요소 없음");
    return;
  }

  dropdownVisible = !dropdownVisible;
  dropdown.style.display = dropdownVisible ? 'block' : 'none';
  console.log("드롭다운 상태 변경됨:", dropdown.style.display);

  if (dropdownVisible) {
    // 외부 클릭 감지 등록
    document.addEventListener("click", handleOutsideClick);
  } else {
    document.removeEventListener("click", handleOutsideClick);
  }
}

function handleOutsideClick(event) {
  const dropdown = document.getElementById("storeDropdown");
  const icon = document.querySelector(".dropdown-icon");

  console.log("document 클릭 발생");
  console.log("클릭한 요소:", event.target);
  const isClickInside = dropdown.contains(event.target);
  const isClickIcon = icon.contains(event.target);

  console.log("dropdown 내부 클릭?:", isClickInside);
  console.log("아이콘 클릭?:", isClickIcon);

  if (!isClickInside && !isClickIcon) {
    dropdown.style.display = "none";
    dropdownVisible = false;
    document.removeEventListener("click", handleOutsideClick);
    console.log("드롭다운 닫힘");
  }
}


	function handleOutsideClick(event) {
	  const dropdown = document.getElementById("storeDropdown");
	  const icon = document.querySelector(".dropdown-icon");

	  if (!dropdown || !icon) return;

	  const isClickInsideDropdown = dropdown.contains(event.target);
	  const isClickOnIcon = icon.contains(event.target);

	  if (!isClickInsideDropdown && !isClickOnIcon) {
	    dropdown.style.display = "none";
	    console.log("드롭다운 닫힘");
	    document.removeEventListener("click", handleOutsideClick); // 한번만 실행
	  }
	}


  function changeStore(storeId) {
    console.log("changeStore 호출됨, 전달받은 storeId:", storeId);

    fetch(`${cpath}/seller/store/changeStore`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ storeId })
    })
    .then(res => {
      console.log("서버 응답 수신");
      return res.text();
    })
    .then(msg => {
      console.log("서버 메시지:", msg);
      alert(msg);
      location.reload();
    })
    .catch(err => {
      console.error("상점 변경 실패:", err);
      alert("상점 변경 실패: " + err);
    });
  }

  // 바깥 클릭 시 드롭다운 닫기
  document.addEventListener("click", function(event) {
    console.log("document 클릭 발생");

    const dropdown = document.getElementById("storeDropdown");
    const icon = document.querySelector(".dropdown-icon");

    if (!dropdown || !icon) {
      console.warn("dropdown 또는 icon 요소를 찾지 못함");
      return;
    }

    const isClickInsideDropdown = dropdown.contains(event.target);
    const isClickOnIcon = icon.contains(event.target);

    console.log("클릭한 요소:", event.target);
    console.log("dropdown 내부 클릭?:", isClickInsideDropdown);
    console.log("아이콘 클릭?:", isClickOnIcon);

    if (!isClickInsideDropdown && !isClickOnIcon) {
      dropdown.style.display = "none";
      console.log("드롭다운 닫힘");
    }
  });
</script>
