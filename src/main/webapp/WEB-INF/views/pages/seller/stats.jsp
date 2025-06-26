<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>상점 통계</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
body {
	font-family: 'Noto Sans KR', sans-serif;
	background-color: #f9f9f9;
	margin: 30px;
}

h1 {
	text-align: center;
	margin-bottom: 40px;
}

.grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr); /* 3열 고정 */
	gap: 30px;
	max-width: 1200px;
	margin: 0 auto;
}

.card {
	background: white;
	padding: 20px;
	border-radius: 16px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
}

.card.full-width {
	grid-column: span 3; /* 1행 전체 차지 */
}

canvas {
	width: 100% !important;
	height: auto !important;
}

.card h2 {
	font-size: 18px;
	margin-bottom: 15px;
}

.reRate {
	font-weight: bold;
	font-size: 16px;
	text-align: center;
	margin-bottom: 10px;
}
</style>
</head>
<body>

	<h1>📊 상점 통계 대시보드</h1>

	<div class="grid">
		<!-- 1. 월별 주문 및 매출 (한 줄 전체) -->
		<div class="card full-width">
			<h2>1. 월별 주문 및 매출</h2>
			<canvas id="orderChart"></canvas>
		</div>

		<!-- 2. 인기 상품 -->
		<div class="card">
			<h2>2. 인기 상품 Top 5</h2>
			<canvas id="popularProductChart"></canvas>
		</div>

		<!-- 3. 재구매 Top 5 -->
		<div class="card">
			<h2>3. 재구매 Top 5 상품</h2>
			<p style="color: gray; font-size: 14px;">(재구매 횟수)</p>
			<ol style="padding-left: 20px; font-size: 16px;">
				<c:forEach var="item" items="${topRePurchased}" varStatus="status">
					<li style="margin-bottom: 10px;"><strong>${item.productName}</strong>
						<span style="color: gray; font-size: 14px;">
							(${item.rePurchaseCount}회)</span></li>
				</c:forEach>
				<c:if test="${empty topRePurchased}">
					<li>재구매 상품 정보가 없습니다.</li>
				</c:if>
			</ol>
		</div>

		<!-- 4. 태그별 주문 통계 -->
		<div class="card">
			<h2>4. 태그별 주문 수</h2>
			<canvas id="tagStatsChart"></canvas>
		</div>
	</div>


	<script>
    // 1. 월별 주문/매출 - Line Chart
new Chart(document.getElementById('orderChart'), {
    type: 'bar',
    data: {
        labels: [<c:forEach var="stat" items="${orderStats}">"${stat.month}",</c:forEach>],
        datasets: [
            {
                type: 'bar',
                label: '주문 수',
                data: [<c:forEach var="stat" items="${orderStats}">${stat.orderCount},</c:forEach>],
                backgroundColor: 'rgba(54, 162, 235, 0.7)',
                yAxisID: 'y'
            },
            {
                type: 'line',
                label: '매출 (원)',
                data: [<c:forEach var="stat" items="${orderStats}">${stat.revenue},</c:forEach>],
                borderColor: 'rgba(255, 99, 132, 0.9)',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderWidth: 2,
                fill: true,
                tension: 0.4,
                yAxisID: 'y1',
                pointRadius: 3,
                pointHoverRadius: 6
            }
        ]
    },
    options: {
        responsive: true,
        interaction: {
            mode: 'index',
            intersect: false
        },
        animation: {
            duration: 1800,
            easing: 'easeOutQuart'
        },
        scales: {
            y: {
                beginAtZero: true,
                position: 'left',
                title: {
                    display: true,
                    text: '주문 수'
                }
            },
            y1: {
                beginAtZero: true,
                position: 'right',
                grid: {
                    drawOnChartArea: false
                },
                title: {
                    display: true,
                    text: '매출 (원)'
                }
            }
        },
        plugins: {
            legend: {
                position: 'top'
            },
            tooltip: {
                mode: 'index',
                intersect: false,
                callbacks: {
                    label: function(context) {
                        let label = context.dataset.label || '';
                        let value = context.parsed.y;
                        if (label === '매출 (원)') {
                            return label + ': ' + value.toLocaleString() + '원';
                        } else {
                            return label + ': ' + value + '건';
                        }
                    }
                }
            }
        }
    }
});


    // 2. 인기 상품 - Pie Chart
    new Chart(document.getElementById('popularProductChart'), {
        type: 'pie',
        data: {
            labels: [<c:forEach var="p" items="${popularProducts}">"${p.label}",</c:forEach>],
            datasets: [{
                data: [<c:forEach var="p" items="${popularProducts}">${p.value},</c:forEach>],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.7)',
                    'rgba(54, 162, 235, 0.7)',
                    'rgba(255, 206, 86, 0.7)',
                    'rgba(75, 192, 192, 0.7)',
                    'rgba(153, 102, 255, 0.7)'
                ]
            }]
        },
        options: {
            responsive: true
        }
    });

    // 4. 태그별 통계 - Doughnut Chart
    new Chart(document.getElementById('tagStatsChart'), {
        type: 'doughnut',
        data: {
            labels: [<c:forEach var="tag" items="${tagStats}">"${tag.label}",</c:forEach>],
            datasets: [{
                data: [<c:forEach var="tag" items="${tagStats}">${tag.value},</c:forEach>],
                backgroundColor: [
                    'rgba(255, 159, 64, 0.7)',
                    'rgba(54, 162, 235, 0.7)',
                    'rgba(255, 99, 132, 0.7)',
                    'rgba(153, 102, 255, 0.7)',
                    'rgba(75, 192, 192, 0.7)'
                ]
            }]
        },
        options: {
            responsive: true,
            cutout: '60%'
        }
    });
</script>
</body>
</html>
