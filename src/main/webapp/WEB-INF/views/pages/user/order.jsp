<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/order.css">

<div class="order-container">
	<!-- 왼쪽 영역 -->
	<div class="order-left">
		<!-- 펀딩 상품 정보 -->
		<div class="info">
			<h3>펀딩 상품 정보</h3>
			<div class="product-info">
				<div class="product-image">
					<img src="${funding.thumbnailImageUrl}" alt="펀딩 이미지" />
				</div>
				<div class="product-detail">
					<span class="store-name">${store.storeName}</span><br>
					<p class="funding-name">
						${funding.fundingName}
					</p>
					<p>수량 ${quantity}개</p>
					<p class="price">
						<fmt:formatNumber value="${totalPrice}" type="number" />
						원
					</p>
				</div>
			</div>
		</div>

		<!-- 구매자 정보 -->
		<div class="info">
			<h3>구매자 정보</h3>
			<p>${loginUser.userName}</p>
			<p>${loginUser.phone}</p>
			<p>${loginUser.birth}</p>
		</div>
	</div>

	<!-- 오른쪽 영역 -->
	<div class="order-right">
		<div class="info">
			<h3>
				최종 결제 금액 <span class="right-price">
				<fmt:formatNumber value="${totalPrice}" type="number" /> 원</span>
			</h3>
			<p class="small-text">
				펀딩이 정해진 기간 내 100% 달성되면, 쿠폰이 발급되어 사용하실 수 있습니다.<br> 펀딩이 무산되거나 중단될
				경우, 결제 금액은 자동으로 전액 환불됩니다.
			</p>
			<form action="${cpath}/order/payment" method="post">
				<input type="hidden" name="fundingId" value="${funding.fundingId}" />
				<input type="hidden" name="quantity" value="${quantity}" /> 
				<input type="hidden" name="totalPrice" value="${totalPrice}" />
				<button type="submit" class="buy-button">결제하기</button>
			</form>
		</div>
	</div>
</div>