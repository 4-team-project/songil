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
			<div class="menu-title" onclick="showCoupons('미사용')">사용 가능한 쿠폰</div>
			<div class="menu-sub" onclick="showCoupons('사용')">사용한 쿠폰</div>
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

					<div class="coupon-card" data-status="${coupon.useStatus}">
						<div class="coupon-left">
							<div class="sale">
								<fmt:formatNumber value="${discountRateInt}" type="number"
									maxFractionDigits="0" />
								%
							</div>
						</div>

						<div class="coupon-middle">
							<div class="store">${funding.storeId}</div>
							<div class="title">
								<strong>${funding.fundingName}</strong>
							</div>
							<div class="desc">${funding.fundingDesc}</div>
							<div class="desc">${price},${sale}</div>
						</div>

						<div class="coupon-right">
							<c:choose>
								<c:when test="${coupon.useStatus == '사용'}">
									<button class="use-btn">사용완료</button>
									<button class="review-btn">리뷰쓰기</button>
								</c:when>
								<c:when test="${coupon.useStatus == '미사용'}">
									<form action="${cpath}/user/coupon_detail" method="post"
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
			<div class="pagination">
				<span class="page">&lt;</span> <span class="page current">1</span> <span
					class="page">2</span> <span class="page">3</span> <span
					class="page">&gt;</span>
			</div>
		</div>

	</div>

	<script>
function showCoupons(status) {
    const allCards = document.querySelectorAll('.coupon-card');
    allCards.forEach(card => {
        if (card.dataset.status === status) {
            card.style.display = 'flex';
        } else {
            card.style.display = 'none';
        }
        
        const tabsContainer = document.getElementById('tabs');
        if (status === '미사용') {
          tabsContainer.innerHTML = `
            <div class="tab active">모든 쿠폰</div>
            <div class="tab">마감임박</div>
          `;
        } else if (status === '사용') {
          tabsContainer.innerHTML = `
        	  <div class="tab active">모든 쿠폰</li>
        	  <div class="tab">리뷰 쓴 쿠폰</div>
        	  <div class="tab">리뷰 안 쓴 쿠폰</div>
          `;
        }
        
        $(document).ready(function () {
            $(".tab").click(function () {
              $(".tab").removeClass("active"); // 모든 탭 비활성화
              $(this).addClass("active");      // 클릭한 탭만 활성화
            });
          });

    });

   
}

// 페이지 로딩 시 '미사용' 쿠폰만 보이게
window.onload = () => showCoupons('미사용');


</script>
</body>
</html>
