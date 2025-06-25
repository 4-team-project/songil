<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/components/searchBox.css">
</head>
<body>
<div class="search-box">
	<input class="search-text" id="searchText" type="text" placeholder="검색하기">
	<div class="search-button" onclick="sendSearchData()">
		<div class="search-button-circle">
			<img class="search-icon"
				src="${cpath}/resources/images/icons/search.svg"
				alt="search">
		</div>
	</div>
</div>
</body>
</html>

<script>
  const cpath = '${pageContext.request.contextPath}';
</script>

<script>
function sendSearchData() {
	  const searchText = document.getElementById("searchText").value.replace(/\s+/g, "");

	  if (searchText !== "") {
	    const encodedSearch = encodeURIComponent(searchText);
	    const url = cpath + "/fundings/ajax?search=" + encodedSearch;
	    console.log(url);
	    window.location.href = url;
	  }
	}

</script>
