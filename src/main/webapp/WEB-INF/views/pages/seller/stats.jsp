<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
.main-content {
	flex: 1;
	padding: 40px;
	overflow-y: auto;
	box-sizing: border-box;
	background-color: white;
	display: flex;
	flex-direction: column;
}

h1 {
	text-align: left;
	font-size: 2em;
	margin-bottom: 40px;
	color: #333;
	font-weight: 700;
}

.stats-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 12px;
}

.stats-grid canvas {
	width: 100% !important;
	height: 260px !important;
	display: block;
}

.card {
	border: 2px solid #fdbfa8;
	background-color: #fff8f5;
	border-radius: 10px;
	padding: 20px;
	box-sizing: border-box;
}

.card h2 {
	font-size: 1.5em;
	color: #f97c5d;
	margin-bottom: 10px;
	font-weight: 600;
}

.full-width {
	grid-column: 1/-1;
}

.stats-grid canvas {
	width: 100% !important;
	height: 260px !important;
	display: block;
}

canvas {
	width: 100% !important;
	height: auto !important;
	max-height: 250px;
	display: block;
	margin: 0 auto;
}

.repurchase-list {
	padding-left: 20px;
	font-size: 14px;
}

.repurchase-list li {
	margin-bottom: 10px;
}

.repurchase-list strong {
	color: #333;
	font-size: 14px;
	font-weight: 500;
}

.tips {
	background-color: #fff1ec;
	border-left: 5px solid #f97c5d;
	padding: 15px;
	margin-top: 30px;
	border-radius: 8px;
	width: 100%;
	box-sizing: border-box;
	font-size: 13px;
	color: #444;
}

.icon {
	width: 30px;
	height: 30px;
	vertical-align: middle;
	margin-right: 6px;
}

.highlight {
	color: #f97c5d;
}
</style>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<div class="main-content">
	<h1>
		<img src="${cpath}/resources/images/icons/solar_star-bold.svg"
			alt="상점 통계" class="icon" />
		<c:out value="${userDTO.nickname}" default="딱쿠" />
		사장님의 <span class="highlight"><c:out
				value="${storeDTO.storeName}" default="상점" /> 상점 통계</span>
	</h1>

	<div class="stats-grid">
		<!-- 1. 월별 주문 및 매출 -->
		<div class="card full-width">
			<h2>1. 월별 주문 및 매출</h2>
			<canvas id="orderChart"></canvas>
		</div>

		<!-- 2. 인기 상품 -->
		<div class="card">
			<h2>2. 인기 상품 Top 5</h2>
			<canvas id="popularProductChart"></canvas>
		</div>

		<!-- 3. 태그별 주문 수 -->
		<div class="card">
			<h2>3. 태그별 주문 수</h2>
			<canvas id="tagStatsChart"></canvas>
		</div>

		<!-- 4. 재구매 상품 -->
		<div class="card full-width">
			<h2>4. 재구매 Top 5</h2>
			<p style="font-size: 15px; color: gray;">(재구매 횟수 기준)</p>
			<ol class="repurchase-list">
				<c:forEach var="item" items="${topRePurchased}">
					<li><strong style="font-size: 18px;">${item.productName}</strong>
						<span style="color: gray; font-size: 18px;">(${item.rePurchaseCount}회)</span>
					</li>
				</c:forEach>
				<c:if test="${empty topRePurchased}">
					<li>재구매 상품 정보가 없습니다.</li>
				</c:if>
			</ol>
		</div>
	</div>
</div>
<script>
    // 1. 월별 주문/매출
    new Chart(document.getElementById('orderChart'), {
        type: 'bar',
        data: {
            labels: [<c:forEach var="stat" items="${orderStats}">"${stat.month}",</c:forEach>],
            datasets: [
                {
                    label: '주문 수',
                    data: [<c:forEach var="stat" items="${orderStats}">${stat.orderCount},</c:forEach>],
                    backgroundColor: 'rgba(54, 162, 235, 0.7)',
                    yAxisID: 'y'
                },
                {
                    type: 'line',
                    label: '매출 (원)',
                    data: [<c:forEach var="stat" items="${orderStats}">${stat.revenue},</c:forEach>],
                    borderColor: '#f97c5d',
                    backgroundColor: 'rgba(249, 124, 93, 0.2)',
                    borderWidth: 2,
                    tension: 0.4,
                    yAxisID: 'y1'
                }
            ]
        },
        options: {
            responsive: true,
            interaction: {
                mode: 'index',
                intersect: false
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: { display: true, text: '주문 수', font: { size: 20 } },
                    ticks: { font: { size: 20 } }
                },
                y1: {
                    beginAtZero: true,
                    position: 'right',
                    grid: { drawOnChartArea: false },
                    title: { display: true, text: '매출 (원)', font: { size: 20 } },
                    ticks: { font: { size: 20 } }
                },
                x: {
                    ticks: { font: { size: 20 } }
                }
            },
            plugins: {
                tooltip: {
                    bodyFont: { size: 20 },
                    titleFont: { size: 20 },
                    callbacks: {
                        label: function(context) {
                            let label = context.dataset.label || '';
                            let value = context.parsed.y;
                            return label.includes('매출') ? `${label}: ${value.toLocaleString()}원` : `${label}: ${value}건`;
                        }
                    }
                },
                legend: {
                    labels: { font: { size: 20 } }
                }
            }
        }
    });

    // 2. 인기 상품 Pie
    new Chart(document.getElementById('popularProductChart'), {
        type: 'pie',
        data: {
            labels: [<c:forEach var="p" items="${popularProducts}">"${p.label}",</c:forEach>],
            datasets: [{
                data: [<c:forEach var="p" items="${popularProducts}">${p.value},</c:forEach>],
                backgroundColor: [
                    '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF',
                    '#FF9F40', '#C9CBCF', '#8E44AD', '#2ECC71', '#E67E22'
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                tooltip: {
                    bodyFont: { size: 20 },
                    titleFont: { size: 20 }
                },
                legend: {
                    labels: { font: { size: 20 } }
                }
            }
        }
    });

    // 3. 태그별 주문 수 - Doughnut
    new Chart(document.getElementById('tagStatsChart'), {
        type: 'doughnut',
        data: {
            labels: [<c:forEach var="tag" items="${tagStats}">"${tag.label}",</c:forEach>],
            datasets: [{
                data: [<c:forEach var="tag" items="${tagStats}">${tag.value},</c:forEach>],
                backgroundColor: [
                    '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF',
                    '#FF9F40', '#C9CBCF', '#8E44AD', '#2ECC71', '#E67E22'
                ]
            }]
        },
        options: {
            responsive: true,
            cutout: '60%',
            plugins: {
                tooltip: {
                    bodyFont: { size: 20 },
                    titleFont: { size: 20 }
                },
                legend: {
                    labels: { font: { size: 20 } }
                }
            }
        }
    });
</script>
