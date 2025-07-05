<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<link rel="stylesheet" href="${cpath}/resources/css/pages/seller/productDetail.css">
<link rel="stylesheet" href="${cpath}/resources/css/pages/seller/settlements.css">
<link rel="stylesheet" href="${cpath}/resources/css/pages/seller/storeList.css">

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
  return item ? item.image : '/project/resources/images/category/default.svg';
}

function loadStoreListPage(page) {
  console.log("유저 아이디 확인:", userId);
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
        console.log(imageUrl);
        html += `
          <div class="store-card">
            <div class="store-info-left">
              <div class="store-title" style="display:flex; align-items:center;">
                <img src="${cpath}/\${imageUrl}" alt="${store.categoryName}" style="width: 40px; height: 40px; margin-right: 8px;">
                \${store.storeName} (\${store.storeId})
              </div>
              <div class="store-detail">\${store.sido} \${store.sigungu} \${store.dong}</div>
              <div class="store-detail">\${store.categoryName} · ⭐ <div class="store-detail-color">\${rating}</div></div>
            </div>
            <button class="btn">상세 보기</button>
          </div>
        `;
      });

      html += `<div class="pagination">`;
      for (let i = 1; i <= totalPages; i++) {
        if (i === currentPage) {
          html += `<button class="page-link active" disabled>\${i}</button>`;
        } else {
          html += `<button class="page-link" data-page="${i}">\${i}</button>`;
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
      console.error("카테고리 JSON 로딩 실패:", err);
      loadStoreListPage(1);  // 기본 이미지만 사용해서라도 표시
    });
});

// 페이지네이션
$(document).on("click", ".page-link", function(e) {
  e.preventDefault();
  const page = $(this).data("page");
  loadStoreListPage(page);
});
</script>
