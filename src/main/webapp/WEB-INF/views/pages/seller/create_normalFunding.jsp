<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/pages/seller/createFunding_exist_normal.css">

<script>
let basePrice = null;

$(document).ready(function () {

  // 메뉴 목록 불러오기
$('#menuSelect').on('focus', function() {
    const storeId = ${storeDTO.storeId}; 

    $.ajax({
      url: '${pageContext.request.contextPath}/seller/product/list',
      method: 'GET',
      data: { storeId: storeId },
      success: function(productList) {
        $('#menuSelect').html('<option value="" disabled selected>메뉴를 선택해주세요.</option>');
        $.each(productList, function(i, product) {
          $('#menuSelect').append(
            $('<option></option>').val(product.productId).text(product.productName)
          );
        });
      }
    });
  });


  // 메뉴 선택 시 정가 불러오기 + basePrice 저장
  $('#menuSelect').on('change', function () {
    const productId = $(this).val();
    $.ajax({
      url: '${pageContext.request.contextPath}/seller/product/info',
      method: 'GET',
      data: { productId: productId },
      dataType: 'json',
      success: function (product) {
        if (product && product.price != null) {
          basePrice = product.price;
          $('#menuPrice').attr('placeholder', basePrice);
          $('#menuPrice').val('');
          $('#discountRate').text('할인율은 %입니다.');
          updateMinPrice(); // 정가 변경되면 최소금액도 다시 계산
        } else {
          basePrice = null;
          $('#menuPrice').attr('placeholder', '가격 정보를 불러오지 못했습니다.');
        }
      }
    });
  });

  // 할인율 계산
  $('#menuPrice').on('input', function () {
    const sellingPrice = Number($(this).val());

    if (basePrice && sellingPrice > 0 && sellingPrice <= basePrice) {
      let discount = ((1 - (sellingPrice / basePrice)) * 100).toFixed(1);
      $('#discountRate').text(`할인율은 \${discount}%입니다.`);
    } else if (sellingPrice > basePrice) {
      $('#discountRate').text('판매가는 정가보다 클 수 없습니다.');
    } else {
      $('#discountRate').text('할인율은 %입니다.');
    }
    updateMinPrice();
  });

  // 최소 펀딩 성공 금액 계산
  $('#minSales').on('input', updateMinPrice);

  function updateMinPrice() {
    const price = Number($('#menuPrice').val());
    const quantity = Number($('#minSales').val());

    if (price > 0 && quantity > 0) {
      const minAmount = price * quantity;
      $('#amount').text(minAmount.toLocaleString());
    } else {
      $('#amount').text('0');
    }
  }

  // 제출 시 정가보다 높은 경우 차단
  $('form').on('submit', function (e) {
    const sellingPrice = Number($('#menuPrice').val());
    if (basePrice && sellingPrice > basePrice) {
      e.preventDefault();
      $('#resultModal, #modalBackdrop').fadeIn();
    }
  });
  
  //모달 닫기 및 포커스 이동
  $('#closeModalBtn').on('click', function () {
    $('#resultModal, #modalBackdrop').fadeOut();
    $('#menuPrice').focus(); // 판매가 입력 칸으로 포커스 이동
  });
  
$(document).on('click', '#btn-edit', function(){
	const productId = $('#menuSelect').val();
	
	if(productId) {
		window.location.href='${pageContext.request.contextPath}/seller/product/' + productId + '/edit';
	}else {
		alert("수정할 메뉴를 선택해주세요");
	}
});
});

</script>

<div class="step-progress">
  <div class="step active">
    <div class="circle">1</div>
    <div class="label">상품 정보</div>
  </div>
  <div class="line"></div>
  <div class="step">
    <div class="circle">2</div>
    <div class="label">기간 및 이미지</div>
  </div>
  <div class="line"></div>
  <div class="step">
    <div class="circle">3</div>
    <div class="label">상세 내용</div>
  </div>
</div>

<form action="${pageContext.request.contextPath}/seller/fundings/create-step3" method="post">
	<div class="menuName">
		<div class="menu-label">펀딩할 메뉴를 선택해주세요.</div>
		<div class="menu-select">

			<select id="menuSelect" name="productId" required>
				<option value="" disabled selected>메뉴를 선택해주세요.</option>
			</select>

			<button type="button" class="btn-edit" id="btn-edit">정보 수정</button>
			<button type="button" class="btn-add" onclick="location.href='${pageContext.request.contextPath}/seller/product/new'">메뉴 추가</button>
		</div>
		<div id="menu-list"></div>
	</div>

	<!-- 메뉴의 정가 입력 -->
	<div class="menuPrice">
		<div class="menu-label" name="fundingName">해당 메뉴를 얼마에 판매할지 판매가를 입력해주세요.</div>
		<input type="number" id="menuPrice" placeholder="판매가 " name="salePrice" required class="form-input" /> <span class="unit-text">&nbsp원</span><br> <span
			id="discountRate">할인율은 %입니다.</span>
	</div>

	<!-- 펀딩성공기준 -->
	<div class="form-group">
		<div class="menu-label">원하시는 최소 판매 개수를 입력해주세요. (펀딩 성공 기준)</div>
		<div class="description">예: 30개를 목표로 하면, 30개가 팔려야 펀딩이 성공합니다.</div>
		<input type="number" id="minSales" name=targetQty
			placeholder="최소 판매 개수 입력" required class="form-input"/><span class="unit-text">&nbsp개</span><br>
		<span id="minPrice">펀딩 성공을 위한 최소 금액은 <span id="amount"></span>원입니다.</span>
	</div>

	<!-- 판매 가능한 최대 개수 -->
	<div class="form-group">
		<div class="menu-label">펀딩 이벤트로 판매 가능한 최대 개수를 입력해 주세요.</div>
		<div class="description">예: 50개가 가능하면, 50개 판매시 사용자가 펀딩 참여 불가능합니다.</div>
		<input type="number" id="maxSales" name="maxQty"
			placeholder="최대 판매 개수 입력" required class="form-input"/> <span class="unit-text">&nbsp개</span>
	</div>

	<!-- 한 사람이 구매할 수 있는 펀딩 개수 -->
	<div class="form-group">
		<div class="menu-label">한 사람이 최대 몇 개까지 살 수 있는지 정해주세요.</div>
		<div class="description">예: 1명당 2개까지 구매 가능합니다.</div>
		<input type="number" id="maxPerUser" name="perQty"
			placeholder="인당 구매 가능 개수 입력" required class="form-input"/><span class="unit-text">&nbsp&nbsp개</span>
	</div>


	<div class="btn-container">
		<button class="btn" type="button" onclick="location.href='${pageContext.request.contextPath}/seller/fundings/create-step1'">이전</button>
		<button class="btn" type="submit">다음</button>
	</div>

</form>

<!-- 모달 영역 -->
<div id="resultModal">
	<p id="modalMsg">판매가는 정가보다 높을 수 없습니다.</p>
	<button id="closeModalBtn">확인</button>
</div>
<!-- 모달 배경 -->
<div id="modalBackdrop"></div>