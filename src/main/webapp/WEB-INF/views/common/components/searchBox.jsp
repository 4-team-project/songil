<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="searchBox">
	<input class="searchText" type="text" placeholder="검색하기">
	<div class="searchButton">
		<div class="searchButtonCircle">
			<img class="searchIcon"
				src="${pageContext.request.contextPath}/resources/images/icons/search.svg"
				alt="search">
		</div>
	</div>
</div>
