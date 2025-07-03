<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/user/funding_detail.css">

<!-- Swiper 스타일과 JS -->
<link rel="stylesheet"
	href="https://unpkg.com/swiper/swiper-bundle.min.css" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/swiper@10/swiper-bundle.min.css" />
<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
  const isLoggedIn = ${not empty sessionScope.loginUser ? 'true' : 'false'};
</script>

<script>
	//funding 이미지 배열
	const fundingImages = [
	    <c:choose>
	      <c:when test="${empty funding.images}">
	         "${cpath}/resources/images/noimage.jpg"
	      </c:when>
	      <c:otherwise>
	        <c:forEach var="img" items="${funding.images}" varStatus="status">
	          "${cpath}${img.imageUrl}"<c:if test="${!status.last}">,</c:if>
	        </c:forEach>
	      </c:otherwise>
	    </c:choose>
  		];
	
	// product 이미지 배열
	const productImages = [
		<c:choose>
	      <c:when test="${empty product.images}">
	         "${cpath}/resources/images/noimage.jpg"
	      </c:when>
	      <c:otherwise>
	  	    <c:forEach var="img" items="${product.images}" varStatus="status">
	   	  	 "${cpath}${img.imageUrl}"<c:if test="${!status.last}">,</c:if>
	  	    </c:forEach>
	   	  </c:otherwise>
	    </c:choose>
	];
	
	// 범용 이미지 슬라이더 함수
	function createImageSlider(images, imgSelector, prevBtnSelector, nextBtnSelector, dotSelector) {
	  let currentIndex = 0;
	
	  function showImage(index) {
	    if (index < 0) currentIndex = images.length - 1;
	    else if (index >= images.length) currentIndex = 0;
	    else currentIndex = index;
	
	    $(imgSelector).attr("src", images[currentIndex]);
	    
	    if(dotSelector) {
	      $(dotSelector).css("color", "#ccc");
	      $(dotSelector).eq(currentIndex).css("color", "#ff6600");
	    }
	  }
	
	  // 초기 표시
	  showImage(0);
	
	  // 버튼 이벤트
	  $(prevBtnSelector).on("click", () => showImage(currentIndex - 1));
	  $(nextBtnSelector).on("click", () => showImage(currentIndex + 1));
	
	  // dot 클릭 이벤트(옵션)
	  if(dotSelector) {
	    $(dotSelector).on("click", function() {
	      const idx = $(this).data("index");
	      showImage(idx);
	    });
	  }
	}

	function formatDate(timestamp) {
		  const date = new Date(timestamp);
		  return date.toLocaleDateString("ko-KR", {
		    year: "numeric",
		    month: "2-digit",
		    day: "2-digit",
		  });
		}
	
	$(document).ready(function() {
	  createImageSlider(fundingImages, "#fundingMainImage", "#fundingPrevBtn", "#fundingNextBtn", ".funding-dot");
	  createImageSlider(productImages, "#productMainImage", "#productPrevBtn", "#productNextBtn");
	});
	//구매 개수, 총 가격 증가 감소
	$(function () {
		const salePrice = parseInt("${funding.salePrice}");
	    const perQty = parseInt("${funding.perQty}");
	    const maxQty = parseInt("${funding.maxQty}");
	    const currentQty = parseInt("${funding.currentQty}");

	    const maxBuyable = Math.min(perQty, maxQty - currentQty);

	    function updateTotal(qty) {
	      const total = salePrice * qty;
	      $("#totalPrice").text(total.toLocaleString());
	    }

	    $(".plus").click(function () {
	      let qty = parseInt($("#quantity").val());
	      if (qty < maxBuyable) {
	        qty++;
	        $("#quantity").val(qty);
	        updateTotal(qty);
	      } else {
	        alert("최대 구매 가능 수량은 " + maxBuyable + "개입니다.");
	      }
	    });

	    $(".minus").click(function () {
	      let qty = parseInt($("#quantity").val());
	      qty = qty > 1 ? qty - 1 : 1;
	      $("#quantity").val(qty);
	      updateTotal(qty);
	    });
	  });
	//구매하기 결제 창 이동
	$(function () {
		  $(".buy-button").click(function () {
			  if (!isLoggedIn) {
			      alert("로그인 후 이용 가능합니다.");
			      location.href = "${cpath}/auth/login"; 
			      return;
			    }
		    const quantity = $("#quantity").val();
		    const totalPrice = $("#totalPrice").text().replace(/,/g, ""); // 쉼표 제거

		    $("#hiddenQuantity").val(quantity);
		    $("#hiddenTotalPrice").val(totalPrice);

		    $("#paymentForm").submit();
		  });
		});
	
	  $(function () {
		  $(".tab-btn").click(function () {
		    const tab = $(this).data("tab");

		    $(".tab-btn").removeClass("active-tab");
		    $(this).addClass("active-tab");

		    if (tab === "desc") {
		      $("#desc-tab").show();
		      $("#review-tab").hide();
		    } else if (tab === "review") {
		      $("#desc-tab").hide();
		      $("#review-tab").show();
		    }
		  });

		  // 페이지 로드시 초기 상태
		  $("#desc-tab").show();
		  $("#review-tab").hide();
		});
	  
	  //페이지 처리
	  function loadReviewPage(fundingId, page) {
		  $.ajax({
		    url: `${cpath}/fundings/${fundingId}/reviews`,
		    method: "GET",
		    data: { page: page },
		    success: function (data) {
		      const reviewList = data.reviewlist;
		      const currentPage = data.currentPage;
		      const totalPages = data.totalPages;
			  
		      let reviewHtml = '';
		      reviewList.forEach((review, index) => {
		        reviewHtml += `
		          <div class="review-card" data-index="\${index}">
		            <div class="review-body">
		              <div class="review-left">
		                <div class="review-user">
		                  <span class="user-icon">👤</span>
		                  <strong>\${review.name}</strong>
		                  <span class="review-date">\${formatDate(review.createdAt)}</span>
		                </div>
		                <div class="review-rating">
		                  \${[1, 2, 3, 4, 5].map(i =>
		                    `<span class="star \${i <= review.rating ? 'filled' : ''}">★</span>`).join('')}
		                </div>
		                <div class="review-content">\${review.content}</div>
		              </div>
		              
		              \${review.images && review.images.length > 0 ? `
		            	        <div class="review-image">
		            	          <img src="${cpath}\${review.images[0].imageUrl}" alt="리뷰 이미지" />
		            	        </div>
		            	      ` : ''}
		            </div>
		          </div>
		        `;
		      });

		      
		      $("#review-tab").html(reviewHtml);

		      // 페이징 HTML 다시 그리기
		      let paginationHtml = `<div class="pagination">`;
		      for (let i = 1; i <= totalPages; i++) {
		        if (i === currentPage) {
		          paginationHtml += `<button class="page-link active" disabled>\${i}</button>`;
		        } else {
		          paginationHtml += `<button class="page-link" data-page="\${i}">\${i}</button>`;
		        }
		      }
		      $("#review-tab").append(`<div class="pagination-container" style="text-align: center; margin-top: 20px;">\${paginationHtml}</div>`);
		    },
		    error: function () {
		      alert("리뷰 데이터를 불러오는데 실패했습니다.");
		    }
		  });
		}

		// 페이지 로딩 후 리뷰 탭 전환 시 첫 페이지 자동 로드
		$(document).on("click", ".tab-btn[data-tab='review']", function () {
		  const fundingId = "${funding.fundingId}";
		  loadReviewPage(fundingId, 1);
		});

		// 동적으로 생성된 페이징 버튼 클릭 시
		$(document).on("click", ".page-link", function (e) {
		  e.preventDefault();
		  const page = $(this).data("page");
		  const fundingId = "${funding.fundingId}";
		  console.log("페이지 클릭됨:", page);
		  loadReviewPage(fundingId, page);
		});
