<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/settlements.css">

<div
	style="display: flex; align-items: center; gap: 20px; padding-left: 20px; margin-top: 20px;">
	<img src="${cpath}/resources/images/settlement.svg" alt="settlement" />
	<h2 style="margin: 0;">사장님의 정산 현황을 확인하세요.</h2>
</div>

<div class="settlement-box">
	<c:forEach var="settlement" items="${settlementlist}">
		<div class="settlement-card">
			<div class="settlement-inner">
				<!-- 이미지 -->
				<div class="settlement-img">
					<c:choose>
						<c:when test="${not empty settlement.funding.images}">
							<img src="${cpath}${settlement.funding.images[0].imageUrl}"
								alt="펀딩 이미지" />
						</c:when>
						<c:otherwise>
							<img src="${cpath}/resources/images/noimage.jpg" alt="기본 이미지" />
						</c:otherwise>
					</c:choose>
				</div>

				<!-- 정보 -->
				<div class="settlement-info">
					<p>
						<strong>펀딩 이름 : </strong> ${settlement.funding.fundingName}
					</p>
					<p>
						<strong>정산 금액 : </strong>
						<fmt:formatNumber value="${settlement.amount}" type="number" />
						원
					</p>
					<p>
						<strong>수수료 : </strong>
						<fmt:formatNumber value="${settlement.fee}" type="number" />
						원
					</p>
					<p>
						<strong>정산 상태 : </strong> ${settlement.status}
					</p>
					<p>
						<strong>정산 날짜 : </strong>
						<c:choose>
							<c:when test="${not empty settlement.settledAt}">
								<fmt:formatDate value="${settlement.settledAt}"
									pattern="yyyy-MM-dd" />
							</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</p>
				</div>

				<!-- 상세보기 버튼 -->
				<div class="settlement-action">
					<form action="${cpath}/seller/store/stats" method="get"
						style="margin-left: auto;">
						<input type="hidden" name="fundingId"
							value="${settlement.fundingId}" />
						<button type="submit">상세보기</button>
					</form>
				</div>
			</div>
		</div>
	</c:forEach>
</div>