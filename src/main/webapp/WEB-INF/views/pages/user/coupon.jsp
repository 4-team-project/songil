<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<meta charset="UTF-8">
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 빈 파비콘 (브라우저 요청 방지) -->
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/my_coupon_page.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/review-form.css" />

<div class="coupon-container">
<!-- 사이드 메뉴 -->
<div class="sidebar">
	<div class="menu-title active">사용 가능한 쿠폰</div>
	<div class="menu-sub">사용한 쿠폰</div>
</div>
	<!-- 본문 -->
	<div class="main-content">
		<!-- 검색 -->
		
		<!-- 탭 -->
		<div class="tab-search-bar">
    <div id="tabs" class="tabs"></div>
    <div class="search-wrapper">
        <%@ include file="/WEB-INF/views/common/searchBox.jsp"%>
    </div>
</div>
		<!-- 쿠폰 리스트 -->
		<div class="coupon-list">
			<c:forEach var="coupon" items="${coupons}">
				<c:set var="funding" value="${fundingMap[coupon.fundingId]}" />
				<c:set var="product" value="${productMap[funding.productId]}" />
				<c:set var="store" value="${storeMap[funding.storeId]}" />
				<c:set var="price" value="${product.price}" />
				<c:set var="sale" value="${funding.salePrice}" />
				<c:set var="discountRate" value="${(price - sale) * 100 / price}" />
				<c:set var="discountRateInt"
					value="${discountRate - (discountRate % 1)}" />

				<div class="coupon-card" data-status="${coupon.useStatus}"
					data-expired-at="<fmt:formatDate value='${coupon.expiredAt}' pattern='yyyy-MM-dd'/>"
					data-reviewed="${coupon.reviewed}">
					<div class="coupon-left">
						<c:choose>
							<c:when test="${coupon.useStatus == '미사용'}">
								<div class="sale">
									
									<fmt:formatNumber value="${discountRateInt}" type="number"
										maxFractionDigits="0" />
									% 
								</div>
							</c:when>
							<c:when test="${coupon.useStatus == '사용'}">
								<div class="usedAt">
									<br>
									<fmt:formatDate value='${coupon.usedAt}' pattern='yyyy-MM-dd' />
									<br>
									<div class="use">사용</div>
									<br>
								</div>
							</c:when>
						</c:choose>
					</div>

					<div class="coupon-middle">
						<div class="store">${store.sido}${store.sigungu}
							${store.dong}</div>
						<div class="title">
							<strong class="coupon-title">${store.storeName}</strong>
						</div>
						<div class="name">
							<strong>${funding.fundingName}</strong>
						</div>
						<div class="desc">${funding.fundingDesc}</div>
					</div>
					<div class="coupon-right">
						<c:choose>
							<c:when
								test="${coupon.useStatus == '사용' and coupon.reviewed == 1}">
								<button class="use-btn used-btn"><span class="btn-word">사용완료</span></button>
							</c:when>
							<c:when test="${coupon.useStatus == '사용'}">
								<button class="use-btn used-btn"><span class="btn-word">사용완료</span></button>
								<button type="button" class="review-btn"
									onclick="openReviewModal('${cpath}/review/write/${coupon.couponId}')"><span class="btn-word">리뷰쓰기</span></button>
							</c:when>
							<c:when test="${coupon.useStatus == '미사용'}">
								<form action="${cpath}/user/coupon/detail" method="post"
									style="display: inline;">
									<input type="hidden" name="couponId" value="${coupon.couponId}" />
									<button type="submit" class="use-btn unused-btn"><span class="btn-word">사용하기</span></button>
								</form>
								<div class="coupon-date">
									~
									<fmt:formatDate value="${coupon.expiredAt}"
										pattern="yyyy.MM.dd" />
								</div>
							</c:when>
						</c:choose>
					</div>
				</div>
			</c:forEach>
		</div>

		<!-- 페이지네이션 -->
		<div class="pagination"></div>
	</div>

