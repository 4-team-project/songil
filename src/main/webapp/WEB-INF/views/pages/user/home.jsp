<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/home.css">

<div class="content-box">
	<%@ include file="/WEB-INF/views/common/categoryBar.jsp"%>

	<div class="main-contents">
		<p>
			세연님 <span style="color: #FF9670;">추천 펀딩</span>
		</p>

		<div class="funding-list-wrapper">
			<c:forEach var="funding" items="${fundinglist}" begin="0" end="3">
				<c:set var="percent"
					value="${(funding.currentQty * 100.0) / funding.targetQty}" />
				<fmt:formatNumber value="${percent}" type="number"
					maxFractionDigits="0" var="percentInt" />

				<div class="funding-recommend-box">
					<c:choose>
						<c:when test="${not empty funding.images}">
							<img class="funding-recommend-image"
								src="${cpath}${funding.images[0].imageUrl}" alt="펀딩 이미지" />
						</c:when>
						<c:otherwise>
							<div class="funding-recommend-image"></div>
						</c:otherwise>
					</c:choose>

					<div class="funding-contents">
						<div class="funding-title">${funding.fundingName}</div>
						<div class="funding-title">${funding.fundingDesc}</div>

						<div class="funding-progress-box">
							<span>${percentInt}%</span>
							<p>${daysLeftMap[funding.fundingId]}일남음</p>
						</div>

						<div class="funding-bar">
							<div class="funding-bar-inner" style="width: ${percentInt}%;"></div>
						</div>
					</div>
				</div>
			</c:forEach>

		</div>
	</div>


</div>
