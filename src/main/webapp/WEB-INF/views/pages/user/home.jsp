<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet" href="${cpath}/resources/css/pages/user/home.css">
<link rel="stylesheet"
	href="${cpath}/resources/css/components/regionDropdown.css">

<div class="content-box">
	<%@ include file="/WEB-INF/views/common/categoryBar.jsp"%>
	<div class="main-contents">
		<div class="head-box">
			<p>
				세연님 <span>추천 펀딩</span>
			</p>
			<!-- 지역 드롭다운 -->
			<div class="dropdown-box">
				<div class="dropdown">
					<button class="dropbtn" id="sidoButton"
						onclick="toggleDropdown('sido')">시/도 선택</button>
					<div class="dropdown-content" id="sidoDropdown"></div>
				</div>
				<div class="dropdown">
					<button class="dropbtn" id="sigunguButton"
						onclick="toggleDropdown('sigungu')">시/군/구 선택</button>
					<div class="dropdown-content" id="sigunguDropdown"></div>
				</div>
				<div class="find-btn" id="findBtn">찾기</div>
			</div>
		</div>
		<div id="main-contents">
			<div id="initialContent">
				<%@ include file="/WEB-INF/views/common/funding.jsp"%>
			</div>

			<div id="filteredFundingList"></div>

			<div class="add-button" id="moreButton">
				<div class="add-button-text">더보기</div>
			</div>
		</div>
	</div>
</div>

<script>
	const cpath = '${cpath}';
</script>

<script src="${cpath}/resources/js/funding.js"></script>
<script src="${cpath}/resources/js/region.js"></script>
