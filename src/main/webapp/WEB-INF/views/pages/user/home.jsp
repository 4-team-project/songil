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
				${sessionScope.loginUser.nickname}님 <span>추천 펀딩</span>
			</p>
			<div class="dropdown-box">
				<%@ include file="/WEB-INF/views/common/regionDropdown.jsp"%>
				<div class="find-btn">찾기</div>
			</div>
		</div>
		<div id="main-contents">
			<div id="initialContent">
				<%@ include file="/WEB-INF/views/common/funding.jsp"%>
			</div>

			<div id="filteredFundingListBox"></div>

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
<script src="${cpath}/resources/js/searchBox.js"></script>
