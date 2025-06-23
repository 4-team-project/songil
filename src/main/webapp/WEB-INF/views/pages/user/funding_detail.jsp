<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/fundign_detail.css">

<!-- Swiper 스타일과 JS -->
<link rel="stylesheet"
	href="https://unpkg.com/swiper/swiper-bundle.min.css" />
<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	const swiper = new Swiper('.swiper-container', {
		loop : true,
		pagination : {
			el : '.swiper-pagination',
			clickable : true,
		},
	});

	$(function () {
		const salePrice = parseInt("${funding.salePrice}");
	    const perQty = parseInt("${funding.perQty}");
	    const maxQty = parseInt("${funding.maxQty}");
	    const currentQty = parseInt("${funding.currentQty}");

	    const maxBuyable = Math.min(perQty, maxQty - currentQty);

	    function updateTotal(qty) {
	      const total = salePrice * qty;
	      $("#totalPrice").text(total.toLocaleString());
	    }

	    $(".plus").click(function () {
	      let qty = parseInt($("#quantity").val());
	      if (qty < maxBuyable) {
	        qty++;
	        $("#quantity").val(qty);
	        updateTotal(qty);
	      } else {
	        alert("최대 구매 가능 수량은 " + maxBuyable + "개입니다.");
	      }
	    });

	    $(".minus").click(function () {
	      let qty = parseInt($("#quantity").val());
	      qty = qty > 1 ? qty - 1 : 1;
	      $("#quantity").val(qty);
	      updateTotal(qty);
	    });
	  });
	
	$(function () {
	    $(".tab-btn").click(function () {
	      const tab = $(this).data("tab");

	      // 버튼 스타일 초기화 및 현재 탭 강조
	      $(".tab-btn").removeClass("active-tab");
	      $(this).addClass("active-tab");

	      if (tab === "desc") {
	        // 상세설명 내용 복원
	        const descHtml = `
	          <pre class="funding-desc">${funding.fundingDesc}</pre>
	          <div class="image-placeholder"></div>
	          <div class="hashtags">#남대문맛집 #곰탕추천 #건강한한식 #직장인간식 #한밤늦은히든</div>
	        `;
	        $("#tab-content").html(descHtml);
	      } else if (tab === "review") {
	        // review-content의 내용을 가져와서 붙이기
	        const reviewHtml = $(".review-list").html();
	        $("#tab-content").html(reviewHtml);
	      }
	    });
	  });
</script>
<p class="category">Home / ${store.categoryName}</p>
<div class="product-detail-container">

	<!-- 이미지 슬라이더 -->
	<div class="image-carousel">
		<span class="category"></span>
		<div class="swiper-container">
			<div class="swiper-wrapper">
				<c:forEach var="img" items="${funding.images}">
					<div class="swiper-slide">
						<img src="${img.imageUrl}" alt="펀딩 이미지" />
					</div>
				</c:forEach>
			</div>
			<div class="swiper-pagination"></div>
		</div>
	</div>


	<!-- 상품 정보 -->
	<div class="product-info">
		<a class="store-name">${store.storeName}></a>
		<p class="funding-desc">${funding.fundingName}</p>

		<div class="avg-rating">
			<span style="color: #FF9670;">★</span>
			<fmt:formatNumber value="${avgRating}" type="number"
				maxFractionDigits="1" />
			<span>{${reviewCount}}</span>
		</div>

		<div class="price">
			<c:set var="original" value="${product.price}" />
			<c:set var="sale" value="${funding.salePrice}" />
			<c:set var="discount" value="${(1 - (sale / original)) * 100}" />
			<span class="discount"><fmt:formatNumber value="${discount}"
					type="number" maxFractionDigits="0" />%</span>
			<del class="product-price">
				<fmt:formatNumber value="${original}" type="number" />
				원
			</del>
			<br> <strong class="sale-price"><fmt:formatNumber
					value="${sale}" type="number" />원</strong>
		</div>

		<div>
			<p class="date">
				<span class="label-text">남은 기간</span><br>
				<c:set var="today" value="<%=new java.util.Date()%>" />
				<c:set var="remaining"
					value="${(funding.endDate.time - today.time) / (1000*60*60*24)}" />
				<span class="remaining-day"> <fmt:formatNumber
						value="${remaining}" type="number" maxFractionDigits="0" /> 일
				</span><br> <span class="period">${funding.startDate}~${funding.endDate}</span>
			</p>
		</div>

		<div class="funding-contents">
			<p>달성률</p>
			<c:set var="percent"
				value="${(funding.currentQty * 100.0) / funding.targetQty}" />
			<fmt:formatNumber value="${percent}" type="number"
				maxFractionDigits="0" var="percentInt" />

			<div class="funding-progress-box">
				<span>${percentInt}%</span>
			</div>
			<div class="funding-bar">
				<div class="funding-bar-inner" style="width: ${percentInt}%;"></div>
			</div>
		</div>
		<hr class="divider" />
		<div class="price-row">
			<div class="quantity-control">
				<button class="qty-btn minus">-</button>
				<input type="text" id="quantity" value="1" readonly />
				<button class="qty-btn plus">+</button>
			</div>

			<p class="total-price">
				총 가격 <br> <span class="total-amount"> <span
					id="totalPrice">${funding.salePrice}</span><span class="won">원</span>
				</span>
			</p>
		</div>

		<button class="buy-button">구매하기</button>
	</div>
</div>

<!-- 탭 메뉴 -->
<div class="tab-menu">
	<button class="tab-btn active-tab" data-tab="desc">펀딩 상세설명</button>
	<button class="tab-btn" data-tab="review">리뷰(${reviewCount})</button>
</div>

<!-- 내용이 바뀔 영역 -->
<div id="tab-content">
	<!-- 기본: 펀딩 상세설명 표시 -->
	<pre class="funding-desc">${funding.fundingDesc}</pre>
	<div class="image-placeholder"></div>
	<div class="hashtags">#남대문맛집 #곰탕추천 #건강한한식 #직장인간식 #한밤늦은히든</div>
</div>

<!-- 숨겨진 리뷰 HTML (JSTL 반복문 활용) -->
<div class="review-list" style="display: none;">
	<c:forEach var="review" items="${reviewlist}">
		<div class="review-card">
			<div class="review-body">
				<div class="review-left">
					<div class="review-user">
						<span class="user-icon">👤</span> <strong>${review.name}</strong>
						<span class="review-date">${review.createdAt}</span>
					</div>

					<div class="review-rating">
						<c:forEach begin="1" end="5" var="i">
							<span
								class="star <c:if test='${i <= review.rating}'>filled</c:if>">★</span>
						</c:forEach>
					</div>

					<div class="review-content">${review.content}</div>
				</div>

				<div class="review-image">
					<img src="${review.images}" alt="리뷰 이미지" />
				</div>
			</div>
		</div>
	</c:forEach>
</div>