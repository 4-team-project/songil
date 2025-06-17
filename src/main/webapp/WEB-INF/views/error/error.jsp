<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>에러 발생</title>
</head>
<body>
<h2>문제가 발생했습니다</h2>
<p>에러코드: ${error.errorCode}</p>
<p>메시지: ${error.message}</p>

<c:if test="${not empty error.detail}">
    <p>상세: ${error.detail}</p>
</c:if>

<a href="/main">메인으로 돌아가기</a>
</body>
</html>
