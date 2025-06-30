<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/pages/seller/funding_ai_input.css">

<h3>불고기 정식은 어떤 느낌인가요?</h3>
<p class="example">예: 푸짐한 한 끼, 집밥 느낌, 인기 메뉴</p>

<form
	action="${pageContext.request.contextPath}/seller/create-step6"
	method="post">
	<div class="input-group">
		<input type="text" name="keywords" placeholder="떠오르는 단어를 적어주세요"
			required />
	</div>

	<div class="btn-group">
		<button type="button" onclick="history.back()" class="nav-btn">이전</button>
		<button type="submit" class="nav-btn">다음</button>
	</div>
</form>
