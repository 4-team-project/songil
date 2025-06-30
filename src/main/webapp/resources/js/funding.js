let currentPage = 1;
let lastParams = {};
let isFullList = false;

// 초기 펀딩 숨기기
function hideInitialContent() {
  const initialContent = document.getElementById('initialContent');
  const moreButton = document.getElementById('moreButton');
  const filteredWrapper = document.getElementById('filteredFundingListBox');
  if (initialContent) initialContent.style.display = 'none';
  if (moreButton) moreButton.style.display = 'none';
  if (filteredWrapper) filteredWrapper.style.display = 'flex';
}

// 초기 펀딩 보이기
function showInitialContent() {
  const initialContent = document.getElementById('initialContent');
  const moreButton = document.getElementById('moreButton');
  const filteredWrapper = document.getElementById('filteredFundingListBox');
  if (initialContent) initialContent.style.display = 'flex';
  if (moreButton) moreButton.style.display = 'flex';
  if (filteredWrapper) filteredWrapper.style.display = 'none';
}

// 필터링 후 펀딩 렌더링
function renderFundingList(fundingList, append = false) {
  const filteredWrapper = document.getElementById('filteredFundingListBox');
  if (!append) filteredWrapper.innerHTML = '';

  let allFundingsHTML = `
    <div class="funding-filter-box" id="sortButtons">
      <div class="funding-filter selected" data-sort-id="popular">
        <div class="funding-filter-text">인기순</div>
      </div>
      <div class="funding-filter" data-sort-id="latest">
        <div class="funding-filter-text">최신순</div>
      </div>
      <div class="funding-filter" data-sort-id="closing">
        <div class="funding-filter-text">마감 임박 순</div>
      </div>
    </div>
<div class="funding-list-wrapper">
      <div class="filteredFundingList">
  `;

  fundingList.forEach(funding => {
    const avgRating = funding.avgRating?.toFixed(1) || '0.0';
    const reviewCnt = funding.reviewCnt ?? 0;
    const percent = funding.targetQty > 0 ? Math.round((funding.currentQty * 100) / funding.targetQty) : 0;
    const discount = funding.price > 0 ? Math.round(((funding.price - funding.salePrice) * 100) / funding.price) : 0;
    const imageUrl = funding.images?.[0]?.imageUrl ? `${cpath}${funding.images[0].imageUrl}` : '';
    const imageHtml = imageUrl ? `<img class="funding-image" src="${imageUrl}" alt="펀딩 이미지" />` : '<div class="funding-image"></div>';
    const daysLeft = funding.daysLeft;

    allFundingsHTML += `
        <div class="funding-box" onclick="location.href='${cpath}/fundings/${funding.fundingId}'">
          ${imageHtml}
          <div class="funding-contents">
            <div class="funding-place">${funding.storeName}</div>
            <div class="funding-title">${funding.fundingName}</div>
            <div class="rating-box">
              <img class="rating-img" src="${cpath}/resources/images/icons/rating.svg" alt="rating" />
              <div class="rating-text">${avgRating}</div>
              <div class="review-text">(${reviewCnt})</div>
            </div>
            <div class="percent-box">
              <div class="percent-text">${discount}%</div>
              <div class="regular-price-text">${funding.price.toLocaleString()}</div>
            </div>
            <div class="price-text">${funding.salePrice.toLocaleString()}원</div>
            <div class="funding-progress-box">
              <div class="funding-progress-text-box">
                <div class="funding-progress-text">${percent}%</div>
              </div>
              <div class="funding-date-box">
                <div class="funding-date">${daysLeft}일</div>
                <div class="funding-date-text">남음</div>
              </div>
            </div>
            <div class="funding-bar">
              <div class="funding-bar-inner" style="width: ${percent}%;"></div>
            </div>
          </div>
        </div>
    `;
  });
  
  allFundingsHTML += `
		    </div> <!-- filteredFundingList -->
		  </div>   <!-- funding-list-wrapper -->
		`;

  filteredWrapper.insertAdjacentHTML('beforeend', allFundingsHTML);
  bindSortButtons(); // 동적으로 생성된 버튼에 다시 이벤트 바인딩
}

// 정렬 버튼 바인딩
function bindSortButtons() {
  const filters = document.querySelectorAll('.funding-filter');
  filters.forEach(el => {
    el.addEventListener('click', (event) => {
      event.preventDefault();
      filters.forEach(f => f.classList.remove('selected'));
      el.classList.add('selected');

      currentPage = 1;
      const sort = el.dataset.sortId;
      loadFundings({ sort });
    });
  });
}

// 펀딩 데이터 불러오기
function loadFundings(params = {}) {
  if (Object.keys(params).length > 0 || currentPage > 1) {
    hideInitialContent();
  } else {
    showInitialContent();
  }

  lastParams = { ...lastParams, ...params };
  if (lastParams.sido === '' || lastParams.sido === '시/도 선택') delete lastParams.sido;
  if (lastParams.sigungu === '' || lastParams.sigungu === '시/군/구 선택') delete lastParams.sigungu;

  const pageSize = isFullList ? 9999 : 8;
  const query = new URLSearchParams({ ...lastParams, page: currentPage, size: pageSize }).toString();

  fetch(`${cpath}/fundings/search/json?${query}`)
    .then(res => res.json())
    .then(data => {
      renderFundingList(data.fundinglist, false);
    })
    .catch(err => console.error('펀딩 로딩 실패:', err));
}

// DOM 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
  bindSortButtons();

  document.getElementById('findBtn')?.addEventListener('click', () => {
    const sido = document.getElementById('sidoButton').textContent.trim();
    const sigungu = document.getElementById('sigunguButton').textContent.trim();
    currentPage = 1;
    isFullList = false;
    loadFundings({ sido, sigungu });
  });

  document.getElementById('moreButton')?.addEventListener('click', () => {
    currentPage = 1;
    isFullList = true;
    const selectedSort = document.querySelector('.funding-filter.selected')?.dataset.sortId || 'popular';
    loadFundings({ sort: selectedSort });
  });
});

// 전역 함수 등록
window.loadFundings = loadFundings;
window.currentPage = currentPage;
