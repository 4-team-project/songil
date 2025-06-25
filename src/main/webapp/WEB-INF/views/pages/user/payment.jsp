<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/payment.css">

<script>
	$(function() {
		$('#showDetailBtn').on('click', function() {
			$('#paymentModal').fadeIn();
		});

		$('#closeModal, .modal-btn.confirm').on('click', function() {
			$('#paymentModal').fadeOut();
		});

		$('#paymentModal').on('click', function(e) {
			if (e.target === this) {
				$(this).fadeOut();
			}
		});

		$('.modal-btn.cancel').on('click', function() {
			alert('결제 취소 요청 처리'); // 여기에 실제 결제 취소 로직 연결
		});
	});
</script>
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
		<a id="showDetailBtn" class="btn">결제 상세 보기</a> <a
			href="${cpath}/user/coupon" class="btn">내 쿠폰함</a>

	</div>
	<!-- 모달 구조 -->
	<div id="paymentModal" class="modal" style="display: none;">
		<div class="modal-content">
			<span id="closeModal" class="close">&times;</span>
			<h2 class="modal-title">결제 내역</h2>

			<div class="modal-info">
				<p>
					<strong>펀딩명</strong> <span>${saveOrder.fundingName}</span>
				</p>
				<p>
					<strong>구매 수량</strong> <span>${saveOrder.qty}</span>
				</p>
				<p>
					<strong>결제 날짜</strong> <span>${saveOrder.purchasedAt}</span>
				</p>
				<p>
					<strong>결제 금액</strong> <span>${saveOrder.discountAmount} 원</span>
				</p>
				<p>
					<strong>결제 수단</strong> <span>${saveOrder.paymentMethod}</span>
				</p>
				<p>
					<strong>결제 상태</strong> <span> 결제완료<br> <small
						class="cancel-guide">(펀딩 성공 전까지 취소 가능)</small>
					</span>
				</p>
				<%-- <p>
					<strong>펀딩 성공 여부</strong> <span>${saveOrder.progressPercent}%
						진행 중</span>
				</p> --%>
			</div>

			<div class="modal-buttons">
				<button type="button" class="modal-btn cancel">결제 취소하기</button>
				<button type="button" class="modal-btn confirm">확인</button>
			</div>
		</div>
	</div>
</div>