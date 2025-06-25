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
				</div>
			</nav>
		</aside>


		<!-- 위에-->
		<section class="content-area">
			<nav class="tab-search-container">
				<div class="tab-wrapper">
					<ul class="tab-menu">
						<li><a href="#" class="allbuylist active"
							data-status="allbuylist">모든 구매 내역</a></li>
						<li><a href="#" class="complete" data-status="complete">결제
								완료</a></li>
						<li><a href="#" class="cancel" data-status="cancel">결제 취소</a></li>
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
			<div id="order-list-container" class="content">
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
				</c:forEach>
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
						<button type="button" class="modal-btn cancel">취소하기</button>
						<button class="modal-btn confirm">확인</button>
					</div>
				</div>
			</div>
		</section>
	</div>





















	<!-- 결제상세보기  모달 -->
	<script>
	// 모달 보여주는 함수
	
	let currentTabStatus = 'allbuylist';
	
	  function formatDate(timestamp) {
	    const date = new Date(parseInt(timestamp));
	    return date.toLocaleDateString('ko-KR') + ' ' + date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
	  }

	  function showModal(data) {
	    document.getElementById('modal-fundingName').textContent = data.fundingName;
	    document.getElementById('modal-qty').textContent = data.qty;
	    document.getElementById('modal-purchasedAt').textContent = formatDate(data.purchasedAt);
	    document.getElementById('modal-paymentMethod').textContent = data.paymentMethod;
	    document.getElementById('modal-status').textContent = data.status;
	    document.getElementById('modal-success').textContent = data.success || '';

	    const cancelBtn = document.querySelector('.modal-btn.cancel');

	    // 모든 내역 보기일 땐 status가 'allbuylist'일 경우, 취소하기 버튼 보이게
	    if (currentTabStatus === 'allbuylist') {
	      cancelBtn.style.display = 'inline-block';
	    } else { // 결제완료나 환불 상태일 땐 숨기기
	      cancelBtn.style.display = 'none';
	    }
	    
	    document.getElementById('modal').style.display = 'block';
	  }

	  // 모달 이벤트 및 결제상세 버튼 이벤트 바인딩 함수
	  function bindModalEvents() {
	    document.querySelectorAll('.payment-detail-btn').forEach(btn => {
	      btn.onclick = () => {
	        const orderId = btn.getAttribute('data-orderid');
	        fetch(`${contextPath}/order/detail?orderId=\${orderId}`)
	          .then(response => response.json())
	          .then(data => {
	            showModal(data);
	          });
	      };
	    });

	    document.querySelectorAll('.close-btn').forEach(btn => {
	      btn.onclick = () => {
	        document.getElementById('modal').style.display = 'none';
	      };
	    });

	    document.querySelectorAll('.modal-btn.confirm').forEach(btn => {
	      btn.onclick = () => {
	        document.getElementById('modal').style.display = 'none';
	      };
	    });

	    document.querySelectorAll('.modal-btn.cancel').forEach(btn => {
	    	  btn.onclick = function(event) {
	    	    event.preventDefault();
	    	    event.stopPropagation();

	    	    document.getElementById('modal').style.display = 'none';
	    	    alert("결제가 취소되었습니다.");
	      };
	    });
	  }

	  // 초기 페이지 로드 시 모달 이벤트 등록
	  window.addEventListener('DOMContentLoaded', () => {
	    bindModalEvents();
	  });

	  // 탭 클릭 시 AJAX 호출 및 모달 이벤트 재등록
	  document.querySelectorAll('.allbuylist, .complete, .cancel').forEach(tab => {
	    tab.addEventListener('click', e => {
	      e.preventDefault();

	      document.querySelectorAll('.allbuylist, .complete, .cancel').forEach(t => t.classList.remove('active'));
	      tab.classList.add('active');

	      const status = tab.getAttribute('data-status');
	     
	      currentTabStatus = status;

	      fetch(`${contextPath}/order/list?status=\${status}`)
	        .then(response => {
	          if (!response.ok) throw new Error('서버 응답 에러: ' + response.status);
	          return response.text();
	        })
	        .then(html => {
	          document.getElementById('order-list-container').innerHTML = html;
	          bindModalEvents(); // ★AJAX 후 이벤트 재등록 필수★
	        })
	        .catch(err => {
	          alert('데이터를 불러오는 중 오류가 발생했습니다.');
	          //console.error(err);
	        });
	    });
	  });
</script>
</body>
</html>