<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/productDetail.css">
<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/settlements.css">
<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/storeList.css">
<%@ include file="/WEB-INF/views/common/sellerModal.jsp" %>

<script>
  const cpath = '${cpath}';
</script>

<div class="main-title-box">
	<div class="main-title" id="typingText">00 사장님의 상점 목록입니다</div>
</div>

<div class="store-list" id="store-list"></div>

<script>
const userId = 1;
let categoryJson = [];

function getImageByCategory(name) {
  const item = categoryJson.find(c => c.name === name);
  return item.image;
}

function loadStoreListPage(page) {
  $("#store-list").empty();
  $.ajax({
    url: `${cpath}/seller/store/list/byUserId`,
    method: 'GET',
    data: { page: page, userId: userId },
    success: function(data) {
      const list = data.storelist;
      const currentPage = data.currentPage;
      const totalPages = data.totalPages;
      const ratingMap = data.ratingMap;
      let html = '';

      list.forEach(store => {
        const rating = ratingMap[String(store.storeId)] || 0.0;
        const imageUrl = getImageByCategory(store.categoryName);
        html += `
          <div class="store-card">
            <div class="store-info-left">
              <div class="store-title" style="display:flex; align-items:center;">
                <img src="${cpath}/\${imageUrl}" alt="${store.categoryName}" style="width: 40px; height: 40px; margin-right: 8px;">
                \${store.storeName}
              </div>
                <div class="store-info-middle">
                <div class="store-info-middle-text">
              <div class="store-detail">\${store.sido} \${store.sigungu} \${store.dong} · \${store.categoryName}</div>
              <div class="store-detail"><div class="store-detail-color"><img src="${cpath}/resources/images/icons/solar_star-bold.svg" alt="star" style="width: 20px; height: 20px; margin-right: 4px;">\${rating}</div></div>
              </div>
              <button class="btn">현재 상점으로 변경</button>
              </div>
              </div>
              <div class="store-buttons">
              <button class="btn" onclick="location.href='${cpath}/seller/store/edit'">상세보기</button>
              <button class="btn-delete" data-store-id="\${store.storeId}">삭제하기</button>
            </div>
          </div>
        `;
      });

      html += `<div class="pagination">`;
      for (let i = 1; i <= totalPages; i++) {
        if (i === currentPage) {
          html += `<button class="page-link active" disabled>\${i}</button>`;
        } else {
          html += `<button class="page-link" data-page="\${i}">\${i}</button>`;
        }
      }
      html += `</div>`;
      $("#store-list").html(html);
    },
    error: function() {
      alert("상점 목록을 불러오는 데 실패했습니다.");
    }
  });
}

// 외부 JSON 먼저 로드 후 실행
$(document).ready(function() {
  fetch('${cpath}/resources/data/categories.json')
    .then(res => res.json())
    .then(data => {
      categoryJson = data;
      loadStoreListPage(1);
    })
    .catch(err => {
      loadStoreListPage(1); 
    });
});

// 페이지네이션
$(document).on("click", ".page-link", function(e) {
  e.preventDefault();
  const page = $(this).data("page");
  loadStoreListPage(page);
});

//삭제 버튼 클릭 시
$(document).on("click", ".btn-delete", function () {
  const storeName = $(this).data("storeName");

  openModal({
    title: "상점 삭제 확인",
    body: `<div class="modal-info">
             <strong>\${storeName} 상점을 삭제하시겠습니까?</strong>
             <p class="cancel-guide">삭제하시면 되돌릴 수 없습니다. <br />괜찮으시면 아래 ‘삭제’ 버튼을 눌러주세요.</p>
           </div>`,
    cancelText: "취소",
    confirmText: "삭제",
    onConfirm: function () {
      deleteStore(storeId);
      closeModal(); 
    }
  });
});


</script>
