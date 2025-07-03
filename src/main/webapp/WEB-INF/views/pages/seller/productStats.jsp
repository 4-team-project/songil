<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/pages/seller/sellerMain.css">
<title>상품 통계</title>
<style>
button, button:hover, button:active, button:focus {
	cursor: url('${cpath}/resources/images/cursor.svg') 2 2, auto !important;
	width: 200px;
	height: 70px;
	font-size: 20px;
}

.summary-box {
	margin: 10px 0;
}

.menu-box {
	display: grid;
	grid-template-columns: 1fr 2fr;
	grid-template-rows: auto auto;
	gap: 20px;
	border: 1px solid #f1c5b3;
	border-radius: 10px;
	background-color: #fff4ee;
	padding: 20px;
	margin: 20px 0;
	position: relative;
	width: 100%;
	box-sizing: border-box;
}

/* ✅ 이미지: 1열 */
.image-slider {
	grid-column: 1;
	grid-row: 1/span 2;
	aspect-ratio: 4/3;
	background-color: #ddd;
	display: flex;
	align-items: center;
	justify-content: center;
	position: relative;
}

.image-slider img {
	width: 100%;
	height: 100%;
	object-fit: cover;
}

.prev-btn, .next-btn {
	all: unset; /* 버튼 스타일 초기화*/
	position: absolute;
	background-color: #FF9670;
	border: none;
	color: white;
	font-size: 20px;
	width: 30px !important;
	height: 20px;
	cursor: pointer;
	text-align: center;
	border-radius: 50%; /* 👈 동그랗게 */
}

.prev-btn {
	left: 5px;
}

.next-btn {
	right: 5px;
}

/* 메뉴 정보: 1행 2열 */
.menu-info {
	grid-column: 2;
	grid-row: 1;
	text-align: left;
	display: flex;
	flex-direction: column;
	justify-content: center;
	gap: 12px;
}
/* 수정 버튼: 2행 2열 */
.edit-btn-wrap {
	grid-column: 2;
	grid-row: 2;
	justify-self: end;
}

.product-name {
	font-size: 24px;
	font-weight: bold;
	margin: 2;
	color: #333;
}

.rating-price {
	display: flex;
	align-items: center;
	gap: 20px;
	font-size: 18px;
	color: #555;
}

.rating {
	background-color: #ffe8a1;
	padding: 4px 8px;
	border-radius: 5px;
}

.price {
	font-weight: bold;
	color: #e74a3b;
	font-size: 20px;
}

.description {
	font-size: 20px;
	line-height: 1.6;
	color: #444;
}
</style>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<div class="main-content"
	style="cursor: url('${cpath}/resources/images/cursor.svg') 2 2, auto;">
	<div class="menu-header">
		<button class="menu-list-btn" onclick="alert('메뉴 목록 보기 클릭!')">메뉴
			목록 보기</button>
	</div>
	<div class="menu-box">
		<div class="image-slider">
			<button class="prev-btn" onclick="prevImage()">&lt;</button>
			<img id="menu-image" src="${productDTO.thumbnailImageUrl}"
				alt="메뉴 이미지">
			<button class="next-btn" onclick="nextImage()">&gt;</button>
		</div>
		<div class="menu-info">
			<h2 class="product-name">${productDTO.productName}</h2>
			<div class="rating-price">
				<c:choose>
					<c:when
						test="${not empty productDTO.averageRating and productDTO.averageRating > 0}">
						<span class="rating">⭐ ${productDTO.averageRating}</span>
					</c:when>
					<c:otherwise>
						<span class="rating">📝 리뷰가 없습니다</span>
					</c:otherwise>
				</c:choose>
				<span class="price"><fmt:formatNumber
						value="${productDTO.price}" type="currency" /></span>
			</div>
			<p class="description">${productDTO.description}</p>
		</div>

		<div class="edit-btn-wrap">
			<button>메뉴 정보 수정</button>
		</div>
	</div>

	<div class="summary-box">
		<h2>리뷰 요약</h2>
		<ul id="review-summary-list">
			<c:choose>
				<c:when
					test="${not empty positiveSummary or not empty negativeSummary}">
					<c:if test="${not empty positiveSummary}">
						<li><strong>👍 긍정 요약</strong></li>
						<c:forEach var="line" items="${positiveSummary}">
							<li>💬 ${line}</li>
						</c:forEach>
					</c:if>
					<c:if test="${not empty negativeSummary}">
						<li style="margin-top: 1rem;"><strong>👎 부정 요약</strong></li>
						<c:forEach var="line" items="${negativeSummary}">
							<li>💬 ${line}</li>
						</c:forEach>
					</c:if>
				</c:when>
				<c:otherwise>
					<li>😢 리뷰 요약이 없습니다.</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
	<div class="summary">

		<div class="summary-box">
			<h2>연령대 비율</h2>
			<canvas id="ageChart"></canvas>
		</div>

		<div class="summary-box">
			<h2>성별 비율</h2>
			<canvas id="genderChart"></canvas>
		</div>
	</div>
</div>

<script>

new Chart(document.getElementById('ageChart'), {
    type: 'pie',
    data: {
        labels: [<c:forEach var="item" items="${productAgeStats}">"${item.label}",</c:forEach>],
        datasets: [{
            backgroundColor: ['#36b9cc', '#1cc88a', '#f6c23e', '#e74a3b', '#858796'],
            data: [<c:forEach var="item" items="${productAgeStats}">${item.value},</c:forEach>]
        }]
    }
});

new Chart(document.getElementById('genderChart'), {
    type: 'doughnut',
    data: {
        labels: [<c:forEach var="item" items="${productGenderStats}">"${item.label}",</c:forEach>],
        datasets: [{
            backgroundColor: ['#4e73df', '#e74a3b'],
            data: [<c:forEach var="item" items="${productGenderStats}">${item.value},</c:forEach>]
        }]
    }
});
</script>

