<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/pages/seller/funding_ai_input.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
	$(function() {
		const content = `${aiResponse != null ? aiResponse.content : ''}`; // JSTL 데이터를 JS로 가져옴
		$("#htmlContent").html(content); // innerHTML로 출력
		$("#fundingContentHidden").val(content); // 서버 제출용 textarea에 저장
	});
</script>
<h3>불고기 정식은 어떤 느낌인가요?</h3>
<p class="example">예: 푸짐한 한 끼, 집밥 느낌, 인기 메뉴</p>

<form action="${pageContext.request.contextPath}/ai/ai-generate"
	method="post">
	<div class="input-group">
		<input type="text" name="keywords" placeholder="떠오르는 단어를 적어주세요"
			required />
	</div>
	<div class="input-group">
		<input type="text" name="target" placeholder="예: 20대 여성, 직장인, 커플"
			required />
	</div>

	<div class="btn-group">
		<button type="button" onclick="history.back()" class="nav-btn">이전</button>
		<button type="submit" class="nav-btn">AI 생성</button>
	</div>
</form>

<c:if test="${not empty aiResponse}">
	<h3>
		아래는 AI가 자동으로 만든 펀딩 제목과 설명, 관련 단어입니다.<br> 원하는 문장이 아니라면 아래 [다시 생성]
		버튼으로 다시 요청해보세요!
	</h3>
	<form action="${cpath}/seller/fundings/submit-funding"
		method="post">
		<div class="input-group">
			<label for="title">펀딩 제목</label> <input type="text" id="title"
				name="fundingName" placeholder="예: 불고기 정식 펀딩"
				value="${aiResponse.title}" required />
		</div>

		<div class="input-group">
			<label for="htmlContent">펀딩 설명</label>
			<div id="htmlContent" class="content-viewer"></div>
			<textarea id="fundingContentHidden" name="fundingDesc"
				style="display: none;" required></textarea>
		</div>

		<div class="input-group">
			<label for="hashtags">관련 단어</label> <input type="text" id="hashtags"
				name="hashtags" placeholder="예: 불고기, 정식, 든든한한끼"
				value="${aiResponse.hashtags}" required />
		</div>

		<div class="btn-group">
			<button type="button" class="nav-btn" onclick="history.back()">이전</button>
			<button type="submit" class="nav-btn filled">등록</button>
		</div>
	</form>
</c:if>

<c:if test="${not empty aiError}">
	<p style="color: red;">
		<b>에러:</b> ${aiError}
	</p>
</c:if>
