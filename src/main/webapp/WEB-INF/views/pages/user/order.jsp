<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<script src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/order.css">

<script>
	$(function() {
		const fundingId = ${funding.fundingId};
		const quantity = ${quantity};
		const totalPrice = 10;

		const IMP = window.IMP;
		IMP.init("imp22234788"); // 본인의 가맹점 식별코드로 바꾸세요

		$(".buy-button").click(function(e) {
			e.preventDefault();

			IMP.request_pay({
				pg : "html5_inicis", // PG사
				pay_method : "card",
				merchant_uid : "order_" + new Date().getTime(),
				name : "funding.fundingName",
				amount : totalPrice,
				buyer_email : "shinhan@takku.com",
				buyer_name : "${loginUser.name}",
				buyer_tel : "${loginUser.phone}"
			}, function(rsp) {
				if (rsp.success) {
					// POST 방식으로 결제 정보 서버로 전송
					const form = $('<form>', {
						method : 'post',
						action : '${cpath}/order/payment'
					});

					form.append($('<input>', {
						type : 'hidden',
						name : 'fundingId',
						value : fundingId
					}));
					form.append($('<input>', {
						type : 'hidden',
						name : 'quantity',
						value : quantity
					}));
					form.append($('<input>', {
						type : 'hidden',
						name : 'totalPrice',
						value : totalPrice
					}));
					form.append($('<input>', {
						type : 'hidden',
						name : 'imp_uid',
						value : rsp.imp_uid
					}));
					form.append($('<input>', {
						type : 'hidden',
						name : 'merchant_uid',
						value : rsp.merchant_uid
					}));

					$('body').append(form);
					form.submit();
				} else {
					alert("결제에 실패했습니다: " + rsp.error_msg);
				}
			});
		});
	});
</script>

<div class="order-container">
	<div class="order-left">
		<div class="info">
			<h3>펀딩 상품 정보</h3>
			<div class="product-info">
				<div class="product-image">
					<img src="${funding.thumbnailImageUrl}" alt="펀딩 이미지" />
				</div>
				<div class="product-detail">
					<span class="store-name">${store.storeName}</span><br>
					<p class="funding-name">${funding.fundingName}</p>
					<p>수량 ${quantity}개</p>
					<p class="price">
						<fmt:formatNumber value="${totalPrice}" type="number" />
						원
					</p>
				</div>
			</div>
		</div>

		<div class="info">
			<h3>구매자 정보</h3>
			<p>${loginUser.name}</p>
			<p>${loginUser.phone}</p>
			<p>${loginUser.birth}</p>
		</div>
	</div>

	<div class="order-right">
		<div class="info">
			<h3>
				최종 결제 금액 <span class="right-price"> <fmt:formatNumber
						value="${totalPrice}" type="number" /> 원
				</span>
			</h3>
			<p class="small-text">
				펀딩이 정해진 기간 내 100% 달성되면, 쿠폰이 발급되어 사용하실 수 있습니다.<br> 펀딩이 무산되거나 중단될
				경우, 결제 금액은 자동으로 전액 환불됩니다.
			</p>
			<button type="submit" class="buy-button">결제하기</button>
		</div>
	</div>
</div>
