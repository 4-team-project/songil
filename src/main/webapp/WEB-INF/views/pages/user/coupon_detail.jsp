<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>쿠폰 보기</title>
  <style>
    .coupon-card {
      width: 300px;
      background-color: #fff4ed;
      border-radius: 16px;
      padding: 20px;
      margin: 20px auto;
      box-shadow: 0 4px 8px rgba(0,0,0,0.1);
      font-family: 'Pretendard', sans-serif;
      text-align: center;
      position: relative;
    }

    .coupon-card h2 {
      color: #ff6e40;
      font-size: 20px;
      margin-bottom: 16px;
    }

    .coupon-card img.qr {
      width: 160px;
      height: 160px;
      margin: 0 auto;
      display: block;
      border: 2px solid #eee;
    }

    .coupon-date {
      margin-top: 10px;
      font-size: 14px;
      color: #555;
    }

    .download-icon {
      margin-top: 10px;
      font-size: 20px;
      color: #ff6e40;
    }

    .coupon-info {
      margin-top: 20px;
      padding-top: 12px;
      border-top: 1px dashed #aaa;
      text-align: left;
      font-size: 13px;
      line-height: 1.5;
      color: #333;
    }

    .coupon-info strong {
      display: block;
      margin-bottom: 4px;
    }
  </style>
</head>
<body>

<div class="coupon-card">
  <h2>${intDiscountRate}%</h2>
  
  <!-- QR 코드: DB에서 img src로 불러오기 -->
  <img class="qr" src="${coupon.couponCode}" alt="QR 코드">

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