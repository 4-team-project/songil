<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	<%@ include file="/WEB-INF/views/common/components/header.jsp"%>
	<div class="container">

		<div class="mypage-container">
			<aside class="sidebar">
				<div class="profile-section">
					<div class="profile-image"></div>
					<div class="username">닉네임</div>
					<div class="editMypage">내 정보 수정하기</div>
				</div>
				<nav class="menu">
					<ul>
						<li class="active">구매 내역</li>
						<li class="active2">내가 참여한 펀딩</li>
					</ul>
				</nav>
				<a href="#" class="logout">로그아웃</a>
			</aside>

			<div class="tab-menu">
				<ul>
					<li class="active">모든 구매 내역</li>
					<li>결제 완료</li>
					<li>결제 취소</li>
				</ul>
			</div>


		</div>




		<%@ include file="/WEB-INF/views/common/components/footer.jsp"%>
	</div>
</body>
</html>