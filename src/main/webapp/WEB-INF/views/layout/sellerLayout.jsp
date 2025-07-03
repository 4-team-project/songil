<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<html>
<head>
<title><tiles:getAsString name="title" /></title>
</head>


<body class="layout-wrapper">
	<div id="header">
		<tiles:insertAttribute name="header" />
	</div>
	<div class="seller-container" style="cursor: url('${cpath}/resources/images/cursor.svg') 2 2, auto !important;">
		<%@ include file="/WEB-INF/views/common/sellerSideBar.jsp"%>
		<div id="sellerBody">
			<tiles:insertAttribute name="body" />
		</div>
	</div>
	<div id="footer">
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>