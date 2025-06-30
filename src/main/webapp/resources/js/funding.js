var currentPage = 1;
var lastParams = {};
function hideInitialContent() {
  var initialContent = document.getElementById('initialContent');
  var moreButton = document.getElementById('moreButton');
  var filteredWrapper = document.getElementById('filteredFundingList');
  if (initialContent) initialContent.style.display = 'none';
  if (moreButton) moreButton.style.display = 'none';
  if (filteredWrapper) filteredWrapper.style.display = 'flex';
}
function showInitialContent() {
  var initialContent = document.getElementById('initialContent');
  var moreButton = document.getElementById('moreButton');
  var filteredWrapper = document.getElementById('filteredFundingList');
  if (initialContent) initialContent.style.display = 'block';
  if (moreButton) moreButton.style.display = 'block';
  if (filteredWrapper) filteredWrapper.style.display = 'none';
}
function renderFundingList(fundingList, append) {
  if (append !== true) append = false;
  var filteredWrapper = document.getElementById('filteredFundingList');
  if (!append) filteredWrapper.innerHTML = '';
  var allFundingsHTML = '';
  for (var i = 0; i < fundingList.length; i++) {
    var funding = fundingList[i];
    var avgRating = (funding.avgRating != null) ? funding.avgRating.toFixed(1) : '0.0';
    var reviewCnt = (funding.reviewCnt != null) ? funding.reviewCnt : 0;
    var percent = (funding.targetQty > 0) ? Math.round((funding.currentQty * 100) / funding.targetQty) : 0;
    var discount = (funding.price > 0) ? Math.round(((funding.price - funding.salePrice) * 100) / funding.price) : 0;
    var imageUrl = (funding.images && funding.images[0] && funding.images[0].imageUrl) ? (cpath + funding.images[0].imageUrl) : '';
    var imageHtml = imageUrl ? '<img class="funding-image" src="' + imageUrl + '" alt="펀딩 이미지" />' : '<div class="funding-image"></div>';
    var daysLeft = funding.daysLeft;
    var html = '' +
      '<div class="funding-box" onclick="location.href=\'' + cpath + '/fundings/' + funding.fundingId + '\'">' +
      imageHtml +
      '<div class="funding-contents">' +
        '<div class="funding-place">' + funding.storeName + '</div>' +
        '<div class="funding-title">' + funding.fundingName + '</div>' +
        '<div class="rating-box">' +
          '<img class="rating-img" src="' + cpath + '/resources/images/icons/rating.svg" alt="rating" />' +
          '<div class="rating-text">' + avgRating + '</div>' +
          '<div class="review-text">(' + reviewCnt + ')</div>' +
        '</div>' +
        '<div class="percent-box">' +
          '<div class="percent-text">' + discount + '%</div>' +
          '<div class="regular-price-text">' + funding.price.toLocaleString() + '</div>' +
        '</div>' +
        '<div class="price-text">' + funding.salePrice.toLocaleString() + '원</div>' +
        '<div class="funding-progress-box">' +
          '<div class="funding-progress-text-box">' +
            '<div class="funding-progress-text">' + percent + '%</div>' +
          '</div>' +
          '<div class="funding-date-box">' +
            '<div class="funding-date">' + daysLeft + '일</div>' +
            '<div class="funding-date-text">남음</div>' +
          '</div>' +
        '</div>' +
        '<div class="funding-bar">' +
          '<div class="funding-bar-inner" style="width: ' + percent + '%;"></div>' +
        '</div>' +
      '</div>' +
    '</div>';
    allFundingsHTML += html;
  }
  filteredWrapper.insertAdjacentHTML('beforeend', allFundingsHTML);
}
function loadFundings(params) {
  if (!params) params = {};
  var hasParams = Object.keys(params).length > 0;
  if (hasParams || currentPage > 1) {
    hideInitialContent();
  } else {
    showInitialContent();
  }
  for (var key in params) {
    lastParams[key] = params[key];
  }
  var queryObj = Object.assign({}, lastParams, { page: currentPage, size: 12 });
  var query = new URLSearchParams(queryObj).toString();
  fetch(cpath + '/fundings/search/json?' + query)
    .then(function(res) { return res.json(); })
    .then(function(data) {
      if (currentPage === 1) {
        renderFundingList(data.fundinglist, false);
      } else {
        renderFundingList(data.fundinglist, true);
      }
    })
    .catch(function(err) {
      console.error('펀딩 로딩 실패:', err);
    });
}
document.addEventListener('DOMContentLoaded', function() {
  loadFundings();
  var filters = document.querySelectorAll('.funding-filter');
  filters.forEach(function(el) {
    el.onclick = function() {
      filters.forEach(function(f) {
        f.classList.remove('selected');
      });
      el.classList.add('selected');
      currentPage = 1;
      loadFundings({ sort: el.dataset.sortId });
    };
  });
  var findBtn = document.getElementById('findBtn');
  if (findBtn) {
    findBtn.addEventListener('click', function() {
      var sido = document.getElementById('sidoButton').textContent.trim();
      var sigungu = document.getElementById('sigunguButton').textContent.trim();
      currentPage = 1;
      loadFundings({ sido: sido, sigungu: sigungu });
    });
  }
  var moreButton = document.getElementById('moreButton');
  if (moreButton) {
    moreButton.addEventListener('click', function() {
      currentPage++;
      loadFundings();
    });
  }
});
// 전역 등록
window.loadFundings = loadFundings;
window.currentPage = currentPage;