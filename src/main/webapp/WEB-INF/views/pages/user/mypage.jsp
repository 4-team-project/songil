<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Mypage</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/common/style.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/common/mypage.css">

</head>
<body>
	<div class="mypage-container">

		<!-- 사이드바 -->
		<aside class="sidebar">
			<div class="profile-section">
				<div class="profile-image"></div>
				<div class="username">닉네임</div>
				<a href="#" class="editMypage">내 정보 수정하기</a>
			</div>

			<nav class="menu">
				<div class="menulist">
					<a href="#" class="buylist">구매 내역</a> <a href="#"
						class="activeFunding">내가참여한펀딩</a> <a href="#" class="logout">로그아웃</a>
	<%@ include file="/WEB-INF/views/layout/main-header.jsp"%>
	<div class="container">

		<div class="mypage-container">
			<aside class="sidebar">
				<div class="profile-section">
					<div class="profile-image"></div>
					<div class="username">닉네임</div>
					<div class="editMypage">내 정보 수정하기</div>
				</div>
			</nav>
		</aside>


		<!-- 위에-->
		<section class="content-area">
			<nav class="tab-search-container">
				<div class="tab-wrapper">
					<ul class="tab-menu">
						<li><a href="#" class="allbuylist active">모든 구매 내역</a></li>
						<li><a href="#" class="complete">결제 완료</a></li>
						<li><a href="#" class="cancel">결제 취소</a></li>
					</ul>
				</div>
				<div class="search-wrapper">
					<%@ include file="/WEB-INF/views/common/searchBox.jsp"%>
				</div>
			</nav>

			<!-- 테이블 헤더 -->
			<div class="table-header">
				<span class="col-title">결제명</span> <span class="col-amount">결제
					금액</span> <span class="col-status">결제 여부</span> <span class="col-detail">결제상세</span>
			</div>


			<!-- 내용 -->
			<div class="content">
				<c:forEach var="order" items="${orderList }">
					<div class="payment-item">

						<!-- 왼쪽 : 결제날짜, 이미지 -->
						<div class="payment-left">
							<div class="payment-date">${order.purchasedAt }</div>
							<div class="payment-image">
								<%--  <img src="${ }" alt="상점 이미지">--%>
							</div>
						</div>

						<!-- 오른쪽 : 메뉴, 수량, 결제, 금액, 상세보기 -->
						<div class="payment-right">
							<div class="top-row">
								<div class="menu-name">메뉴이름</div>
								<div class="payment-amount">${order.amount }</div>
								<div class="payment-status">${order.status }</div>
								<div class="payment-detail-btn" id="detailBtn">
									<span>결제상세</span>
								</div>
								<div id="modal" class="modal" style="display: none;">
									<div class="modal-content">
										<span class="close-btn">&times;</span>
										<h2>결제 상세 정보</h2>
										<div class="modal-info">
											<p><strong>메뉴 이름:</strong> <span id="modal-menu"></span></p>
											<p><strong>수량:</strong> <span id="modal-qty"></span></p>
											<p><strong>결제일:</strong> <span id="modal-date"></span></p>
											<p><strong>결제 금액:</strong> <span id="modal-amount"></span></p>
										</div>
									</div>
								</div>
							</div>
							<div class="payment-qty">수량 : ${order.qty}</div>
						</div>



					</div>
				</c:forEach>
			</div>
		</section>
		<%@ include file="/WEB-INF/views/layout/footer.jsp"%>
	</div>

	<!-- 결제상세보기 > 모달 -->
<script>
document.addEventListener('DOMContentLoaded', () => {
	  const modal = document.getElementById("modal");
	  const closeBtn = document.querySelector(".close-btn");

	  // 테스트용: 버튼 클릭 시 무조건 모달 띄우기
	  document.querySelectorAll(".payment-detail-btn").forEach(btn => {
	    btn.addEventListener("click", () => {
	      // 내용 추가
	     
	      modal.style.display = "block";
	    });
	  });

	  //닫기 버튼
	  closeBtn.addEventListener("click", () => {
	    modal.style.display = "none";
	  });	  
	});

	</script>
</body>
</html>
