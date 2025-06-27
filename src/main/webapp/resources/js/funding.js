let currentPage = 1;
let lastParams = {};

function hideInitialContent() {
  const initialContent = document.getElementById('initialContent');
  const moreButton = document.getElementById('moreButton');
  const filteredWrapper = document.getElementById('filteredFundingList');
  if (initialContent) initialContent.style.display = 'none';
  if (moreButton) moreButton.style.display = 'none';
  if (filteredWrapper) filteredWrapper.style.display = 'flex';
}

function showInitialContent() {
  const initialContent = document.getElementById('initialContent');
  const moreButton = document.getElementById('moreButton');
  const filteredWrapper = document.getElementById('filteredFundingList');
  if (initialContent) initialContent.style.display = 'block';
  if (moreButton) moreButton.style.display = 'block';
  if (filteredWrapper) filteredWrapper.style.display = 'none';
}

function renderFundingList(fundingList, append = false) {
  const filteredWrapper = document.getElementById('filteredFundingList');
  if (!append) filteredWrapper.innerHTML = '';

  let allFundingsHTML = '';

  fundingList.forEach(funding => {
    const avgRating = funding.avgRating?.toFixed(1) || '0.0';
    const reviewCnt = funding.reviewCnt ?? 0;
    const percent = funding.targetQty > 0 ? Math.round((funding.currentQty * 100) / funding.targetQty) : 0;
    const discount = funding.price > 0 ? Math.round(((funding.price - funding.salePrice) * 100) / funding.price) : 0;
    const imageUrl = funding.images?.[0]?.imageUrl ? `${cpath}${funding.images[0].imageUrl}` : '';
    const imageHtml = imageUrl ? `<img class="funding-image" src="${imageUrl}" alt="펀딩 이미지" />` : '<div class="funding-image"></div>';
    const daysLeft = funding.daysLeft;

    const html = `
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
    allFundingsHTML += html;
  });

  filteredWrapper.insertAdjacentHTML('beforeend', allFundingsHTML);
}

function loadFundings(params = {}) {
  if (Object.keys(params).length > 0 || currentPage > 1) {
    hideInitialContent();
  } else {
    showInitialContent();
  }

  lastParams = { ...lastParams, ...params };
  const query = new URLSearchParams({ ...lastParams, page: currentPage, size: 12 }).toString();

  fetch(`${cpath}/fundings/search/json?${query}`)
    .then(res => res.json())
    .then(data => {
      if (currentPage === 1) {
        renderFundingList(data.fundinglist, false);
      } else {
        renderFundingList(data.fundinglist, true);
      }
    })
    .catch(err => console.error('펀딩 로딩 실패:', err));
}

document.addEventListener('DOMContentLoaded', () => {
  loadFundings();

  const filters = document.querySelectorAll('.funding-filter');
  filters.forEach(el => {
    el.onclick = () => {
      filters.forEach(f => f.classList.remove('selected'));
      el.classList.add('selected');
      currentPage = 1;
      loadFundings({ sort: el.dataset.sortId });
    };
  });

  document.getElementById('findBtn')?.addEventListener('click', () => {
    const sido = document.getElementById('sidoButton').textContent.trim();
    const sigungu = document.getElementById('sigunguButton').textContent.trim();
    currentPage = 1;
    loadFundings({ sido, sigungu });
  });

  document.getElementById('moreButton')?.addEventListener('click', () => {
    currentPage++;
    loadFundings();
  });
});

// 전역 등록
window.loadFundings = loadFundings;
window.currentPage = currentPage;