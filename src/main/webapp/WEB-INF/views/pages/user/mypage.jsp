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
	<%--<%@ include file="/WEB-INF/views/layout/main-header.jsp"%> --%>
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
				<c:forEach var="order" items="${orderList}">
					<div class="payment-item">

						<!-- 왼쪽 : 결제날짜, 이미지 -->
						<div class="payment-left">
							<div class="payment-date">${order.purchasedAt}</div>
							<div class="payment-image">
								<img src="" alt="상점 이미지">
							</div>
						</div>

						<!-- 오른쪽 : 메뉴, 수량, 결제, 금액, 상세보기 -->
						<div class="payment-right">
							<div class="top-row">
								<div class="menu-name">${order.productName}</div>
								<div class="payment-amount">${order.amount}</div>
								<div class="payment-status">${order.status}</div>
								<div class="payment-detail-btn" data-orderid="${order.orderId}">
									<span>결제상세</span>
								</div>

								<div class="payment-qty">수량 : ${order.qty}</div>
							</div>
						</div>
					</div>

					<!-- 모달 -->
					<div id="modal" class="modal" style="display: none;">
						<div class="modal-content">
							<span class="close-btn">&times;</span>
							<h2>결제 상세 정보</h2>
							<hr>
							<div class="modal-info">
								<p>
									펀딩명: <span id="modal-fundingName"></span>
								</p>
								<p>
									수량: <span id="modal-qty"></span>
								</p>
								<p>
									결제날짜: <span id="modal-purchasedAt"></span>
								</p>
								<p>
									결제수단: <span id="modal-paymentMethod"></span>
								</p>
								<p>
									결제상태: <span id="modal-status"></span>
								</p>
								<p>
									펀딩 성공 여부: <span id="modal-success"></span>
								</p>
							</div>
							<div class="modal-buttons">
								<button class="modal-btn cancel">취소하기</button>
								<button class="modal-btn confirm">확인</button>
							</div>
						</div>
					</div>
				</c:forEach>

			</div>
		</section>
		<%--<%@ include file="/WEB-INF/views/layout/footer.jsp"%> --%>
	</div>

	<!-- 결제상세보기  모달 -->
	<script>
	
	function formatDate(timestamp) {
		  const date = new Date(parseInt(timestamp)); 
		  return date.toLocaleDateString('ko-KR') + ' ' + date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
		}
	 
	    document.querySelectorAll('.payment-detail-btn').forEach(btn => {
	    btn.addEventListener('click', () => {
	      const orderId = btn.getAttribute('data-orderid');
	      
	      
	      fetch(`/order/detail?orderId=\${orderId}`)
	        .then(response => response.json())
	        .then(data => {
	          // 모달 내용 세팅
	          document.getElementById('modal-fundingName').textContent = data.fundingName;
	          document.getElementById('modal-qty').textContent = data.qty;
	          document.getElementById('modal-purchasedAt').textContent = formatDate(data.purchasedAt);
	          document.getElementById('modal-paymentMethod').textContent = data.paymentMethod;
	          document.getElementById('modal-status').textContent = data.status;

	          // 모달 보이기
	          document.getElementById('modal').style.display = 'block';
	        });
	    });
	  });
	    
	    
	    
	    
	    
	    
	    
	    
	    
	 document.querySelectorAll('.close-btn').forEach(btn => {
		 btn.addEventListener('click', () => {
			    document.getElementById('modal').style.display = 'none';
			  });
	 });
	 document.querySelectorAll('.modal-btn.confirm').forEach(btn => {
		 btn.addEventListener('click', () => {
			    document.getElementById('modal').style.display = 'none';
			  });
	 });
	 document.querySelectorAll('.modal-btn.cancel').forEach(btn => {
		 btn.addEventListener('click', () => {
			    document.getElementById('modal').style.display = 'none';
			    alert("결제가 취소되었습니다.");
			  });
	 });
	 
	
		</script>
</body>
</html>
