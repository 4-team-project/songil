<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
    
<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/auth_signup.css">
	
<div class="signup-container">
	<img src="${cpath}/resources/images/logo.svg" class="logo-img" />

	<form id="joinForm" method="post" action="${cpath}/user/join">
		<div class="modal-info">
			<p>
				<strong>이름</strong>
				<input type="text" name="name" required class="modal-input" />
			</p>
			<p>
				<strong>전화번호</strong>
				<input type="text" name="phone" required class="modal-input" placeholder="숫자만 입력" />
			</p>
			<p>
				<strong>비밀번호</strong>
				<input type="password" name="password" required class="modal-input" />
			</p>
			<p>
				<strong>비밀번호 확인</strong>
				<input type="password" name="confirmPassword" required class="modal-input" />
			</p>
		</div>

		<!-- 회원 유형 선택 (선택 사항) -->
		<div class="modal-info">
			<p>
				<strong>회원 유형</strong>
				<label><input type="radio" name="userType" value="user" checked /> 사용자</label>
				<label style="margin-left: 10px;"><input type="radio" name="userType" value="seller" /> 소상공인</label>
			</p>
		</div>

		<div class="modal-buttons">
			<button type="submit" class="modal-btn confirm">가입하기</button>
			<button type="button" class="modal-btn cancel" id="cancelJoinBtn">취소</button>
		</div>
	</form>
</div>