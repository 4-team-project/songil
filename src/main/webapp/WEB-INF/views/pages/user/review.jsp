<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>리뷰 작성</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/review-form.css" />
</head>
<body>
	<div class="review-box">
		<div class="review-box-header">
			<h2>리뷰 작성</h2>
			<button class="review-close-btn" onclick="openConfirmModal()">✕</button>
		</div>
		<hr>


		<div class="info">
			<p>
				<strong>펀딩명 </strong>
				<c:out value="${fundingDTO.fundingName}" default="-" />
			</p>
			<p>
				<strong>사용 날짜 </strong>
				<c:choose>
					<c:when test="${couponDTO.usedAt == null}">미사용 쿠폰</c:when>
					<c:otherwise>
						<fmt:formatDate value="${couponDTO.usedAt}"
							pattern="yyyy.MM.dd HH:mm" />
					</c:otherwise>
				</c:choose>
			</p>
		</div>
		<form id="reviewForm" method="post" enctype="multipart/form-data"
			action="${pageContext.request.contextPath}/review">
			<input type="hidden" name="productId" value="${fundingDTO.productId}" />
			<input type="hidden" name="userId" value="${couponDTO.userId}" /> <label>별점</label>
			<div class="star-rating">
				<span data-value="1">★</span><span data-value="2">★</span><span
					data-value="3">★</span><span data-value="4">★</span><span
					data-value="5">★</span>
			</div>
			<input type="hidden" name="rating" id="rating" required /> <label
				for="content">리뷰 내용</label>
			<textarea name="content" id="content" placeholder="리뷰를 입력하세요."
				required></textarea>
			<label for="images" class="custom-file-upload">사진 선택</label> <input
				type="file" id="images" name="images" multiple accept="image/*"
				onchange="handleFiles(this.files)" />
			<div id="preview-container"
				style="margin-top: 10px; display: flex; flex-wrap: wrap; gap: 10px;"></div>
			<button type="submit" class="submit-btn">리뷰 등록</button>
		</form>
	</div>
	<!-- 리쥬 작성 종료 모달 -->
	<div class="modal-overlay" id="confirmModal">
		<div class="modal-dialog">
			<div class="modal-header">
				<span>리뷰 작성</span>
				<button class="close-btn" onclick="closeConfirmModal()">✕</button>
			</div>
			<hr />
			<div class="modal-content">
				<p class="modal-title">리뷰 작성을 취소하시겠습니까?</p>
				<p class="modal-desc">작성된 내용은 저장되지 않습니다.</p>
			</div>
			<div class="modal-buttons">
				<button class="modal-cancel" onclick="closeConfirmModal()">닫기</button>
				<button class="modal-confirm" onclick="exitReview()">리뷰 계속
					쓰기</button>
			</div>
		</div>
	</div>

	<!-- 등록 성공 모달 -->
	<div class="modal-overlay" id="successModal">
		<div class="modal-dialog">
			<div class="modal-header">
				<span>리뷰 작성</span>
				<button class="close-btn" onclick="closeConfirmModal()">✕</button>
			</div>
			<hr />
			<div class="modal-content">
				<p class="modal-title" style="font-size: 18px; color: #ff7f50;">리뷰가
					등록되었습니다!</p>
				<p class="modal-desc" style="font-size: 14px; margin-top: 4px;">
					작성하신 리뷰를 보시려면 리뷰 보러 가기를 눌러주세요</p>
			</div>
			<div class="modal-buttons">
				<button class="modal-cancel" onclick="closeSuccessModal()">닫기</button>
				<button class="modal-confirm" onclick="goToReviewPage()">리뷰
					보러 가기</button>
			</div>
		</div>
	</div>


	<script>
const contextPath = "${pageContext.request.contextPath}";
const imageLimit = 5;
const imageMap = new Map();
function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
    const r = Math.random() * 16 | 0, v = c === 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}
const stars = document.querySelectorAll('.star-rating span');
const ratingInput = document.getElementById('rating');
let selectedValue = 0;
stars.forEach(star => {
  const value = parseInt(star.getAttribute('data-value'), 10);
  star.addEventListener('click', () => {
    selectedValue = value;
    ratingInput.value = selectedValue;
    updateStars(selectedValue);
  });
  star.addEventListener('mouseover', () => updateStars(value));
  star.addEventListener('mouseout', () => updateStars(selectedValue));
});
function updateStars(value) {
  stars.forEach(star => {
    const starValue = parseInt(star.getAttribute('data-value'), 10);
    star.classList.toggle('selected', starValue <= value);
  });
}
function handleFiles(files) {
  const previewContainer = document.getElementById('preview-container');
  if (imageMap.size + files.length > imageLimit) {
    alert("이미지는 최대 " + imageLimit + "장까지 업로드가 가능합니다.");
    return;
  }
  [...files].forEach(file => {
    if (!file.type.startsWith("image/") || file.size > 5 * 1024 * 1024) {
      alert("유효하지 않은 이미지입니다.");
      return;
    }
    const uuid = generateUUID();
    imageMap.set(uuid, file);
    const reader = new FileReader();
    reader.onload = e => {
      const img = document.createElement('img');
      img.src = e.target.result;
      const btn = document.createElement('button');
      btn.textContent = '×';
      btn.type = 'button';
      btn.onclick = () => removeImage(uuid, btn);
      const wrapper = document.createElement('div');
      wrapper.className = 'preview-image';
      wrapper.appendChild(img);
      wrapper.appendChild(btn);
      previewContainer.appendChild(wrapper);
    };
    reader.readAsDataURL(file);
  });
  document.getElementById('images').value = '';
}
function removeImage(uuid, btn) {
  imageMap.delete(uuid);
  btn.parentElement.remove();
}
function openConfirmModal() {
  document.getElementById('confirmModal').style.display = 'flex';
}
function closeConfirmModal() {
  document.getElementById('confirmModal').style.display = 'none';
}
function exitReview() {
  window.history.back();
}
document.getElementById("reviewForm").addEventListener("submit", async function (e) {
  e.preventDefault();
  const form = e.target;
  let imageUrls = [];
  for (const file of imageMap.values()) {
    const formData = new FormData();
    formData.append("file", file);
    const response = await fetch(contextPath + "/image/upload", {
      method: "POST",
      body: formData
    });
    const url = await response.text();
    imageUrls.push(url);
  }
  const json = {
    userId: parseInt(form.userId.value),
    productId: parseInt(form.productId.value),
    rating: parseInt(form.rating.value),
    content: form.content.value,
    imageUrls: imageUrls
  };
  const result = await fetch(contextPath + "/review/submit", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(json)
  });
  if (result.ok) {
    document.getElementById('successModal').style.display = 'flex';
  } else {
    alert("리뷰 등록 실패");
  }
});

function closeSuccessModal() {
	  document.getElementById('successModal').style.display = 'none';
	}
	function goToReviewPage() {
	  //리뷰 페이지 경로로 수정 필요.
	  window.location.href = contextPath + "/user/reviews";
	}

</script>
</body>
</html>
