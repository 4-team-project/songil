<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/common/components/searchBox.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/common/components/header.css">
</head>
<body>
<div class="box"></div>
<div class="header-box">

	<div class="logo">
		<img
			src="${pageContext.request.contextPath}/resources/images/logo.svg"
			alt="logo" />
	</div>

	<div class="lower-box">
		<%@ include file="/WEB-INF/views/common/components/searchBox.jsp" %>

		<div class="nav-box">
			<div class="nav-text" onclick="location.href='${pageContext.request.contextPath}/user/coupon'">내 쿠폰함</div>
			<div class="nav-text" onclick="location.href='${pageContext.request.contextPath}/user/mypage'">마이페이지</div>
		</div>
	</div>
</div>
</body>
</html>
