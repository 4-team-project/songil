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
		<div class="title" id="toggleImg">
			<div class="category-text">카테고리</div>
			<div class="title-img">
				<img src="${cpath}/resources/images/icons/drop-down.svg"
					alt="drop-down" />
			</div>
		</div>
		<div class="category-box-list" id="categoryList">
			<div class="category-box">
				<img src="${cpath}/resources/images/category/korean-food.svg"
					alt="korean-food" />
				<div class="category-text">한식</div>
			</div>
			<div class="category-box">
				<img src="${cpath}/resources/images/category/tteokbokki.svg"
					alt="tteokbokki" />
				<div class="category-text">분식</div>
			</div>
			<div class="category-box">
				<img src="${cpath}/resources/images/category/dumpling.svg"
					alt="dumpling" />
				<div class="category-text">중식</div>
			</div>
			<div class="category-box">
				<img src="${cpath}/resources/images/category/sushi.svg" alt="sushi" />
				<div class="category-text">일식</div>
			</div>
			<div class="category-box">
				<img src="${cpath}/resources/images/category/steak.svg" alt="steak" />
				<div class="category-text">양식</div>
			</div>
			<div class="category-box">
				<img src="${cpath}/resources/images/category/rice-noodles.svg"
					alt="rice-noodles" />
				<div class="category-text">아시안</div>
			</div>
			<div class="category-box">
				<img src="${cpath}/resources/images/category/hamburger.svg"
					alt="hamburger" />
				<div class="category-text">패스트푸드</div>
			</div>
			<div class="category-box">
				<img src="${cpath}/resources/images/category/cake.svg" alt="cake" />
				<div class="category-text">카페&디저트</div>
			</div>
			<div class="category-box">
				<img src="${cpath}/resources/images/category/lunch-box.svg"
					alt="lunch-box" />
				<div class="category-text">도시락</div>
			</div>
		</div>
	</div>

	<script>
		  const toggleDiv = document.getElementById("toggleImg");
		  const img = toggleDiv.querySelector("img"); 
		  const categoryList = document.getElementById("categoryList");
		
		  const defaultSrc = "${cpath}/resources/images/icons/drop-down.svg";
		  const toggledSrc = "${cpath}/resources/images/icons/drop-up.svg";   
		
		  let toggled = false;
		
		  img.src = defaultSrc;
		  categoryList.style.display = "none";
		
		  toggleDiv.addEventListener("click", () => {
		    toggled = !toggled;
		    img.src = toggled ? toggledSrc : defaultSrc;
		    categoryList.style.display = toggled ? "flex" : "none";
		  });
</script>

</body>
</html>