</div>
<script>
	
	let currentPage = 1;
	const itemsPerPage = 5;

	let originalOrder = [];
	let currentStatus = '미사용'; // '미사용' or '사용'
	let currentTab = 'all';
	let currentFilteredCards = [];
	let currentSearchQuery = '';
	

	window.onload = () => {
	    init();
	};

	function init() {
	    originalOrder = Array.from(document.querySelectorAll('.coupon-card'));
	    renderTabs(currentStatus);
	    registerSidebarEvents();
	    registerTabEvents();
	    registerSearchEvent();

	    // 초기 탭 선택 및 렌더링
	    setActiveTab('all');
	}

	function renderTabs(status) {
	    const tabsContainer = document.getElementById('tabs');
	    
	    if (status === '미사용') {
	        tabsContainer.innerHTML = `
	            <div class="tab" data-tab="all">모든 쿠폰</div>
	            <div class="tab" data-tab="urgent">마감임박</div>
	        `;
	    } else {
	        tabsContainer.innerHTML = `
	            <div class="tab" data-tab="used-all">모든 쿠폰</div>
	            <div class="tab" data-tab="reviewed">리뷰 쓴 쿠폰</div>
	            <div class="tab" data-tab="not-reviewed">리뷰 안 쓴 쿠폰</div>
	        `;
	    }
	}

	function registerSidebarEvents() {
	    const menuTitle = document.querySelector('.menu-title');
	    const menuSub = document.querySelector('.menu-sub');
	    const input = document.querySelector('.search-text');

	    menuTitle.addEventListener('click', () => {
	        if (currentStatus !== '미사용') {
	            currentStatus = '미사용';
	            updateSidebarActive();
	            renderTabs(currentStatus);
	            currentSearchQuery = '';
	            if(input) input.value = '';
	            setActiveTab('all');
	        }
	    });

	    menuSub.addEventListener('click', () => {
	        if (currentStatus !== '사용') {
	            currentStatus = '사용';
	            updateSidebarActive();
	            renderTabs(currentStatus);
	            currentSearchQuery = '';
	            if(input) input.value = '';
	            setActiveTab('used-all');
	        }
	    });
	}

	function updateSidebarActive() {
	    const menuTitle = document.querySelector('.menu-title');
	    const menuSub = document.querySelector('.menu-sub');

	    // 텍스트는 고정
	    menuTitle.textContent = "사용 가능한 쿠폰";
	    menuSub.textContent = "사용한 쿠폰";

	    // 스타일은 상태에 따라 변경
	    if (currentStatus === '미사용') {
	        menuTitle.classList.add('active');
	        menuSub.classList.remove('active');
	    } else {
	        menuTitle.classList.remove('active');
	        menuSub.classList.add('active');
	    }
	}

	function registerTabEvents() {
	    const tabsContainer = document.getElementById('tabs');

	    tabsContainer.addEventListener('click', e => {
	        if (!e.target.classList.contains('tab')) return;

	        setActiveTab(e.target.dataset.tab);
	    });
	}

	function setActiveTab(tab) {
	    currentTab = tab;
	    currentPage = 1;

	    const tabs = document.querySelectorAll('#tabs .tab');
	    tabs.forEach(t => t.classList.toggle('active', t.dataset.tab === tab));

	    let cards = getFilteredCards(tab, currentStatus);

	    if (currentSearchQuery) {
	        cards = cards.filter(card => {
	            const title = card.querySelector('.coupon-title')?.textContent.toLowerCase() || '';
	            const desc = card.querySelector('.coupon-desc')?.textContent.toLowerCase() || '';
	            return title.includes(currentSearchQuery) || desc.includes(currentSearchQuery);
	        });
	    }

	    currentFilteredCards = cards;
	    renderCoupons(currentFilteredCards);
	}

	function getFilteredCards(tabType, status) {
	    switch(tabType) {
	        case 'all':
	            if (status === '미사용') {
	                return sortByCreatedDate(filterCoupons('미사용'));  // ✅ createdAt 기준 정렬
	            }
	            return filterCoupons(status); // fallback
	        case 'urgent':
	            return sortByExpiredDate(filterCoupons('미사용'));
	        case 'used-all':
	        	 return sortByReviewedAndUsedDate(filterCoupons('사용'));
	        case 'reviewed':
	            return filterCoupons('사용', '1');
	        case 'not-reviewed':
	            return filterCoupons('사용', '0');
	        default:
	            return [];
	    }
	}
	
	function sortByCreatedDate(cards) {
	    return cards.slice().sort((a, b) => {
	        const dateA = new Date(a.querySelector('.coupon-date')?.textContent?.split('~')[0].trim());
	        const dateB = new Date(b.querySelector('.coupon-date')?.textContent?.split('~')[0].trim());
	        return dateB - dateA;  // 최신순
	    });
	}
	
	function sortByUsedDate(cards) {
	    return cards.slice().sort((a, b) => {
	        const usedA = new Date(a.querySelector('.usedAt')?.textContent?.trim());
	        const usedB = new Date(b.querySelector('.usedAt')?.textContent?.trim());
	        return usedB - usedA; // 최신순
	    });
	}
	
	function sortByReviewedAndUsedDate(cards) {
	    return cards.slice().sort((a, b) => {
	        const reviewedA = a.dataset.reviewed;
	        const reviewedB = b.dataset.reviewed;

	        // reviewed가 0인 항목이 우선
	        if (reviewedA !== reviewedB) {
	            return reviewedA === "0" ? -1 : 1;
	        }

	        // 둘 다 reviewed 같으면 usedAt 최신순 정렬
	        const usedATextA = a.querySelector('.usedAt')?.textContent?.trim();
	        const usedATextB = b.querySelector('.usedAt')?.textContent?.trim();

	        const dateA = usedATextA ? new Date(usedATextA) : new Date(0);
	        const dateB = usedATextB ? new Date(usedATextB) : new Date(0);

	        return dateB - dateA;
	    });
	}

	function filterCoupons(status, reviewed = null) {
	    return originalOrder.filter(card => {
	        const matchesStatus = card.dataset.status === status;
	        const matchesReviewed = reviewed === null || card.dataset.reviewed === reviewed;
	        return matchesStatus && matchesReviewed;
	    });
	}

	function sortByExpiredDate(cards) {
	    return cards.slice().sort((a, b) => new Date(a.dataset.expiredAt) - new Date(b.dataset.expiredAt));
	}

	function renderCoupons(cards) {
	    const container = document.querySelector('.coupon-list');
	    container.innerHTML = '';

	    const startIndex = (currentPage - 1) * itemsPerPage;
	    const paginated = cards.slice(startIndex, startIndex + itemsPerPage);

	    if (paginated.length === 0) {
	        container.innerHTML = `<div class="no-result-message">쿠폰이 없습니다.</div>`;
	    } else {
	        paginated.forEach(card => container.appendChild(card));
	    }

	    renderPagination();
	}

	function renderPagination() {
	    const pagination = document.querySelector('.pagination');
	    const totalItems = currentFilteredCards.length;
	    const totalPages = Math.ceil(totalItems / itemsPerPage);

	    pagination.replaceChildren();

	    if (totalPages <= 1) return;

	    let html = '';

	    if (currentPage > 1) {
	        html += `<span class="page nav" data-action="prev">&laquo;</span>`;
	    }

	    for (let i = 1; i <= totalPages; i++) {
	    	  html += '<span class="page ' + (i === currentPage ? 'current' : '') +
	    	          '" data-page="' + i + '">' + i + '</span>';
	    	}

	    if (currentPage < totalPages) {
	        html += `<span class="page nav" data-action="next">&raquo;</span>`;
	    }

	    pagination.innerHTML = html;

	    pagination.querySelectorAll('.page').forEach(btn => {
	        btn.addEventListener('click', () => {
	            let requestedPage;

	            if (btn.dataset.page) {
	                requestedPage = parseInt(btn.dataset.page, 10);
	            } else if (btn.dataset.action === 'prev') {
	                requestedPage = currentPage - 1;
	            } else if (btn.dataset.action === 'next') {
	                requestedPage = currentPage + 1;
	            }

	            if (
	                isNaN(requestedPage) ||
	                requestedPage < 1 ||
	                requestedPage > totalPages ||
	                requestedPage === currentPage
	            ) return;

	            currentPage = requestedPage;
	            renderCoupons(currentFilteredCards);
	        });
	    });
	}

	function registerSearchEvent() {
	    const input = document.querySelector('.search-text');
	    const button = document.querySelector('.search-button');

	    function doSearch() {
	        const input = document.querySelector('.search-text');
	        const query = input.value.trim().toLowerCase();
	        currentSearchQuery = query; // 검색어 상태 저장

	        if (!query) {
	            // 검색어 없으면 기본 탭 결과로 복구
	            setActiveTab(currentTab);
	            return;
	        }

	        const filteredByTab = getFilteredCards(currentTab, currentStatus);

	        currentFilteredCards = filteredByTab.filter(card => {
	            const title = card.querySelector('.coupon-title')?.textContent.toLowerCase() || '';
	            const desc = card.querySelector('.coupon-desc')?.textContent.toLowerCase() || '';
	            return title.includes(query) || desc.includes(query);
	        });

	        currentPage = 1;
	        renderCoupons(currentFilteredCards);

	        if (currentFilteredCards.length === 0) {
	            document.querySelector('.coupon-list').innerHTML = `<div class="no-result-message">검색 결과가 없습니다.</div>`;
	        }
	    }


	    button.addEventListener('click', doSearch);
	    input.addEventListener('keydown', e => {
	        if (e.key === 'Enter') {
	            doSearch();
	        }
	    });
	}
	
	function openReviewModal(url) {
	    fetch(url)
	        .then(res => {
	            if (!res.ok) {
	                throw new Error('리뷰 폼 로드 실패: ' + res.status);
	            }
	            return res.text();
	        })
	        .then(html => {
	            const container = document.getElementById('reviewModalContainer');
	            container.innerHTML = html;
	            container.style.display = 'flex';
	            document.body.classList.add('modal-open');

	            // 모달 HTML이 DOM에 추가된 후, 스크립트를 초기화합니다.
	            initializeStarRating(); // 별점 기능 초기화
	            setupReviewFormSubmission(); // 폼 제출 이벤트 리스너 연결

	            // 닫기 버튼 이벤트 리스너 재연결 (필요한 경우)
	            // review-form-modal.jsp의 버튼에 onclick 속성이 있으므로 직접 재연결할 필요는 없지만,
	            // 만약 동적으로 생성되는 버튼이라면 addEventListener로 연결해야 합니다.
	            // 현재는 onclick이 있으므로 괜찮을 수 있습니다.
	        })
	        .catch(error => {
	            console.error("Error loading review modal:", error);
	            alert("리뷰 폼을 불러오는 데 실패했습니다.");
	        });
	}

    function closeModal() {
        const container = document.getElementById('reviewModalContainer');
        container.innerHTML = ''; // 모달 내용 비우기
        container.style.display = 'none'; // 모달 숨기기
        document.body.classList.remove('modal-open'); // 스크롤 잠금 해제
    }

    const imageLimit = 5;
    const imageMap = new Map();
    function generateUUID() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
        const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      });
    }
    function initializeStarRating() {
        const stars = document.querySelectorAll('#reviewModalContainer .star-rating span');
        const ratingInput = document.getElementById('rating');
        let selectedValue = 0; // 초기화마다 초기값 설정

        stars.forEach(star => {
            const value = parseInt(star.getAttribute('data-value'), 10);
            star.addEventListener('click', () => {
                selectedValue = value;
                ratingInput.value = selectedValue;
                updateStars(selectedValue);
            });
            star.addEventListener('mouseover', () => updateStars(value));
            star.addEventListener('mouseout', () => updateStars(selectedValue));
        });

        function updateStars(value) {
            stars.forEach(star => {
                const starValue = parseInt(star.getAttribute('data-value'), 10);
                star.classList.toggle('selected', starValue <= value);
            });
        }
    }


    function handleFiles(files) {
        const previewContainer = document.getElementById('preview-container');
        if (imageMap.size + files.length > imageLimit) {
            alert("이미지는 최대 " + imageLimit + "장까지 업로드가 가능합니다.");
            return;
        }
        [...files].forEach(file => {
            if (!file.type.startsWith("image/") || file.size > 5 * 1024 * 1024) {
                alert("유효하지 않은 이미지입니다.");
                return;
            }
            const uuid = generateUUID();
            imageMap.set(uuid, file);
            const reader = new FileReader();
            reader.onload = e => {
                const img = document.createElement('img');
                img.src = e.target.result;
                const btn = document.createElement('button');
                btn.textContent = '×';
                btn.type = 'button';
                btn.onclick = () => removeImage(uuid, btn);
                const wrapper = document.createElement('div');
                wrapper.className = 'preview-image';
                wrapper.appendChild(img);
                wrapper.appendChild(btn);
                previewContainer.appendChild(wrapper);
            };
            reader.readAsDataURL(file);
        });
        document.getElementById('images').value = '';
    }

    function removeImage(uuid, btn) {
        imageMap.delete(uuid);
        btn.parentElement.remove();
    }

    function openConfirmModal() {
        document.getElementById('confirmModal').style.display = 'flex';
    }

    function closeConfirmModal() {
        document.getElementById('confirmModal').style.display = 'none';
    }

    function exitReview() {
        closeConfirmModal();
        // 리뷰 작성을 취소하고 전체 모달 닫기
        closeModal(); // my_coupon_page.jsp의 closeModal 호출
    }

    // 리뷰 폼 제출 이벤트 리스너도 모달 로드 후에 동적으로 연결해야 합니다.
    // 이를 위한 함수를 만들고 openReviewModal에서 호출합니다.
    function setupReviewFormSubmission() {
        const reviewForm = document.getElementById("reviewForm");
        if (reviewForm) {
            reviewForm.addEventListener("submit", async function (e) {
                e.preventDefault();
                const form = e.target;
                let imageUrls = [];

                for (const file of imageMap.values()) {
                    const formData = new FormData();
                    formData.append("file", file);
                    const response = await fetch(cpath + "/image/upload", {
                        method: "POST",
                        body: formData
                    });
                    if (!response.ok) {
                        alert("이미지 업로드 실패: " + response.statusText);
                        return;
                    }
                    const url = await response.text();
                    imageUrls.push(url);
                }

                const json = {
                    userId: parseInt(form.userId.value),
                    productId: parseInt(form.productId.value),
                    rating: parseInt(form.rating.value),
                    content: form.content.value,
                    imageUrls: imageUrls
                };

                const result = await fetch(cpath + "/review/submit", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(json)
                });

                if (result.ok) {
                    document.getElementById('successModal').style.display = 'flex';
                    form.reset();
                    if (document.getElementById('rating')) {
                         document.getElementById('rating').value = 0;
                         document.querySelectorAll('#reviewModalContainer .star-rating span').forEach(star => star.classList.remove('selected'));
                    }

                    imageMap.clear();
                    document.getElementById('preview-container').innerHTML = '';
                } else {
                    const errorText = await result.text();
                    alert("리뷰 등록 실패: " + errorText);
                }
            });
        }
    }


    function closeSuccessModal() {
        document.getElementById('successModal').style.display = 'none';
        closeModal(); // 성공 모달 닫을 때 최상위 모달도 닫기
    }

    function goToReviewPage() {
        window.location.href = cpath + "/user/reviews";
        closeModal(); // 페이지 이동 후에도 모달 닫기
    }

    
    </script>
<div id="reviewModalContainer" class="modal-overlay"
	style="display: none;"></div>
