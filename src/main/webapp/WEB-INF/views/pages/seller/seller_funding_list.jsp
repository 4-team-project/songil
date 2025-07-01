<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>

<head>
<!-- jQuery -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<meta charset="UTF-8">
<link rel="stylesheet" href="/resources/css/seller_funding_list.css" />
<title>Insert title here</title>
</head>
<body>
	<%-- userId 값을 숨겨진 필드에 저장 --%>
	<input type="hidden" id="currentUserId"
		value="${store != null ? store.userId : ''}">
	<h3>
		현재 상점 : ${store.storeName}
		<button id="showStoresBtn">다른 지점 보기</button>
	</h3>
	<!-- 다른 지점 store 리스트 -->
	<div id="storeListContainer"></div>

	<hr>
	<h4>펀딩 현황</h4>
	<div class="funding-tabs">
		<button id="tabAll" class="active" data-status="all">전체</button>
		<button id="tabInProgress" data-status="진행중">진행중</button>
		<button id="tabScheduled" data-status="준비중">준비중</button>
		<button id="tabEnded" data-status="종료">종료</button>
	</div>
	<br>
	<div id="fundingCountSummary" class="funding-summary">
    </div>
    <br>
	
	<!-- 펀딩 리스트 출력 영역 -->
	<div id="fundingListContainer">
	</div>

	<script src="<c:url value="/resources/js/seller_funding_list.js"/>"></script>
</body>
</html>