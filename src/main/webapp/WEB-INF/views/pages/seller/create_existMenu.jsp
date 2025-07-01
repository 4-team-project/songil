<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>


	<h1>한정 상품 펀딩</h1>
<form action="${pageContext.request.contextPath}/seller/create-step3" method="post">
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
		<div class="menu-label" name = "fundingName">해당 메뉴를 얼마에 판매할지 입력해주세요</div>
		<input type="number" id="menuPrice" placeholder="판매가 " name="salePrice" required /><br> <span
			id="discountRate">할인율은 %입니다.</span>
	</div>

	<!-- 펀딩성공기준 -->
	<div class="form-group">
		<label for="minSales">원하시는 최소 판매 개수를 입력해주세요.(펀딩 성공 기준)</label>
		<div class="description">예: 30개를 목표로 하면, 30개가 팔려야 펀딩이 성공합니다.</div>
		<input type="number" id="minSales" name=targetQty
			placeholder="최소 판매 개수 입력" required/><br>
		<span id="minPrice">펀딩 성공을 위한 최소 금액은 <span id="amount"></span>원입니다.</span>
	</div>

	<!-- 판매 가능한 최대 개수 -->
	<div class="form-group">
		<label for="maxSales">펀딩 이벤트로 판매 가능한 최대 개수를 입력해 주세요.</label>
		<div class="description">예: 50개가 가능하면, 50개 판매시 사용자가 펀딩 참여 불가능.</div>
		<input type="number" id="maxSales" name="maxQty"
			placeholder="최대 판매 개수 입력" required/>
	</div>

	<!-- 한 사람이 구매할 수 있는 펀딩 개수 -->
	<div class="form-group">
		<label for="maxPerUser">한 사람이 최대 몇 개까지 살 수 있는지 정해주세요.</label>
		<div class="description">예: 1명당 2개까지 구매 가능</div>
		<input type="number" id="maxPerUser" name="perQty"
			placeholder="인당 구매 가능 개수 입력" required/>
	</div>
	
  <button type="submit">다음</button>
</form>
<button type="submit" onclick="goBack()">이전</button>


<script>
//메뉴명
$(document).ready(function() {
  $('#menuSelect').on('focus', function() {
    const storeId = ${store.storeId}; 

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
      },
      error: function() {
        alert('상품 목록을 불러오지 못했습니다.');
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
      dataType: 'json',
      success: function(product) {
        console.log("받은 상품 정보:", product);
        if (product && product.price != null) {
          $('#menuPrice').attr('placeholder', product.price);
          $('#menuPrice').val('');
        } else {
          $('#menuPrice').attr('placeholder', '가격 정보를 불러오지 못했습니다.');
          $('#menuPrice').val('');
        }
      },
      error: function() {
        alert('상품정보를 불러오지 못했습니다.');
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
      dataType: 'json',
      success: function(product) {
        if (product && product.price != null) {
          basePrice = product.price;
          $('#menuPrice').attr('placeholder', basePrice);
          $('#menuPrice').val('');
          $('#discountRate').text('할인율은 %입니다.');
        }
      },
      error: function() {
        alert('상품 정보를 불러오지 못했습니다.');
      }
    });
  });

// 판매가 입력 시 할인율 계산
$('#menuPrice').on('input', function() {
  const sellingPrice = Number($(this).val());

  if (basePrice && sellingPrice > 0 && sellingPrice <= basePrice) {
    let discount = ((1 - (sellingPrice / basePrice)) * 100);
    discount = discount.toFixed(1); // 소수점 1자리까지

     $('#discountRate').text(`할인율은 \${discount}%입니다.`);
   } else if (sellingPrice > basePrice) {
     $('#discountRate').text('판매가는 정가보다 클 수 없습니다.');
   }
  });
});

//펀딩 성공을 위한 최소 금액은 =원입니다.
$(function() {
	  // 기본 초기화 (초기 텍스트 세팅)
	  function updateMinPrice() {
	    const baseAmount = Number($('#menuPrice').val()); 
	    const minSales = Number($('#minSales').val()); 
	    const minAmount = baseAmount * minSales;

	    if (minAmount > 0) {
	      $('#amount').text(minAmount.toLocaleString());
	    } else {
	      $('#amount').text('0');
	    }
	  }

	  // menuPrice와 minSales 입력값 변경 시마다 실행
	  $('#menuPrice, #minSales').on('input', updateMinPrice);

	  // 페이지 로딩 시 초기화
	  updateMinPrice();
	});
	
	
function goBack() {
  window.history.back();
}
</script>

