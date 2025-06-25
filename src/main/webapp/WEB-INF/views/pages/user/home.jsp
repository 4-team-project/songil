<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/home.css">
<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/components/regionDropdown.css">

<div class="content-box">
	<%@ include file="/WEB-INF/views/common/categoryBar.jsp"%>

	<div class="main-contents">
		<div class="head-box">
			<p>
				세연님 <span>추천 펀딩</span>
			</p>
			<div class="dropdown-box">
				<div class="dropdown">
					<button class="dropbtn" id="sidoButton"
						onclick="toggleDropdown('sido')">시/도 선택</button>
					<div class="dropdown-content" id="sidoDropdown"></div>
				</div>

				<div class="dropdown">
					<button class="dropbtn" id="sigunguButton"
						onclick="toggleDropdown('sigungu')">시/군/구 선택</button>
					<div class="dropdown-content" id="sigunguDropdown"></div>
				</div>
				<div class="find-btn disabled" id="findBtn" onclick="sendRegionData()">찾기</div>
			</div>
		</div>

		<!-- 추천 펀딩 영역 -->
		<div class="funding-list-wrapper">
			<c:forEach var="funding" items="${fundinglist}" begin="0" end="3">
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
		<div class="funding-list-wrapper">
			<%@ include file="/WEB-INF/views/common/funding.jsp"%>
		</div>
		<div class="add-button" id="moreButton" data-category-id="0">
			<div class="add-button-text">더보기</div>
		</div>
	</div>
</div>


<script>
const moreButton = document.getElementById("moreButton");

moreButton.addEventListener("click", () => {
  const categoryId = moreButton.dataset.categoryId;

  const url = `\${cpath}/fundings/ajax?categoryId=\${categoryId}`;
  console.log("더보기 요청 URL:", url);

  fetch(url)
    .then(res => {
      if (!res.ok) throw new Error("서버 오류");
      return res.text();
    })
    .then(html => {
    	const fundingListWrapper = document.querySelector(".main-contents");
    	fundingListWrapper.innerHTML = html;
    })
    .catch(err => console.error("더보기 펀딩 가져오기 실패:", err));
});

</script>
<script>
  const cpath = '${pageContext.request.contextPath}';
</script>
<script src="${cpath}/resources/js/region.js"></script>

