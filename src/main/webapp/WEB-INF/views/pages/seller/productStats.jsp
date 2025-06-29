<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
    <title>상품 통계</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            margin: 2rem;
            background: #f5f7fa;
        }
        h1 {
            text-align: center;
            margin-bottom: 2rem;
            color: #333;
        }
        .grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
        }
        .card {
            background: white;
            padding: 1.5rem;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        .card h2 {
            margin-bottom: 1rem;
            color: #444;
            font-size: 1.2rem;
        }
        canvas {
            width: 100% !important;
            height: auto !important;
        }
        #review-summary-list li {
            margin-bottom: 0.5rem;
        }
        .product-info {
            margin-bottom: 2rem;
            padding: 1.5rem;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        .product-info h2 {
            margin-bottom: 1rem;
            font-size: 1.5rem;
            color: #222;
        }
        .product-info p {
            margin: 0.3rem 0;
        }
        .highlight {
            font-weight: bold;
            color: #2c3e50;
        }
    </style>
</head>
<body>

<h1>📊 상품 통계</h1>

<div class="product-info">
    <h2>상품 정보</h2>
    <p><span class="highlight">상품명:</span> ${productDTO.productName}</p>
    <p><span class="highlight">가격:</span> <fmt:formatNumber value="${productDTO.price}" type="currency" currencySymbol="₩"/></p>
    <p><span class="highlight">평균 평점:</span>
        <c:choose>
            <c:when test="${not empty productDTO.averageRating}">
                ${productDTO.averageRating} / 5
            </c:when>
            <c:otherwise>
                평점 없음
            </c:otherwise>
        </c:choose>
    </p>
</div>

<div class="grid">
    <div class="card">
        <h2>1. 월별 매출</h2>
        <canvas id="monthlyChart"></canvas>
    </div>

    <div class="card">
        <h2>2. 연령대 비율</h2>
        <canvas id="ageChart"></canvas>
    </div>

    <div class="card">
        <h2>3. 성별 비율</h2>
        <canvas id="genderChart"></canvas>
    </div>

    <div class="card">
        <h2>4. 리뷰 요약</h2>
        <ul id="review-summary-list">
            <c:choose>
                <c:when test="${not empty positiveSummary or not empty negativeSummary}">
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
</div>

<script>
new Chart(document.getElementById('monthlyChart'), {
    type: 'bar',
    data: {
        labels: [<c:forEach var="item" items="${productStats}">"${item.month}",</c:forEach>],
        datasets: [{
            label: '매출(원)',
            backgroundColor: '#4e73df',
            data: [<c:forEach var="item" items="${productStats}">${item.revenue},</c:forEach>]
        }]
    }
});

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

</body>
</html>
