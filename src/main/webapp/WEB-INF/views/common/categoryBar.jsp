<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/components/categoryBar.css">
</head>
<body>

	<div class="category-bar">
		<div class="title" id="toggleImg" role="button" tabindex="0">
			<div class="category-text">카테고리</div>
			<div class="title-img">
				<img src="${cpath}/resources/images/icons/drop-down.svg"
					alt="drop-down" />
			</div>
		</div>

		<div class="category-box-list" id="categoryList">
			<div
				class="category-box ${param.categoryId == 0 || empty param.categoryId ? 'selected' : ''}"
				data-category-id="0">
				<img src="${cpath}/resources/images/logo.svg" alt="전체">
				<div class="category-text">전체</div>
			</div>
			<div class="category-box" data-category-id="1">
				<img src="${cpath}/resources/images/category/korean-food.svg"
					alt="한식">
				<div class="category-text">한식</div>
			</div>
			<div class="category-box" data-category-id="2">
				<img src="${cpath}/resources/images/category/tteokbokki.svg"
					alt="분식">
				<div class="category-text">분식</div>
			</div>
			<div class="category-box" data-category-id="3">
				<img src="${cpath}/resources/images/category/dumpling.svg" alt="중식">
				<div class="category-text">중식</div>
			</div>
			<div class="category-box" data-category-id="4">
				<img src="${cpath}/resources/images/category/sushi.svg" alt="일식">
				<div class="category-text">일식</div>
			</div>
			<div class="category-box" data-category-id="5">
				<img src="${cpath}/resources/images/category/steak.svg" alt="양식">
				<div class="category-text">양식</div>
			</div>
			<div class="category-box" data-category-id="6">
				<img src="${cpath}/resources/images/category/rice-noodles.svg"
					alt="아시안">
				<div class="category-text">아시안</div>
			</div>
			<div class="category-box" data-category-id="7">
				<img src="${cpath}/resources/images/category/hamburger.svg"
					alt="패스트푸드">
				<div class="category-text">패스트푸드</div>
			</div>
			<div class="category-box" data-category-id="8">
				<img src="${cpath}/resources/images/category/cake.svg" alt="카페&디저트">
				<div class="category-text">카페&디저트</div>
			</div>
			<div class="category-box" data-category-id="9">
				<img src="${cpath}/resources/images/category/lunch-box.svg"
					alt="도시락">
				<div class="category-text">도시락</div>
			</div>
		</div>
	</div>

	<script>
document.addEventListener("DOMContentLoaded", function () {
  const cpath = '${cpath}';
  const categoryBoxes = document.querySelectorAll(".category-box");
  const fundingListWrapper = document.querySelector(".main-contents");

  const toggleDiv = document.getElementById("toggleImg");
  const img = toggleDiv?.querySelector("img");
  const categoryList = document.getElementById("categoryList");
  const defaultSrc = `${cpath}/resources/images/icons/drop-down.svg`;
  const toggledSrc = `${cpath}/resources/images/icons/drop-up.svg`;

  let toggled = false;
  if (img && categoryList) {
    img.src = defaultSrc;
    categoryList.style.display = "none";

    toggleDiv.addEventListener("click", () => {
      toggled = !toggled;
      img.src = toggled ? toggledSrc : defaultSrc;
      categoryList.style.display = toggled ? "flex" : "none";
    });
  }

  function handleCategoryClick(box) {
    document.querySelectorAll(".category-box").forEach(b => b.classList.remove("selected"));
    box.classList.add("selected");

    const categoryId = parseInt(box.dataset.categoryId, 10);
    if (isNaN(categoryId)) {
      return;
    }
    
    const url = cpath + "/fundings/ajax?categoryId=" + categoryId;

    fetch(url)
      .then(res => {
        if (!res.ok) throw new Error("서버 응답 오류");
        return res.text();
      })
      .then(html => {
        fundingListWrapper.innerHTML = html;
      })
      .catch(err => console.error("카테고리 펀딩 가져오기 실패:", err));
  }

  categoryBoxes.forEach(box => {
    box.addEventListener("click", () => handleCategoryClick(box));
  });
});
</script>


</body>
</html>
