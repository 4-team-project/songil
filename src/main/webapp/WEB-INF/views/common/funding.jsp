<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/home.css">


<div class="funding-list-wrapper">
	<c:forEach var="funding" items="${fundinglist}" begin="0" end="7">
		<c:choose>
			<c:when test="${funding.targetQty > 0}">
				<c:set var="percent"
					value="${(funding.currentQty * 100.0) / funding.targetQty}" />
			</c:when>
			<c:otherwise>
				<c:set var="percent" value="0" />
			</c:otherwise>
		</c:choose>

		<fmt:formatNumber value="${percent}" type="number"
			maxFractionDigits="0" var="percentInt" />
		<fmt:formatNumber
			value="${((funding.price - funding.salePrice) * 100.0) / funding.price}"
			type="number" maxFractionDigits="0" var="discountPercent" />

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
					<div class="percent-text">${discountPercent}%</div>
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

<script>
document.addEventListener("DOMContentLoaded", function () {
  const sortBoxes = document.querySelectorAll(".funding-filter");
  const fundingListWrapper = document.querySelectorAll(".funding-list-wrapper")[1]; // 두 번째

  function handleSortClick(box) {
    sortBoxes.forEach(b => b.classList.remove("selected"));
    box.classList.add("selected");

    const sortId = box.dataset.sortId;
    if (!sortId) {
      console.error("sortId 누락됨");
      return;
    }

    const url = `\${cpath}/fundings/ajax?sort=\${sortId}`;
    console.log("요청 URL:", url);

    fetch(url)
      .then(res => {
        if (!res.ok) throw new Error("서버 응답 오류");
        return res.text();
      })
      .then(html => {
        fundingListWrapper.innerHTML = html;
      })
      .catch(err => console.error("펀딩 정렬 실패:", err));
  }

  sortBoxes.forEach(box => {
    box.addEventListener("click", () => handleSortClick(box));
  });

  const defaultSelected = document.querySelector('.funding-filter.selected');
  if (defaultSelected) {
    handleSortClick(defaultSelected);
  }
});
</script>
