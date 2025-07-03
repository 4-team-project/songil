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
<form action="/seller/edit/step1" method="post" enctype="multipart/form-data">
    <label for="fundingName">펀딩 이름:</label>
    <input type="text" id="fundingName" name="fundingName" value="${tempFunding.fundingName}">
	<input type="hidden" id="fundingId" name="fundingId" value="${tempFunding.fundingId}">
	<input type="hidden" id="currentProcessingUserId" name="currentProcessingUserId" value="${currentProcessingUserId}">
    <label for="fundingType">펀딩 종류:</label>
    <select id="fundingType" name="fundingType">
        <option value="한정" ${tempFunding.fundingType eq '한정' ? 'selected' : ''}>한정</option>
        <option value="일반" ${tempFunding.fundingType eq '일반' ? 'selected' : ''}>일반</option>
    </select>
    
    <label for="fundingDesc">펀딩 설명:</label>
    <textarea id="fundingDesc" name="fundingDesc">${tempFunding.fundingDesc}</textarea>

   <%--  <label for="menuPhotoFile">메뉴 사진:</label>
    <input type="file" id="menuPhotoFile" name="menuPhotoFile">
    <c:if test="${not empty tempFunding.fundingName}">
        <img src="${tempFunding.fundingName}" alt="Current Menu Photo" style="width: 100px;">
    </c:if> --%>

    <button type="submit">다음</button>
</form>
</body>
</html>