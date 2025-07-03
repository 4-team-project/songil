<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="/seller/edit/step2" method="post">
    <p>펀딩 이름: ${tempFunding.fundingName}</p>
    <p>펀딩 종류: ${tempFunding.fundingType}</p>
    
    <label for="targetQty">목표 수량:</label>
    <input type="number" id="targetQty" name="targetQty" value="${tempFunding.targetQty}">

    <label for="maxQty">최대 수량:</label>
    <input type="number" id="maxQty" name="maxQty" value="${tempFunding.maxQty}">
    
    <button type="submit">펀딩 저장</button>
    <button type="button" onclick="location.href='/seller/edit/step1'">이전</button>
</form>
</body>
</html>