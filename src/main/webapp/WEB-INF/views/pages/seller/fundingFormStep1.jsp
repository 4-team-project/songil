<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<link rel="stylesheet" href="/resources/css/funding_edit.css" />
<c:set var="isNotReadyStatus"
	value="${empty tempFunding.status || tempFunding.status ne '준비중'}" />
<%-- 전체 페이지 컨테이너 (옵션: 중앙 정렬 및 여백 부여) --%>
<div class="page-container">
	<div class="edit-funding-container">
		<c:choose>
			<c:when test="${!isNotReadyStatus}">
				<h2>펀딩 정보 입력</h2>
			</c:when>
			<c:when test="${isNotReadyStatus}">
				<h2>펀딩 정보</h2>
			</c:when>
		</c:choose>
		<form action="/seller/store/edit/step1" method="post"
			enctype="multipart/form-data" class="funding-form">
			<input type="hidden" id="fundingId" name="fundingId"
				value="${tempFunding.fundingId}"> <input type="hidden"
				id="currentProcessingUserId" name="currentProcessingUserId"
				value="${currentProcessingUserId}">

			<div class="form-group">
				<c:choose>
					<c:when test="${isNotReadyStatus && tempFunding.status eq '진행중'}">
						<p class="disabled-message">
							<span class="fundingName">${tempFunding.fundingName} 펀딩</span>은
							지금 진행되고 있어요.<br>진행 중인 펀딩은 <span class="editordelete">수정이나
								삭제가 어렵습니다.</span>
						</p>
					</c:when>
					<c:when
						test="${isNotReadyStatus && tempFunding.status eq '성공' || tempFunding.status eq '실패'}">
						<p class="disabled-message">
							<span class="fundingName">${tempFunding.fundingName} 펀딩</span>은
							지금 종료 되었어요.<br>종료된 펀딩은 <span class="editordelete">수정이나
								삭제가 어렵습니다.</span>
						</p>
					</c:when>
					<c:otherwise>
						<p class="disabled-message">
							<span class="fundingName">${tempFunding.fundingName} 펀딩</span>은
							아직 펀딩이 시작 전입니다.<br>펀딩 시작 전까지는 <span class="editordelete">언제든지
								수정이나 삭제가 가능합니다!</span>
						</p>
					</c:otherwise>
				</c:choose>
				<label for="fundingName">펀딩 이름</label> <input type="text"
					id="fundingName" name="fundingName"
					value="${tempFunding.fundingName}" placeholder="펀딩 이름을 입력하세요"
					${isNotReadyStatus ? 'disabled' : ''}>
			</div>

			<div class="form-group">
				<label for="fundingType">펀딩 종류</label> <select id="fundingType"
					name="fundingType" ${isNotReadyStatus ? 'disabled' : ''}>
					<option value="한정"
						${tempFunding.fundingType eq '한정' ? 'selected' : ''}>한정
						펀딩</option>
					<option value="일반"
						${tempFunding.fundingType eq '일반' ? 'selected' : ''}>일반
						펀딩</option>
				</select>
			</div>

			<div class="form-group">
				<label for="fundingDesc">펀딩에 대한 설명</label>
				<textarea id="fundingDesc" name="fundingDesc" rows="15"
					placeholder="상품 설명은 비워도 괜찮아요.&#13;&#10;꼭 작성하지 않아도 등록할 수 있어요."
					${isNotReadyStatus ? 'disabled' : ''}>${tempFunding.fundingDesc}</textarea>
			</div>

			<
			<!-- 사진 추가 -->
			<div class="menuPicture">
				<div class="menu-label">펀딩 사진을 선택해 주세요.</div>
				<p>
					<strong>최대 2개까지</strong> 메뉴 사진을 넣어주세요! <strong>사진 추가하기</strong> 버튼을
					누르면 사진을 선택할 수 있어요. <br> 사진을 삭제하려면, 사진 밑에 있는 <strong>취소하기</strong>
					버튼을 눌러주세요.
				</p>

				<div class="pictureBtn">
					<button type="button" id="btnAddPhoto">펀딩 사진 추가하기</button>
					<input type="file" id="inputPhoto" accept="image/*" multiple
						style="display: none" />
					<!-- 업로드된 이미지 url을 담을 hidden input -->
					<div id="hiddenImageInputs"></div>

					<button type="button" id="btnDefaultPhoto">메뉴 사진과 동일</button>
				</div>
				<!-- 사진 미리보기 -->
				<div id="previewContainer" class="preview-container"></div>

			</div>

			<div class="form-actions">
				<button type="button"
					onclick="location.href='/seller/store/stats?fundingId=${tempFunding.fundingId}'"
					class="back-button">뒤로가기</button>
				<button type="submit" class="submit-button">다음</button>
			</div>
		</form>
	</div>
</div>
<script>
//기존 메뉴로 사진 불러오기
btnDefaultPhoto.addEventListener('click', () => {
	const fundingId = document.getElementById("fundingId").value;

 $.ajax({
   url: "${pageContext.request.contextPath}/seller/fundings/loadDefaultImages",
   type: "POST",
   data: { fundingId: fundingId },
   success: function(response) {
     const hiddenContainer = document.getElementById("hiddenImageInputs");
     const previewContainer = document.getElementById("previewContainer");

    // 기존 내용 초기화
    previewContainer.innerHTML = '';
    hiddenContainer.innerHTML = '';
    
	  // 이미지 없을 경우
	if (!response || response.length === 0) {
		alert("등록된 메뉴 이미지가 없습니다.");
		return;
	}

      response.forEach((url, index) => {
        const div = document.createElement('div');
        div.classList.add('preview-item');

        const img = document.createElement('img');
        img.src = url;
        img.alt = "default";

        const btnCancel = document.createElement('button');
        btnCancel.textContent = '취소하기';
        btnCancel.classList.add('btn-cancel');

        btnCancel.addEventListener('click', () => {
          div.remove();
          hiddenContainer.querySelector(`input[value='${url}']`)?.remove();
        });

        div.appendChild(img);
        div.appendChild(btnCancel);
        previewContainer.appendChild(div);

        const hiddenInput = document.createElement("input");
        hiddenInput.type = "hidden";
        hiddenInput.name = `images[\${index}].imageUrl`;
        hiddenInput.value = url;
        hiddenContainer.appendChild(hiddenInput);
      });
    }
  });
});
</script>