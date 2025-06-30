<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/pages/seller/select_writetype.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	$(function() {
		$('.funding-type-box').on('click', function() {
			$('.funding-type-box').removeClass('selected');
			$(this).addClass('selected');
			$('#writeTypeInput').val($(this).data('type'));
		});

		$('#fundingForm').on('submit', function() {
			if (!$('#writeTypeInput').val()) {
				alert("작성 방식을 선택해주세요.");
				return false;
			}
		});

		$('#btnBack').on('click', function() {
			history.back();
		});
	});
</script>
<h1>현재 상점 : ${store.storeName}</h1>

<form id="fundingForm"
	action="${pageContext.request.contextPath}/seller/create-step5"
	method="post">
	<input type="hidden" id="writeTypeInput" name="type" value="">

	<h3>
		마지막으로, 불고기 정식에 대한 펀딩 제목과 펀딩 내용을 입력해야 합니다.<br>어떤 방식으로 작성할까요?
	</h3>

	<div class="funding-type-container">
		<div class="funding-type-box" id="btnLimited" data-type="ai">
			<h4>AI로 자동 생성</h4>
			<p>
				어떤 메뉴인지 간단히 알려주면,<br>AI가 알아서 펀딩 소개를 완성해요.
			</p>
		</div>
		<div class="funding-type-box" id="btnGeneral" data-type="directly">
			<h4>직접 작성할래요</h4>
			<p>
				펀딩 제목과 내용을 직접 입력해서<br>등록할 수 있어요.
			</p>
		</div>
	</div>

	<div class="btn-group">
		<button type="button" id="btnBack" class="nav-btn">이전</button>
		<button type="submit" class="nav-btn">다음</button>
	</div>
</form>