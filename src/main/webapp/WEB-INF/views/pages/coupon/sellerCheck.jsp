<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h2>쿠폰 정보 확인</h2>

<c:if test="${not empty coupon}">
	<p>
		<strong>쿠폰 코드:</strong> ${coupon.couponCode}
	</p>
	<p>
		<strong>상태:</strong> ${coupon.useStatus}
	</p>
	<p>
		<strong>발급일:</strong>
		<fmt:formatDate value="${coupon.createdAt}" pattern="yyyy-MM-dd" />
	</p>
	<p>
		<strong>만료일:</strong>
		<fmt:formatDate value="${coupon.expiredAt}" pattern="yyyy-MM-dd" />
	</p>

	<hr />

	<h3>관련 정보</h3>
	<c:if test="${not empty store}">
		<p>
			<strong>가게 이름:</strong> ${store.storeName}
		</p>
	</c:if>

	<c:if test="${not empty funding}">
		<p>
			<strong>펀딩 제목:</strong> ${funding.fundingName}
		</p>
	</c:if>

	<c:if test="${not empty product}">
		<p>
			<strong>메뉴 이름:</strong> ${product.productName}
		</p>
	</c:if>

	<hr />

	<c:if test="${coupon.useStatus eq '미사용'}">
		<form action="/coupon/${coupon.couponCode}/use" method="post">
			<button type="submit">쿠폰 사용 처리</button>
		</form>
	</c:if>

	<c:if test="${coupon.useStatus eq '사용'}">
		<p style="color: red;">이미 사용된 쿠폰입니다.</p>
	</c:if>
</c:if>

<c:if test="${empty coupon}">
	<p>쿠폰 정보를 찾을 수 없습니다.</p>
</c:if>
