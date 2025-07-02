<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/storeManagement.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
<script
	src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>

<div class="main-title-box">
	<img class="main-icon" src="${cpath}/resources/images/icons/store.svg"
		alt="store" />
	<div class="main-text">
		<c:out value="${userDTO.nickname}" default="사장님" />
		의
		<div class="highlight">상점 관리</div>
	</div>
</div>
<div class="store-btn-box">
	<div class="store-btn">
		<div class="store-btn-icon">
			<img src="${cpath}/resources/images/sideBar/add_active.svg" alt="add" />
		</div>
		<div class="store-btn-text">상점 추가하기</div>
	</div>
	<div class="store-btn">
		<div class="store-btn-text">상점 목록보기</div>
	</div>
</div>

<div class="store-info-box">
	<div class="store-info-title">
		<div class="highlight">[현재 상점]</div>
		김밥식 맛집
	</div>
	<div class="store-info-content">주소: 서울시 중구 세종대로</div>
	<div class="store-info-content">전화번호: 02-1234-5678</div>
	<div class="store-edit-btn">상점 정보 수정</div>
</div>

<div class="store-menu-box">
	<div class="store-menu-title">불고기 정식집 메뉴</div>
	<div class="store-menu-content">메뉴 사진을 눌러주시면 메뉴 정보를 보실 수 있어요</div>

	<div class="store-menu-content-img-container swiper">
		<div class="swiper-wrapper">
			<div class="swiper-slide">
				<img class="store-menu-content-img"
					src="${cpath}/resources/images/category/cake.svg" alt="사진 1" />
				<div class="store-menu-content-name">불고기1</div>
			</div>
			<div class="swiper-slide">
				<img class="store-menu-content-img"
					src="${cpath}/resources/images/category/cake.svg" alt="사진 2" />
			</div>
			<div class="swiper-slide">
				<img class="store-menu-content-img"
					src="${cpath}/resources/images/category/cake.svg" alt="사진 3" />
			</div>
			<div class="swiper-slide">
				<img class="store-menu-content-img"
					src="${cpath}/resources/images/category/cake.svg" alt="사진 3" />
			</div>
			<div class="swiper-slide">
				<img class="store-menu-content-img"
					src="${cpath}/resources/images/category/cake.svg" alt="사진 3" />
			</div>
		</div>

		<div class="swiper-button-circle swiper-button-circle-prev">
			<div class="swiper-button-prev"></div>
		</div>
		<div class="swiper-button-circle swiper-button-circle-next">
			<div class="swiper-button-next"></div>
		</div>


		<div class="swiper-pagination"></div>
	</div>
	<div class="menu-btn-box">
		<div class="menu-edit-btn"
			onclick="location.href='${cpath}/seller/product/new'">메뉴 추가하기</div>
		<div class="menu-edit-btn">메뉴 목록보기</div>
	</div>
</div>


<script>
	document.addEventListener("DOMContentLoaded", function() {
		new Swiper(".store-menu-content-img-container", {
			slidesPerView : 3,
			spaceBetween : 20,
			loop : true,
			pagination : {
				el : ".swiper-pagination",
				clickable : true,
			},
			navigation : {
				nextEl : ".swiper-button-next",
				prevEl : ".swiper-button-prev",
			},
		});
	});
</script>

