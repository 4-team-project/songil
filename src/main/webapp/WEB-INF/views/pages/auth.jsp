<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/auth.css">

<script>
	$(function() {
		// 회원가입 모달 열기
		$('#joinBtn').on('click', function(e) {
			e.preventDefault();
			$('#joinModal').fadeIn();
		});

		// 모달 닫기 (X 버튼 또는 취소 버튼 또는 바깥 영역 클릭)
		$('#closeJoinModal, #cancelJoinBtn').on('click', function() {
			$('#joinModal').fadeOut();
		});

		$('#joinModal').on('click', function(e) {
			if (e.target === this) {
				$(this).fadeOut();
			}
		});
	});
</script>

<div class="login-container">
	<img src="${cpath}/resources/images/logo.svg" class="logo-img" />

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
				<label> <input type="radio" name="userType" value="user" checked /> 
					<span>사용자</span>
				</label> 
				<label> <input type="radio" name="userType" value="seller" />
					<span>소상공인</span>
				</label>
			</div>

			<!-- 로그인 버튼 -->
			<button type="submit" class="login-submit">로그인</button>

			<!-- 링크들 -->
			<div class="login-links">
				<a href="#" id="joinBtn" class="btn" style="float: right;">회원가입</a>
			</div>
		</form>
	</div>
	<div id="joinModal" class="modal" style="display: none;">
		<div class="modal-content">
			<span class="close" id="closeJoinModal">&times;</span>
			<h2 class="modal-title">회원가입</h2>

			<form id="joinForm" method="post" action="${cpath}/user/join">
				<div class="modal-info">
					<p>
						<strong>이름</strong><input type="text" name="name" required
							class="modal-input" />
					</p>
					<p>
						<strong>전화번호</strong><input type="text" name="phone" required
							class="modal-input" />
					</p>
					<p>
						<strong>비밀번호</strong><input type="password" name="password"
							required class="modal-input" />
					</p>
					<p>
						<strong>비밀번호 확인</strong><input type="password"
							name="confirmPassword" required class="modal-input" />
					</p>
				</div>

				<div class="modal-buttons">
					<button type="submit" class="modal-btn confirm">가입하기</button>
					<button type="button" class="modal-btn cancel" id="cancelJoinBtn">취소</button>
				</div>
			</form>
		</div>
	</div>
</div>