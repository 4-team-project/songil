<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>소상공인이 QR을 찍었을 때 사용확인 체크</h1>

<form action="/mypage/coupon/${param.couponCode}/use" method="post">

<p>펀딩id : ${param.fundingId}</p>
<p>펀딩이른 : ${param.fundingName}</p>
<p>couponCode: ${param.couponCode}</p>
<button type="submit">사용완료</button>

</form>
</body>
</html>