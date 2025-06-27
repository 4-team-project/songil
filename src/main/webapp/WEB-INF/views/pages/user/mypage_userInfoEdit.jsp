<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<script src="${cpath}/resources/js/userInfoEditJS.js"></script>

	<!-- 내 정보 수정 모달 -->
	<div id="user-info-modal" class="modal" style="display: none">
		<div class="modal-content">
			<span class="close-btn">&times;</span>
			<h2>내 정보 수정</h2>

			<form id="user-info-form">
				<label for="name">이름</label> <input type="text" id="name"
					name="name" class="input-no-border" value="${user.name}" readonly /><br>

				<label for="birth">생년월일</label> <input type="text" id="birth"
					name="birth" class="input-no-border" value="${user.birth}" readonly /><br>

				<label for="phone">전화번호</label> <input type="tel" id="phone"
					name="phone" class="input-no-border" value="${user.phone}" readonly /><br>

				<label for="gender">성별</label> <input type="text" name="gender"
					id="gender" class="input-no-border" value="${user.gender }자"
					readonly /><br> 
					
				<label for="addr">지역</label> <input
					type="text" class="input-no-border" id="sido"
					value="${user.sido } ${user.sigungu}"><br>
				<hr>

				<div class="form-row">
					<label for="nickname">닉네임</label> <input type="text" id="nickname"
						name="nickname" value="${user.nickname}" /><br>
				</div>

				<div class="form-row">
					<label for="password">새 비밀번호</label> <input type="text"
						id="password" name="password" autocomplete="off" />
				</div>
				
				<div class="condition">
					<div>영어와 숫자 조합으로 6자 이상 입력해주세요</div>
				</div>

				<div class="form-row">
					<label for="passwordConfirm">비밀번호 확인</label> <input type="password"
						id="passwordConfirm" name="passwordConfirm" />
				</div>
			</form>

			<div class="modal-buttons">
				<button type="button" class="modal-btn Uedit">수정저장하기</button>
				<button type="button" class="modal-btn Ucheck">확인</button>
			</div>
		</div>
	</div>
</body>
</html>