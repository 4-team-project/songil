<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 쿠폰 목록</title>
<!-- 빈 파비콘 (브라우저 요청 방지) -->
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/my_coupon_page.css" />
<c:set var="cpath" value="${pageContext.request.contextPath}" />
</head>

<body>
	<div class="coupon-container">
		<!-- 사이드 메뉴 -->
		<div class="sidebar">
			<div class="menu-title active">사용
				가능한 쿠폰</div>
			<div class="menu-sub">사용한 쿠폰</div>
		</div>
		<!-- 본문 -->
		<div class="main-content">
			<!-- 검색 -->
			<div class="search-wrapper"
				style="display: flex; justify-content: flex-end;">
				<%@ include file="/WEB-INF/views/common/searchBox.jsp"%>
			</div>
			<!-- 탭 -->
			<div id="tabs" class="tabs"></div>

			<!-- 쿠폰 리스트 -->
			<div class="coupon-list">
				<c:forEach var="coupon" items="${coupons}">
					<c:set var="funding" value="${fundingMap[coupon.fundingId]}" />
					<c:set var="product" value="${productMap[funding.productId]}" />

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
										<br>
										<fmt:formatNumber value="${discountRateInt}" type="number"
											maxFractionDigits="0" />
										% <br> <br>
									</div>
								</c:when>
								<c:when test="${coupon.useStatus == '사용'}">
									<div class="usedAt">
										<br>
										<fmt:formatDate value='${coupon.usedAt}' pattern='yyyy-MM-dd' />
										<br> 사용 <br> <br>
									</div>
								</c:when>
							</c:choose>
						</div>

						<div class="coupon-middle">
							<div class="store">${funding.storeId}</div>
							<div class="title">
								<strong class="coupon-title">${funding.fundingName}</strong>
							</div>
							<div class="desc coupon-desc">${funding.fundingDesc}</div>
							<div class="desc">${price},${sale}</div>
						</div>

						<div class="coupon-right">
							<c:choose>
								<c:when
									test="${coupon.useStatus == '사용' and coupon.reviewed == 1}">
									<button class="use-btn">사용완료</button>
								</c:when>
								<c:when test="${coupon.useStatus == '사용'}">
									<button class="use-btn">사용완료</button>
									<form action="${cpath}/review/write/${coupon.couponId}" method="get"
										style="display: inline;">
									<button type="submit" class="review-btn">리뷰쓰기</button>
									</form>
								</c:when>
								<c:when test="${coupon.useStatus == '미사용'}">
									<form action="${cpath}/coupon/user/detail" method="post"
										style="display: inline;">
										<input type="hidden" name="couponId"
											value="${coupon.couponId}" /> <input type="hidden"
											name="discountRate"
											value="${discountRate - (discountRate % 1)}" />
										<button type="submit" class="use-btn">사용하기</button>
									</form>
									<div class="coupon-date">
										<fmt:formatDate value="${coupon.createdAt}"
											pattern="yyyy.MM.dd" />
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

	    menuTitle.addEventListener('click', () => {
	        if (currentStatus !== '미사용') {
	            currentStatus = '미사용';
	            updateSidebarActive();
	            renderTabs(currentStatus);
	            setActiveTab('all');
	        }
	    });

	    menuSub.addEventListener('click', () => {
	        if (currentStatus !== '사용') {
	            currentStatus = '사용';
	            updateSidebarActive();
	            renderTabs(currentStatus);
	            setActiveTab('used-all');
	        }
	    });
	}

	function updateSidebarActive() {
	    const menuTitle = document.querySelector('.menu-title');
	    const menuSub = document.querySelector('.menu-sub');

	    if (currentStatus === '미사용') {
	        menuTitle.classList.add('active');
	        menuTitle.textContent = "사용 가능한 쿠폰";
	        menuSub.classList.remove('active');
	        menuSub.textContent = "사용한 쿠폰";
	    } else {
	    	  menuTitle.classList.remove('active');
	          menuSub.classList.add('active');
	          menuTitle.textContent = "사용한 쿠폰";
	          menuSub.textContent = "사용 가능한 쿠폰";
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

	    const cards = getFilteredCards(tab, currentStatus);
	    renderCoupons(cards);
	}

	function getFilteredCards(tabType, status) {
	    switch(tabType) {
	        case 'all':
	            return filterCoupons(status);
	        case 'urgent':
	            return sortByExpiredDate(filterCoupons('미사용'));
	        case 'used-all':
	            return filterCoupons('사용');
	        case 'reviewed':
	            return filterCoupons('사용', '1');
	        case 'not-reviewed':
	            return filterCoupons('사용', '0');
	        default:
	            return [];
	    }
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

	    renderPagination(cards.length);
	}

	function renderPagination(totalItems) {
	    const pagination = document.querySelector('.pagination');
	    const totalPages = Math.ceil(totalItems / itemsPerPage);

	    pagination.replaceChildren(); // 기존 요소 완전 정리

	    if (totalPages <= 1) {
	        return;
	    }

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

	    // 이벤트 재등록
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

            console.log("클릭됨", currentPage, requestedPage, btn.dataset.page, btn.dataset.action);

            const totalPages = Math.ceil(totalItems / itemsPerPage);
            if (
                isNaN(requestedPage) ||
                requestedPage < 1 ||
                requestedPage > totalPages ||
                requestedPage === currentPage
            ) return;

            currentPage = requestedPage;
            const cards = getFilteredCards(currentTab, currentStatus);
            renderCoupons(cards);
	        });
	    });
	}

	function registerSearchEvent() {
	    const input = document.querySelector('.search-text');
	    const button = document.querySelector('.search-button');

	    function doSearch() {
	        const query = input.value.trim().toLowerCase();
	        if (!query) {
	            // 검색어가 없으면 기본 탭 내용 재출력
	            setActiveTab(currentTab);
	            input.value = '';
	            return;
	        }

	        const filteredByTab = getFilteredCards(currentTab, currentStatus);

	        // 검색어 포함하는 카드 필터링 (title or desc)
	        const searched = filteredByTab.filter(card => {
	            const title = card.querySelector('.coupon-title')?.textContent.toLowerCase() || '';
	            const desc = card.querySelector('.coupon-desc')?.textContent.toLowerCase() || '';
	            return title.includes(query) || desc.includes(query);
	        });

	        currentPage = 1;
	        renderCoupons(searched);

	        if (searched.length === 0) {
	            document.querySelector('.coupon-list').innerHTML = `
	                <div class="no-result-message">검색 결과가 없습니다.</div>
	            `;
	        }

	        input.value = '';
	    }

	    button.addEventListener('click', doSearch);
	    input.addEventListener('keydown', e => {
	        if (e.key === 'Enter') {
	            doSearch();
	        }
	    });
	}

	
</script>
</body>
</html>