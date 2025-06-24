<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/payment.css">

<div class="payment-result-container">
	<img src="${cpath}/resources/images/logo.svg" class="logo-img" />

	<c:choose>
		<c:when test="${isSuccess}">
			<div class="success-box">
				<h1 class="success">결제가 완료되었어요!</h1>
				<p class="desc">정해진 기간 내 펀딩이 100% 달성되면, 쿠폰이 자동으로 내 쿠폰함에 지급됩니다.</p>
			</div>
		</c:when>
		<c:otherwise>
			<div class="fail-box">
				<div class="fail-icon">❗</div>
				<h1 class="fail">결제를 실패했어요</h1>
			</div>
			<p class="desc">결제 내역과 결제 수단을 확인 후 재시도 해보시기 바랍니다.</p>
		</c:otherwise>
	</c:choose>

	<div class="btn-group">
		<a href="${cpath}/mypage/order" class="btn">결제 내역 보기</a> <a
			href="${cpath}/user/coupon" class="btn">내 쿠폰함</a>
	</div>
</div>