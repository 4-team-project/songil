<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/pages/seller/createFunding_exist_normal.css">

<form action="${pageContext.request.contextPath}/seller/fundings/create-step3" method="get">
	<div class="menuName">
		<div class="menu-label">펀딩할 메뉴를 선택해주세요</div>
		<div class="menu-select">

			<select id="menuSelect" name="productId" required>
				<option value="" disabled selected>메뉴를 선택해주세요.</option>
			</select>

			<button type="button" class="btn-edit">정보 수정</button>
			<button type="button" class="btn-add">메뉴 추가</button>
		</div>
		<div id="menu-list"></div>
	</div>

	<!-- 메뉴의 정가 입력 -->
	<div class="menuPrice">
		<div class="menu-label">해당 메뉴를 얼마에 판매할지 입력해주세요</div>
		<input type="number" id="menuPrice" placeholder="판매가 " required />&nbsp개<br> <span
			id="discountRate">할인율은 %입니다.</span>
	</div>

	<!-- 판매 가능한 최대 개수 -->
	<div class="form-group">
		<label for="maxSales">펀딩 이벤트로 판매 가능한 최대 개수를 입력해 주세요.</label>
		<div class="description">예: 50개가 가능하면, 50개 판매시 사용자가 펀딩 참여 불가능.</div>
		<input type="number" id="maxSales" name="maxSales"
			placeholder="최대 판매 개수 입력" required/>&nbsp개
	</div>

	<!-- 한 사람이 구매할 수 있는 펀딩 개수 -->
	<div class="form-group">
		<label for="maxPerUser">한 사람이 최대 몇 개까지 살 수 있는지 정해주세요.</label>
		<div class="description">예: 1명당 2개까지 구매 가능</div>
		<input type="number" id="maxPerUser" name="maxPerUser"
			placeholder="인당 구매 가능 개수 입력" required/>&nbsp개
	</div>

	<div class="btn-container">
		<button class="btn" type="button" onclick="location.href='${pageContext.request.contextPath}/seller/fundings/create-step1'">이전</button>
		<button class="btn" type="submit">다음</button>
	</div>
</form>



<script>
$(document).ready(function() {
  $('#menuSelect').on('focus', function() {
    const storeId = ${store.storeId}; // 상점ID 고정값

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
});

//가격
$(document).ready(function() {
  $('#menuSelect').on('change', function() {
    const productId = $(this).val();
    $.ajax({
      url: '${pageContext.request.contextPath}/seller/product/info',
      method: 'GET',
      data: { productId: productId },
      success: function(product) {
        console.log("받은 상품 정보:", product);
        if (product && product.price != null) {
          $('#menuPrice').attr('placeholder', product.price);
          $('#menuPrice').val('');
        } 
      }
    });
  });
});

//할인율
$(document).ready(function() {
  let basePrice = null; // 정가 (기본 가격)

  $('#menuSelect').on('change', function() {
    const productId = $(this).val();

    $.ajax({
      url: '${pageContext.request.contextPath}/seller/product/info',
      method: 'GET',
      data: { productId: productId },
      success: function(product) {
        if (product && product.price != null) {
          basePrice = product.price;
          $('#menuPrice').attr('placeholder', basePrice);
          $('#menuPrice').val('');
          $('#discountRate').text('할인율은 %입니다.');
        }
      }
    });
  });

// 판매가 입력 시 할인율 계산
$('#menuPrice').on('input', function() {
  const sellingPrice = Number($(this).val());

  if (basePrice && sellingPrice > 0 && sellingPrice <= basePrice) {
    let discount = ((1 - (sellingPrice / basePrice)) * 100);
    discount = discount.toFixed(1); 

     $('#discountRate').text(`할인율은 \${discount}%입니다.`);
   } else if (sellingPrice > basePrice) {
     $('#discountRate').text('판매가는 정가보다 클 수 없습니다.');
   }
  });
});
</script>

