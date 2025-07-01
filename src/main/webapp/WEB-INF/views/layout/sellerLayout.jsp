<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<html>
<head>
<title><tiles:getAsString name="title" /></title>
</head>
<body>
	<div id="header">
		<tiles:insertAttribute name="header" />
	</div>
	<div class="seller-container">
		<%@ include file="/WEB-INF/views/common/sellerSideBar.jsp"%>
		<div id="body">
			<tiles:insertAttribute name="body" />
		</div>
	</div>
	<div id="footer">
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>