</script>

<p class="category">Home / ${store.categoryName}</p>
<div class="product-detail-container">
	
	<!-- funding 이미지 슬라이더 -->
	<div class="image-carousel">
		<img id="fundingMainImage" src="" alt="펀딩 이미지"
			style="width: 90%; height: 80%; object-fit: cover; border-radius: 20px; margin-top: 40px" />
		<div id="fundingControls"
			style="text-align: center; margin-top: 10px;">
			<div class="dot-wrapper">
				<c:forEach var="img" items="${funding.images}" varStatus="status">
					<span class="dot funding-dot" data-index="${status.index}">●</span>
				</c:forEach>
			</div>
		</div>
	</div>

	<!-- 상품 정보 -->
	<div class="product-info">
		<a class="store-name">${store.storeName}></a>
		<p class="funding-desc">${funding.fundingName}</p>

		<div class="avg-rating">
			<span style="color: #FF9670;">★</span>
			<fmt:formatNumber value="${avgRating}" type="number"
				maxFractionDigits="1" />
			<span>{${reviewCount}}</span>
		</div>

		<div class="price">
			<c:set var="original" value="${product.price}" />
			<c:set var="sale" value="${funding.salePrice}" />
			<c:set var="discount" value="${(1 - (sale / original)) * 100}" />
			<span class="discount"><fmt:formatNumber value="${discount}"
					type="number" maxFractionDigits="0" />%</span>
			<del class="product-price">
				<fmt:formatNumber value="${original}" type="number" />
				원
			</del>
			<br> <strong class="sale-price"><fmt:formatNumber
					value="${sale}" type="number" />원</strong>
		</div>

		<div>
			<p class="date">
				<span class="label-text">남은 기간</span><br>
				<c:set var="today" value="<%=new java.util.Date()%>" />
				<c:set var="remaining"
					value="${(funding.endDate.time - today.time) / (1000*60*60*24)}" />
				<c:choose>
					<c:when test="${remaining <= 0}">
						<span class="remaining-day">종료됨</span>
					</c:when>
					<c:otherwise>
						<span class="remaining-day"> <fmt:formatNumber
								value="${remaining}" type="number" maxFractionDigits="0" /> 일
						</span>
					</c:otherwise>
				</c:choose>
				<br> <span class="period">${funding.startDate}~${funding.endDate}</span>
			</p>
		</div>

		<div class="funding-contents">
			<p>달성률</p>
			<c:set var="percent"
				value="${(funding.currentQty * 100.0) / funding.targetQty}" />
			<fmt:formatNumber value="${percent}" type="number"
				maxFractionDigits="0" var="percentInt" />

			<div class="funding-progress-box">
				<span>${percentInt}%</span>
			</div>
			<div class="funding-bar">
				<div class="funding-bar-inner" style="width: ${percentInt}%;"></div>
			</div>
		</div>
		<hr class="divider" />
		<div class="price-row">
			<div class="quantity-control">
				<button class="qty-btn minus">-</button>
				<input type="text" id="quantity" value="1" readonly />
				<button class="qty-btn plus">+</button>
			</div>

			<p class="total-price">
				총 가격 <br> <span class="total-amount"> <span
					id="totalPrice"><fmt:formatNumber
							value="${funding.salePrice}" type="number" /></span><span class="won">원</span>
				</span>
			</p>
		</div>
		<form id="paymentForm" action="${cpath}/order" method="get">
			<input type="hidden" name="fundingId" value="${funding.fundingId}" />
			<input type="hidden" name="quantity" id="hiddenQuantity" /> <input
				type="hidden" name="totalPrice" id="hiddenTotalPrice" />
			<button type="button" class="buy-button">구매하기</button>
		</form>
	</div>
