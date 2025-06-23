<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/home.css">

<div class="content-box">
	<%@ include file="/WEB-INF/views/common/categoryBar.jsp"%>

	<div class="main-contents">
		<div class="head-box">
			<p>
				세연님 <span>추천 펀딩</span>
			</p>

			<select id="sidoSelect">
				<option disabled selected>시/도 선택</option>
			</select> <select id="sigunguSelect">
				<option disabled selected>시/군/구 선택</option>
			</select>

			<script src="${cpath}/resources/js/region.js"></script>
		</div>

		<!-- 추천 펀딩 영역 -->
		<div class="funding-list-wrapper">
			<c:forEach var="funding" items="${fundinglist}" begin="0" end="3">
				<c:set var="percent"
					value="${(funding.currentQty * 100.0) / funding.targetQty}" />
				<fmt:formatNumber value="${percent}" type="number"
					maxFractionDigits="0" var="percentInt" />

				<div class="funding-box">
					<c:choose>
						<c:when test="${not empty funding.images}">
							<img class="funding-recommend-image"
								src="${cpath}${funding.images[0].imageUrl}" alt="펀딩 이미지" />
						</c:when>
						<c:otherwise>
							<div class="funding-recommend-image"></div>
						</c:otherwise>
					</c:choose>

					<div class="funding-recommend-contents">
						<div class="funding-title">${funding.fundingName}</div>
						<div class="funding-progress-box">
							<div class="funding-progress-text-box">
								<div class="funding-progress-text">${percentInt}%</div>
							</div>
							<div class="funding-date-box">
								<div class="funding-date">${daysLeftMap[funding.fundingId]}일</div>
								<div class="funding-date-text">남음</div>
							</div>
>>>>>>> 5e80769 (✨ add  main-page UI)
						</div>

						<div class="funding-bar">
							<div class="funding-bar-inner" style="width: ${percentInt}%;"></div>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>

		<!-- 구분선 -->
		<div class="main-bar">
			<div class="main-bar-text">현재 진행 중인 펀딩</div>
		</div>

		<!-- 전체 펀딩 목록 영역 -->
		<div class="funding-filter-box">
			<div class="funding-filter active">
				<div class="funding-filter-text">인기순</div>
			</div>
			<div class="funding-filter">
				<div class="funding-filter-text">최신순</div>
			</div>
			<div class="funding-filter">
				<div class="funding-filter-text">마감 임박 순</div>
			</div>
		</div>
		<script>
	document.querySelectorAll('.funding-filter').forEach((el) => {
		el.addEventListener('click', () => {
			document.querySelectorAll('.funding-filter').forEach(f => f.classList.remove('active'));
			el.classList.add('active');
		});
	});
</script>
>>>>>>> 5e80769 (✨ add  main-page UI)

		<div class="funding-list-wrapper">
			<c:forEach var="funding" items="${fundinglist}" begin="0" end="7">
				<c:set var="percent"
					value="${(funding.currentQty * 100.0) / funding.targetQty}" />
				<fmt:formatNumber value="${percent}" type="number"
					maxFractionDigits="0" var="percentInt" />

				<div class="funding-box">
					<c:choose>
						<c:when test="${not empty funding.images}">
							<img class="funding-image"
								src="${cpath}${funding.images[0].imageUrl}" alt="펀딩 이미지" />
						</c:when>
						<c:otherwise>
							<div class="funding-image"></div>
						</c:otherwise>
					</c:choose>

					<div class="funding-contents">
						<div class="funding-place">${funding.storeName}</div>
						<div class="funding-title">${funding.fundingName}</div>
						<div class="rating-box">
							<img class="rating-img"
								src="${cpath}/resources/images/icons/rating.svg" alt="rating">
							<div class="rating-text">4.5</div>
							<div class="review-text">(123)</div>
						</div>

						<div class="percent-box">
							<div class="percent-text">20%</div>
							<div class="regular-price-text">
								<fmt:formatNumber value="${funding.price}" type="number"
									groupingUsed="true" />
							</div>
						</div>
						<div class="price-text">
							<fmt:formatNumber value="${funding.salePrice}" type="number"
								groupingUsed="true" />
							원
						</div>


						<div class="funding-progress-box">
							<div class="funding-progress-text-box">
								<div class="funding-progress-text">${percentInt}%</div>
							</div>
							<div class="funding-date-box">
								<div class="funding-date">${daysLeftMap[funding.fundingId]}일</div>
								<div class="funding-date-text">남음</div>
							</div>
						</div>

						<div class="funding-bar">
							<div class="funding-bar-inner" style="width: ${percentInt}%;"></div>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
		<div class="add-button">
			<div class="add-button-text">더보기</div>
		</div>
	</div>
</div>
