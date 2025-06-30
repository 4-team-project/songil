<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/auth.css">

<div class="login-container">
	<img src="${cpath}/resources/images/logo.svg" class="logo-img" />

	<c:if test="${not empty resultMessage}">
		<div class="login-error-message">
			${resultMessage}
		</div>
	</c:if>
	
	<div class="login-box">
		<form action="${cpath}/auth/login" method="post">
			<!-- 핸드폰 번호 입력 -->
			<div class="input-group">
				<input type="text" name="phone" placeholder="휴대폰 번호 (숫자만 입력)"
					required />
			</div>

			<!-- 비밀번호 입력 -->
			<div class="input-group">
				<input type="password" name="password" placeholder="비밀번호" required />
			</div>

			<!-- 사용자 / 소상공인 선택 -->
			<div class="user-type-select">
				<label> <input type="radio" name="userType" value="사용자" checked /> 
					<span>사용자</span>
				</label> 
				<label> <input type="radio" name="userType" value="소상공인" />
					<span>소상공인</span>
				</label>
			</div>

			<!-- 로그인 버튼 -->
			<button type="submit" class="login-submit">로그인</button>

			<!-- 링크들 -->
			<div class="login-links">
				<a href="${cpath}/auth/signup" id="joinBtn" class="btn" style="float: right;">회원가입</a>
			</div>
		</form>
	</div>
</div>