<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>QR 코드 생성</title>
</head>
<body>
	<h1>QR 코드 생성 완료</h1>

	<h2>메뉴 설명</h2>
	<p>펀딩 id: ${fundingId}</p>
	<p>펀딩 이름: ${fundingName}</p>
	<p>아래의 QR 코드를 스캔하면 이동합니다:</p>


	<img src="${qrImageUrl}" alt="QR Code" />


	<br />
	<br />

	<!-- 다운로드 링크 -->
	<a href="${qrImageUrl}" download="my_qr_code.png">QR코드 다운로드</a>

</body>
</html>
