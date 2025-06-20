<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>메인 페이지</title>
</head>
<body>
		<%@ include file="/WEB-INF/views/common/components/header.jsp" %>
  <h1>메인 페이지입니다</h1>
  <button onclick="goHome()">홈으로 이동</button>

  <script>
    function goHome() {
      location.href = '${pageContext.request.contextPath}/user/home';
    }
  </script>
</body>
</html>
