<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>펀딩 메뉴 선택</title>
</head>
<body>
	<h2>어떤 메뉴를 선택하시겠어요?</h2>
	<form id="menuForm" action="" onsubmit="return validateSelection();">
		<!-- 선택 결과를 담을 hidden input -->
		<input type="hidden" id="menuTypeInput" name="menuType" value="">

		<div class="funding-type-select">
			<button type="button" onclick="setMenuType('existing')">기존
				메뉴 선택<br>
			<span class="description">이미 등록된 메뉴를 선택하여 펀딩을 빠르게 시작할 수 있어요.
				이전에 입력한 상품 정보가 자동으로 불러와집니다.</span></button>
			<button type="button" onclick="setMenuType('new')">새로운 메뉴 등록<br>
			<span class="description">처음 등록하는 상품이라면 여기에 입력해 주세요. 상품명, 가격,
				설명 등을 직접 입력해야 합니다.</span></button>
		</div>

		<br>

		<!-- 이동 버튼 -->
		<button type="button" onclick="goBack()">뒤로가기</button>
		<button type="submit">다음</button>
	</form>
	

<script>
function setMenuType(type) {
	const form = document.getElementById("menuForm");
	const basePath = "${pageContext.request.contextPath}/seller/store/";
	if (type === "existing") {
		form.action = basePath + "create_existMenu";
	} else if (type === "new") {
		form.action = basePath + "create_newMenu";
	}
	document.getElementById("menuTypeInput").value = type;
}

function validateSelection() {
	const menuType = document.getElementById("menuTypeInput").value;
	if (!menuType) {
		alert("먼저 메뉴 유형을 선택해주세요.");
		return false;
	}
	return true;
}

function goBack() {
	window.history.back();
}
</script>	
</body>
</html>