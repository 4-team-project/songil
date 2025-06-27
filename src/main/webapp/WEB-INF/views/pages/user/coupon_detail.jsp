<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<script src="https://cdn.jsdelivr.net/npm/html2canvas@1.4.1/dist/html2canvas.min.js"></script>
<script src="https://kit.fontawesome.com/5db5b8890b.js" crossorigin="anonymous"></script>
<meta charset="UTF-8">
<title>쿠폰 보기</title>
<c:set var="qrBaseUrl" value="https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=http%3A%2F%2F192.168.0.22%3A9999%2Fcoupon%2FsellerCheck%3FcouponCode%3D" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/coupon_detail.css" />
<c:set var="qrUrl" value="${qrBaseUrl}${coupon.couponCode}" />
</head>
<body>

	<div class="coupon-card" id="couponCard" data-funding-name="${fn:escapeXml(funding.fundingName)}">
	<button class="detail-close-btn" onclick="exitDetail()">✕</button>
		<h1><strong>${store.storeName}</strong></h1>
		<h2>${product.productName}</h2>

		<!-- QR 코드: qr 생성하는 url을 변수로 선언 -> 쿠폰 코드를 그 옆에 붙여서 qr코드 url 완성 -> src에서 불러옴-->
		<!-- c:set qrBaseUrl 부분에서 ip주소만 바꾸면 실행 가능할거 같습니다. -->
		 <img class="qr" src="${qrUrl}" alt="QR 코드" crossorigin="anonymous" />

		<div class="coupon-date"><fmt:formatDate value='${coupon.createdAt}' pattern='yyyy-MM-dd' />~
			<fmt:formatDate value='${coupon.expiredAt}' pattern='yyyy-MM-dd' />
			</div>

		<div class="download-icon" onclick="downloadCouponImage()">쿠폰 다운로드 <i class="fa-solid fa-download"></i></div>

		<div class="coupon-info">
			<strong>${funding.fundingName}</strong> ${funding.fundingDesc}<br />
			<br />
		</div>
	</div>
	<script>
	
	function exitDetail() {
		window.history.back();
	}
	
	
	function sanitizeFileName(name) {
		  return name.replace(/[\\/:*?"<>|]/g, '').replace(/\s+/g, '_');
		}

		function ensurePngExtension(name) {
		  return name.toLowerCase().endsWith('.png') ? name : name + '.png';
		}

		function downloadCouponImage() {
		  const card = document.getElementById('couponCard');
		  const downloadBtn = card.querySelector('.download-icon');
		  const fundingName = card.dataset.fundingName || 'coupon';
		  const safeFileName = sanitizeFileName(fundingName);
		  const finalFileName = ensurePngExtension(safeFileName);
		  const closeBtn = card.querySelector('.detail-close-btn');

		  downloadBtn.style.display = 'none';
		  closeBtn.style.display = 'none';
		  
		  html2canvas(card, {
		    useCORS: true,
		    allowTaint: false,
		    backgroundColor: null
		  }).then(canvas => {
		    const link = document.createElement('a');
		    link.download = finalFileName;
		    link.href = canvas.toDataURL('image/png');
		    link.click();
		  }).catch(err => {
		    alert('이미지 저장에 실패했습니다: ' + err);
		  }).finally(() => {
			    // 캡처 후 다시 버튼 보이기
			    downloadBtn.style.display = '';
			    closeBtn.style.display = '';
			  });
		}
	</script>
</body>
</html>