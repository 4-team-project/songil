<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/pages/seller/funding_direct_input.css">



<form
	action="${pageContext.request.contextPath}/seller/fundings/submit-funding"
	method="post">
	<h3>펀딩 제목과 내용을 입력해주세요.</h3>
	<div class="input-group">
		<label for="title">펀딩 제목을 입력해 주세요</label><br> <input type="text"
			id="title" name="fundingName" placeholder="예: 불고기 정식 펀딩" required>
	</div>

	<div class="input-group">
		<label for="description">펀딩에 대한 설명을 입력해 주세요</label><br>
		<textarea id="description" name="fundingDesc"
			placeholder="예: 지금 예약하면 짜장면 한 그릇 추가 증정!&#10;혼자 먹어도, 둘이 먹어도 좋습니다."
			required></textarea>
	</div>

	<div class="input-group"><br>
		<label for="keywords">관련 단어를 입력해 주세요</label><br> <input type="text"
			id="keywords" name="keywords" placeholder="예: 집밥, 한식, 점심특선">
	</div>

	<div class="btn-group">
		<button class="btn" type="button" onclick="history.back()" class="nav-btn">이전</button>
		<button class="btn" type="submit" class="nav-btn filled">등록</button>
	</div>
</form>