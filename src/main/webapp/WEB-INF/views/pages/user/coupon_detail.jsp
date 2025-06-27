<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>쿠폰 보기</title>
  <c:set var="qrBaseUrl" value="https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=http%3A%2F%2F192.168.0.19%3A9999%2Fcoupon%2FsellerCheck%3FcouponCode%3D"/>
  <c:url var="qrUrl" value="${qrBaseUrl}${coupon.couponCode}" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/coupon_detail.css" />
</head>
<body>

<div class="coupon-card">
  <h2>${intDiscountRate}%</h2>
  
  <!-- QR 코드: qr 생성하는 url을 변수로 선언 -> 쿠폰 코드를 그 옆에 붙여서 qr코드 url 완성 -> src에서 불러옴-->
  <!-- c:set qrBaseUrl 부분에서 ip주소만 바꾸면 실행 가능할거 같습니다. -->
  <img class="qr" src="${qrUrl}" alt="QR 코드">

  <div class="coupon-date">
    ${coupon.createdAt} ~ ${coupon.expiredAt}
    ${coupon.couponCode}
  </div>

  <div class="download-icon">⬇️</div>

  <div class="coupon-info">
    <strong>${funding.fundingName}</strong>
    ${funding.fundingDesc}<br/><br/>
  </div>
</div>

</body>
</html>