<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/pages/seller/sellerMain.css">
<title>상품 통계</title>
<style>
.main-content {
	flex: 1;
	padding: 40px;
	overflow-y: auto;
	box-sizing: border-box;
	background-color: white;
	display: flex;
	flex-direction: column;
	cursor: url('${cpath}/resources/images/cursor.svg') 2 2, auto;
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

/* ✅ 이미지: 1행 1열만 */
.image-slider {
	grid-column: 1;
	grid-row: 1;
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
	position: absolute;
	background-color: rgba(0, 0, 0, 0.4);
	border: none;
	color: white;
	font-size: 5px;
	width: 10px !important;
	height: 20px;
	line-height: 20px; /* 👈 세로 중앙 정렬 */
	cursor: pointer;
	padding: 0;
	border-radius: 50%; /* 👈 동그랗게 */
}

.prev-btn {
	left: 1px;
}

.next-btn {
	right: 1px;
}

/* 메뉴 정보: 1행 2열 */
.menu-info {
	grid-column: 2;
	grid-row: 1;
	text-align: left;
}

/* 수정 버튼: 2행 2열 */
.edit-btn-wrap {
	grid-column: 2;
	grid-row: 2;
	justify-self: end;
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
			<h3>${productDTO.productName}</h3>
			<p>${productDTO.description}</p>
			<p>${productDTO.price}</p>
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

