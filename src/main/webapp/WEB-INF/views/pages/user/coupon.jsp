<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${cpath}/resources/css/my_coupon_page.css" />
<link rel="stylesheet" href="${cpath}/resources/css/review-form.css" />
<link rel="stylesheet" href="${cpath}/resources/css/coupon_detail.css" />
<script>const cpath = "${pageContext.request.contextPath}";</script>

<div class="coupon-container">
  <div class="sidebar">
    <div class="menu-title active" data-tab="unused">사용 가능한 쿠폰</div>
    <div class="menu-sub" data-tab="used">사용한 쿠폰</div>
  </div>

  <div class="main-content">
    <div class="tab-search-bar">
      <div id="tabs" class="tabs"></div>
      <div class="search-wrapper">
        <%@ include file="/WEB-INF/views/common/searchBox.jsp"%>
      </div>
    </div>

    <!-- 사용 가능한 쿠폰 -->
    <div class="coupon-list unused-coupons">
      <c:forEach var="coupon" items="${coupons}">
        <c:if test="${coupon.useStatus == '미사용'}">
          <c:set var="funding" value="${fundingMap[coupon.fundingId]}" />
          <c:set var="product" value="${productMap[funding.productId]}" />
          <c:set var="store" value="${storeMap[funding.storeId]}" />
          <c:set var="price" value="${product.price}" />
          <c:set var="sale" value="${funding.salePrice}" />
          <c:set var="discountRate" value="${(price - sale) * 100 / price}" />
          <c:set var="discountRateInt" value="${discountRate - (discountRate % 1)}" />

          <div class="coupon-card" data-status="${coupon.useStatus}"
               data-expired-at="<fmt:formatDate value='${coupon.expiredAt}' pattern='yyyy-MM-dd'/>"
               data-reviewed="${coupon.reviewed}">
            <div class="coupon-left">
              <div class="sale">
                <fmt:formatNumber value="${discountRateInt}" type="number" maxFractionDigits="0" />%
              </div>
            </div>

            <div class="coupon-middle">
              <div class="store">${store.sido}${store.sigungu}${store.dong}</div>
              <div class="title"><strong class="coupon-title">${store.storeName}</strong></div>
              <div class="name"><strong>${funding.fundingName}</strong></div>
              <div class="desc">
                <c:choose>
                  <c:when test="${fn:length(funding.fundingDesc) > 40}">
                    ${fn:substring(funding.fundingDesc, 0, 40)}...
                    <span class="more"
                          onclick="goToCouponDetail('${cpath}/user/coupon/detail', '${coupon.couponId}')"
                          style="color: #FF9670; cursor: pointer;">더보기</span>
                  </c:when>
                  <c:otherwise>
                    ${funding.fundingDesc}
                  </c:otherwise>
                </c:choose>
              </div>
            </div>

            <div class="coupon-right">
              <input type="hidden" name="couponId" value="${coupon.couponId}" />
              <button class="btn-show-qr" data-coupon-id="${coupon.couponId}">
                <span>QR 보기</span>
              </button>
              <div class="coupon-date">
                ~ <fmt:formatDate value="${coupon.expiredAt}" pattern="yyyy.MM.dd" />
              </div>
            </div>
          </div>
        </c:if>
      </c:forEach>
    </div>

    <!-- 사용한 쿠폰 -->
    <div class="coupon-list used-coupons" style="display: none;">
      <c:forEach var="coupon" items="${coupons}">
        <c:if test="${coupon.useStatus == '사용'}">
          <c:set var="funding" value="${fundingMap[coupon.fundingId]}" />
          <c:set var="product" value="${productMap[funding.productId]}" />
          <c:set var="store" value="${storeMap[funding.storeId]}" />

          <div class="coupon-card" data-status="${coupon.useStatus}" data-reviewed="${coupon.reviewed}">
            <div class="coupon-left">
              <div class="usedAt">
                <br>
                <fmt:formatDate value="${coupon.usedAt}" pattern="yyyy-MM-dd" />
                <br>
                <div class="use">사용</div>
                <br>
              </div>
            </div>

            <div class="coupon-middle">
              <div class="store">${store.sido}${store.sigungu}${store.dong}</div>
              <div class="title"><strong class="coupon-title">${store.storeName}</strong></div>
              <div class="name"><strong>${funding.fundingName}</strong></div>
              <div class="desc">${funding.fundingDesc}</div>
            </div>

            <div class="coupon-right">
              <c:choose>
                <c:when test="${coupon.reviewed == 1}">
                  <button class="use-btn used-btn"><span class="btn-word">사용완료</span></button>
                </c:when>
                <c:otherwise>
                  <button class="use-btn used-btn"><span class="btn-word">사용완료</span></button>
                  <button type="button" class="review-btn"
                          onclick="openReviewModal('${cpath}/review/write/${coupon.couponId}')">
                    <span class="btn-word">리뷰쓰기</span>
                  </button>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </c:if>
      </c:forEach>
    </div>

    <div class="pagination"></div>
  </div>
</div>

<!-- 모달 영역 -->
<div id="modalBackdrop" class="modal-backdrop" style="display: none;"></div>
<div id="couponModal" class="modal-container" style="display: none;">
  <div id="modalContent" class="modal-content"></div>
</div>

<script>
$(document).ready(function () {
  $(".sidebar div").on("click", function () {
    $(".sidebar div").removeClass("active");
    $(this).addClass("active");

    const tab = $(this).data("tab");

    if (tab === "unused") {
      $(".unused-coupons").show();
      $(".used-coupons").hide();
    } else if (tab === "used") {
      $(".unused-coupons").hide();
      $(".used-coupons").show();
    }
  });

  $(document).on("click", ".btn-show-qr", function () {
    const couponId = $(this).data("coupon-id");

    $.ajax({
      url: `${cpath}/user/coupon/qr`,
      data: { couponId },
      type: "GET",
      success: function (html) {
        $("#modalContent").html(html);
        $("#modalBackdrop, #couponModal").fadeIn();
      },
      error: function () {
        alert("QR 쿠폰 정보를 불러오지 못했습니다.");
      }
    });
  });

  $(document).on("click", "#modalBackdrop", function () {
    $("#modalBackdrop, #couponModal").fadeOut();
  });
});
</script>