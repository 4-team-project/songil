<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/pages/seller/createFunding_main.css">

<h3>어떤 펀딩을 만들고 싶으신가요?</h3>
<form id="fundingForm"
	action="${pageContext.request.contextPath}/seller/fundings/create-step2"
	onsubmit="return validateSelection();">
	<input type="hidden" id="fundingTypeInput" name="type" value="">

	<div class="funding-type-select">
		<!-- 버튼 클릭 시 hidden input 값 설정 -->
		<button type="button" id="btnLimited" onclick="selectFundingType('limited')">
			<p>한정 상품 펀딩</p><br>
			<span class="description">딱쿠에서만 만나볼 수 있는 메뉴에 대한 펀딩이에요</span>
		</button>
		<button type="button" id="btnGeneral" onclick="selectFundingType('general')">
			<p>일반 펀딩</p><br>
			<span class="description">오프라인 매장과 딱쿠 모두에서 판매되는 메뉴에 대한 펀딩이에요</span>
		</button>
	</div>

	<!-- submit 버튼 -->
	<button type="submit" class = "btn-next">다음</button>
</form>

<script>
	function selectFundingType(type) {
		// 버튼 클릭 시 hidden input 값 설정
		document.getElementById('fundingTypeInput').value = type;
	}

	function validateSelection() {
		const selected = document.getElementById('fundingTypeInput').value;
		if (!selected) {
			alert("펀딩 종류를 선택해주세요.");
			return false;
		}
		return true;
	}
</script>
