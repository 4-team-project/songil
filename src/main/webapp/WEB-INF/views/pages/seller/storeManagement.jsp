<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/storeManagement.css">

<div class="main-content">
	<div class="store-container">
		<div class="title-container">
			<h1>
				<img
					src="${cpath}/resources/images/icons/store.svg"
					alt="store" class="icon" />
				<c:out value="${userDTO.nickname}" default="사장님" />
				의 <span class="highlight">상점 관리</span>
			</h1>
		</div>
		<div class="store-info-box">
			<strong>[현재 상점] 불고기 정식집</strong>
			<p>주소: 서울시 중구 세종대로 123</p>
			<p>전화번호: 02-1234-5678</p>
			<button class="btn edit">상점 정보 수정</button>
		</div>
	</div>

	<div class="menu-section">
		<h3>불고기 정식집 메뉴</h3>
		<p>메뉴 사진을 눌러주시면 메뉴 정보를 보실 수 있어요</p>

		<div class="menu-carousel">
			<button class="carousel-btn left">&lt;</button>
			<div class="menu-card">
				<div class="menu-img"></div>
				<p>불고기1</p>
			</div>
			<div class="menu-card">
				<div class="menu-img"></div>
				<p>불고기2</p>
			</div>
			<div class="menu-card">
				<div class="menu-img"></div>
				<p>불고기3</p>
			</div>
			<button class="carousel-btn right">&gt;</button>
		</div>

		<div class="menu-buttons">
			<button class="btn add">메뉴 추가하기</button>
			<button class="btn list">메뉴 목록 보기</button>
		</div>
	</div>
</div>
</div>
