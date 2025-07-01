<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>펀딩 상세 통계</h1>

    <c:if test="${not empty funding}">
        <p><strong>펀딩명:</strong> ${funding.fundingName}</p>
        <p><strong>판매가:</strong> <fmt:formatNumber value="${funding.salePrice}" type="number" groupingUsed="true" />원</p>
        <p><strong>현재 판매 수량:</strong> <fmt:formatNumber value="${funding.currentQty}" type="number" groupingUsed="true" />개</p>
        <p><strong>목표 수량:</strong> <fmt:formatNumber value="${funding.targetQty}" type="number" groupingUsed="true" />개</p>

        <p><strong>현재까지 모인 총 금액:</strong> 
            <fmt:formatNumber value="${funding.salePrice * funding.currentQty}" 
                              type="number" 
                              groupingUsed="true" />원
        </p>

        <c:set var="rate" value="${(funding.currentQty / funding.targetQty) * 100}" />
        <p><strong>달성률:</strong> 
            <fmt:formatNumber value="${rate}" maxFractionDigits="2" />%
        </p>

        <p><strong>펀딩 상태:</strong> ${funding.status}</p>
        <p><strong>시작일:</strong> <fmt:formatDate value="${funding.startDate}" pattern="yyyy년 MM월 dd일" /></p>
        <p><strong>종료일:</strong> <fmt:formatDate value="${funding.endDate}" pattern="yyyy년 MM월 dd일" /></p>

        <div class="info-box">
            <p><strong>현재 날짜:</strong> <fmt:formatDate value="${today}" pattern="yyyy년 MM월 dd일" /></p>
            
            <c:choose>
                <c:when test="${remainingDays != null}">
                    <p><strong>펀딩 종료까지:</strong> 
                        <span class="highlight">${fundingStatusMessage}</span>
                    </p>
                </c:when>
                <c:otherwise>
                    <p><strong>펀딩 종료일 정보:</strong> 종료일 정보가 유효하지 않습니다.</p>
                </c:otherwise>
            </c:choose>
        </div>

    </c:if>
    <c:if test="${empty funding}">
        <p>펀딩 정보를 찾을 수 없습니다.</p>
    </c:if>

    <div class="action-buttons">
        <button type="button" onclick="location.href='/seller/detail?fundingId=${funding.fundingId}'">펀딩 상세보기</button>
        <button type="button" onclick="window.history.back()" class="secondary">펀딩 목록으로 돌아가기</button>
    </div>

</body>
</html>