</div>

<!-- 탭 메뉴 -->
<div class="tab-menu">
	<button class="tab-btn active-tab" data-tab="desc">펀딩 상세설명</button>
	<button class="tab-btn" data-tab="review">리뷰(${reviewCount})</button>
</div>

<!-- 콘텐츠 영역 -->
<div id="tab-content">
	<!-- 설명 탭 영역 -->
	<div id="desc-tab">
		<pre class="product-desc">${funding.fundingDesc}</pre>


		<div class="product-image-carousel"
			style="width: 45%; height: 300px; position: relative; margin-top: 20px;">
			<img id="productMainImage" src="" alt="상품 이미지"
				style="width: 100%; height: 100%; object-fit: cover; border-radius: 15px;" />
			<button id="productPrevBtn" class="nav-btn"
				style="position: absolute; top: 50%; left: 10px; transform: translateY(-50%);">&#x276E;</button>
			<button id="productNextBtn" class="nav-btn"
				style="position: absolute; top: 50%; right: 10px; transform: translateY(-50%);">&#x276F;</button>
		</div>

		<div class="hashtags">
			<c:forEach var="tag" items="${taglist}">#${tag} </c:forEach>
		</div>
	</div>

	<!-- 리뷰 탭 영역 -->
	<div id="review-tab" style="display: none;"></div>