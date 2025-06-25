<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>쿠폰이 발급되었습니다!</h2>
	<p>QR 코드를 스캔해서 가맹점에 보여주세요.</p>
	<img src="${qrImageUrl}" alt="쿠폰 QR 코드" />
	<p>
		쿠폰 코드: <strong>${couponCode}</strong>
	</p>
</body>
</html>