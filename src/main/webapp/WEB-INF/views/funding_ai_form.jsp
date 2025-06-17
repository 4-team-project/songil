<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>AI 펀딩 자동 생성 (Groq Qwen 모델 테스트)</title>
</head>
<body>

<h2>AI 펀딩 제목/본문 생성</h2>

<!-- ✅ 안내문 추가 -->
<div style="border:1px solid #ccc; padding:10px; background-color:#f9f9f9; margin-bottom:20px;">
    ⚠️ <b>주의사항</b><br>
    이 페이지는 <b>Groq Qwen 모델</b>을 이용한 AI 문장 자동 생성 테스트 전용입니다.<br>
    Groq API 무료 사용에는 호출 제한이 있음으로 <b>테스트 목적 외 과도한 요청을 자제</b>해 주세요.<br>
    <br>
    🔧 <b>현재 사용 모델</b>: <code>qwen-qwq-32b</code><br>
    🌐 <b>API 제공처</b>: <a href="https://groq.com" target="_blank">Groq.com</a><br>
</div>

<form action="${pageContext.request.contextPath}/test/ai-generate" method="post" accept-charset="UTF-8">
    상품 키워드: <input type="text" name="keyword" size="40" value="${param.keyword}"><br><br>
    마케팅 대상: <input type="text" name="target" size="40" placeholder="예: 20대 여성, 직장인, 커플" value="${param.target}"><br><br>
    <input type="submit" value="AI 생성">
</form>

<c:if test="${not empty aiResponse}">
    <h3>AI 생성 결과</h3>
    <p><b>제목:</b> ${aiResponse.title}</p>
    <p><b>본문:</b> ${aiResponse.content}</p>
    <p><b>해시태그:</b> ${aiResponse.hashtags}</p>
</c:if>

</body>
</html>
