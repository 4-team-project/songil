<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
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

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/components/regionDropdown.css">

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

			<!-- 구매내역 -->
			<nav class="menu">
				<div class="menulist">
					<a href="#" class="buylist active">구매 내역</a> <a href="#"
						class="activeFunding">내가참여한펀딩</a> <a href="#" class="logout">로그아웃</a>
				</div>
			</nav>
		</aside>

		<!-- 구매내역 > nav-->
		<section class="content-area">
			<nav class="tab-search-container">

				<!-- 구매내역 -->
				<div class="tab-wrapper buylist">
					<ul class="tab-menu">
						<li><a href="#" class="allbuylist active"
							data-status="allbuylist">모든 구매 내역</a></li>
						<li><a href="#" class="complete" data-status="complete">결제
								완료</a></li>
						<li><a href="#" class="cancel" data-status="cancel">결제 취소</a></li>
					</ul>
				</div>

				<!-- 내가 참여한 펀딩 > nav-->
				<div class="tab-wrapper fundinglist" style="display: none">
					<ul class="funding_nav">
						<li><a href="#" class="allfundinglist active"
							data-status="allfundinglist">내가 참여한 <br>모든 펀딩
						</a></li>
						<li><a href="#" class="progressing" data-status="progressing">
								진행 중인 펀딩</a></li>
						<li><a href="#" class="achieved" data-status="achieved">달성된
								펀딩</a></li>
						<li><a href="#" class="failed" data-status="failed">미달성된
								펀딩</a></li>
					</ul>
				</div>

				<!-- 검색하기 -->
				<div class="search-wrapper">
					<%@ include file="/WEB-INF/views/common/searchBox.jsp"%>
				</div>
			</nav>

			<!-- 구매내역 > 헤더 -->
			<div class="table-header buylist-header">
				<span class="col-title">결제명</span> <span class="col-amount">결제
					금액</span> <span class="col-status">결제 여부</span> <span class="col-detail">결제상세</span>
			</div>

			<!-- 내가 참여한 펀딩 > 헤더 -->
			<div class="table-header fundinglist-header" style="display: none;">
				<span class="col-title">펀딩명</span> <span class="col-period">펀딩기간</span>
				<span class="col-status">달성여부</span>
			</div>

			<!-- 구매내역 리스트 -->
			<div id="order-list-container" class="content">
				<jsp:include page="/WEB-INF/views/pages/user/mypage_orderList.jsp" />
			</div>

			<!-- 내가 참여한 펀딩 리스트 -->
			<div id="funding-list-container" class="content"
				style="display: block;">
				<jsp:include page="/WEB-INF/views/pages/user/myPage_fundingList.jsp" />
			</div>
		</section>
	</div>


	<!-- 내 정보 수정하기 -->
	<%@ include file="/WEB-INF/views/pages/user/mypage_userInfoEdit.jsp"%>
	
	<!-- 결제상세 -->
	<%@ include file="/WEB-INF/views/pages/user/mypage_paymentDetail.jsp"%>


<script>
  // html 로딩 완료 후 실행되는 함수들
  window.addEventListener('DOMContentLoaded', () => {
  bindModalEvents(); //모달 열기/닫기
  bindBuyTabs(); // 구매 탭 클릭 시 데이터 불러오기
  bindFundingTabs(); // 펀딩 탭 클릭 시 데이터 불러오기
  bindMenuClickEvents(); // 사이드바 메뉴 클릭 시 탭 전환
 });
 
// 사이드바 메뉴 클릭 이벤트 (구매내역/펀딩 전환)
function bindMenuClickEvents() {
  const buyMenu = document.querySelector('.buylist');       
  const fundingMenu = document.querySelector('.activeFunding');

  buyMenu.addEventListener('click', e => {
	    e.preventDefault();
	    buyMenu.classList.add('active');
	    fundingMenu.classList.remove('active');
	    
	//탭 숨기기/보여주기
    document.querySelector('.tab-wrapper.buylist').style.display = 'flex'; // 구매내역 탭 보임
    document.querySelector('.tab-wrapper.fundinglist').style.display = 'none'; 
    
    // 헤더 토글
    document.querySelector('.buylist-header').style.display = 'flex'; //구매내역 헤더 보임
    document.querySelector('.fundinglist-header').style.display = 'none';
    
    //컨테니어 숨기기/보여주기
    document.getElementById('order-list-container').style.display = 'block'; // 구매내역 리스트 보임
    document.getElementById('funding-list-container').style.display = 'none'; 

    // 기본값으구매내역 탭에서 "모든 구매 내역" 활성화
    document.querySelector('.allbuylist').click();
  });

  fundingMenu.addEventListener('click', e => {
    e.preventDefault();
    fundingMenu.classList.add('active');
    buyMenu.classList.remove('active');

    //탭 숨김 보여주기
    document.querySelector('.tab-wrapper.buylist').style.display = 'none';
    document.querySelector('.tab-wrapper.fundinglist').style.display = 'flex'; //펀딩 탭 보임
    
	 // 헤더 토글
    document.querySelector('.buylist-header').style.display = 'none'; 
    document.querySelector('.fundinglist-header').style.display = 'flex'; //펀딩 헤더 보임

 	//컨테이너 보이기/숨기기
    document.getElementById('funding-list-container').style.display = 'block'; // 펀딩 리스트 보임
    document.getElementById('order-list-container').style.display = 'none';

    // 기본값으로 펀딩탭에서 "내가 참여한 모든 펀딩" 활성화
    document.querySelector('.allfundinglist').click();
  });
}

 // 구매내역 탭 > 모든 구매내역, 결제완료, 결제취소
function bindBuyTabs() {
 document.querySelectorAll('.allbuylist, .complete, .cancel').forEach(tab => {
   tab.addEventListener('click', e => {
     e.preventDefault();

     document.querySelectorAll('.allbuylist, .complete, .cancel').forEach(t => t.classList.remove('active'));
     tab.classList.add('active');

     const status = tab.getAttribute('data-status');
     console.log("status", status);
    
     currentTabStatus = status;

     fetch(`${cpath}/order/list?status=\${status}`)
       .then(response => {
         if (!response.ok) throw new Error('서버 응답 에러: ' + response.status);
         return response.text();
       })
       .then(html => {
         document.getElementById('order-list-container').innerHTML = html;
         bindModalEvents(); 
       })
       .catch(err => {
         alert('데이터를 불러오는 중 오류가 발생했습니다.');
       });
   });
 });
}
 
// 펀딩 탭 > 내가 참여한 모든 펀딩, 진행 중인 펀딩, 달성된 펀딩, 미달성된 펀딩
function bindFundingTabs() {
 document.querySelectorAll('.allfundinglist, .progressing, .achieved, .failed').forEach(tab => {
	 tab.addEventListener('click', e => {
		 e.preventDefault();
		 
		 document.querySelectorAll('.allfundinglist, .progressing, .achieved, .failed').forEach(t => t.classList.remove('active'));
		 tab.classList.add('active');
		 
		 const status = tab.getAttribute('data-status');
		 currentTabStatus = status;
		 
		 fetch(`${cpath}/fundings/list?status=\${status}`)
		 .then(response => response.text())
		 .then(html => {
			 document.getElementById('funding-list-container').innerHTML = html;
			 bindModalEvents();
		 })
	 });
 });
} 
</script>
</body>
</html>