<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/mypage.css">

<script>
	$(function() {
		// 기존 비밀번호 토글
		const $password = $("#passwordInput");
		const $toggle = $("#togglePassword");

		let isShown = false;

		$toggle.on("click", function() {
			if (isShown) {
				$password.attr("type", "password");
				$toggle.find("img").attr("src",
						"${cpath}/resources/images/eye.svg");
			} else {
				$password.attr("type", "text");
				$toggle.find("img").attr("src",
						"${cpath}/resources/images/eye-off.svg");
			}
			isShown = !isShown;
		});

		// 새 비밀번호 토글
		const $newPassword = $("#newPasswordInput");
		const $toggleNew = $("#toggleNewPassword");

		let isNewShown = false;

		$toggleNew.on("click", function() {
			if (isNewShown) {
				$newPassword.attr("type", "password");
				$toggleNew.find("img").attr("src",
						"${cpath}/resources/images/eye.svg");
			} else {
				$newPassword.attr("type", "text");
				$toggleNew.find("img").attr("src",
						"${cpath}/resources/images/eye-off.svg");
			}
			isNewShown = !isNewShown;
		});
	});

	$(function() {
	<%-- 수정 완료 여부 체크 --%>
	var updateSuccess = $
		{
			updateSuccess ? 'true' : 'false'
		}
		;

		if (updateSuccess === 'true') {
			$("#resultModal").show();
		}

		$("#closeModalBtn").on("click", function() {
			$("#resultModal").hide();
		});
	});

	$(function() {
		let actionType = ""; // 'register' or 'cancel'

		$("#registerPartnerBtn")
				.on(
						"click",
						function() {
							actionType = "register";
							$("#modalTitle").text("파트너 등록");
							$("#modalDesc")
									.html(
											"파트너 등록 시 판매가 가능해지며, 수수료 약관에 동의한 것으로 간주됩니다.<br>계속 진행하시겠습니까?");
							$("#partnerModal, #modalBackdrop").fadeIn();
						});

		$("#cancelPartnerTriggerBtn").on("click", function() {
			actionType = "cancel";
			$("#modalTitle").text("파트너 해지");
			$("#modalDesc").html("파트너를 해지하면 판매 기능이 비활성화됩니다.<br>계속 진행하시겠습니까?");
			$("#partnerModal, #modalBackdrop").fadeIn();
		});

		$("#cancelModalBtn, #modalBackdrop").on("click", function() {
			$("#partnerModal, #modalBackdrop").fadeOut();
		});

		$("#confirmPartnerBtn").on("click", function() {
			if (!$("#agreeCheckbox").is(":checked")) {
				alert("약관에 동의해야 진행할 수 있습니다.");
				return;
			}

			$.ajax({
				type : "POST",
				url : "${cpath}/seller/partner/change",
				data : {
					action : actionType
				},
				success : function(res) {
					if (res === "success") {
						alert("처리가 완료되었습니다.");
						location.reload();
					} else {
						alert("처리에 실패했습니다.");
					}
				},
				error : function() {
					alert("오류가 발생했습니다.");
				}
			});
		});
	});
</script>

<div class="mypage-container">
	<h2>
		사장님의 기본 정보를 확인할 수 있습니다. <br> 정보가 바뀌었다면 수정 후 <strong
			style="color: #ff9670">'수정하기'</strong> 버튼을 눌러주세요.
	</h2>

	<form action="${cpath}/seller/mypage/update" method="post">
		<input type="hidden" name="userId" value="${loginUser.userId}" />

		<!-- 이름 -->
		<div class="user-info">
			<label>이름</label>
			<div class="input-group">
				<input type="text" value="${loginUser.name}" readonly />
			</div>
		</div>

		<!-- 전화번호 -->
		<div class="user-info">
			<label>전화번호</label>
			<div class="input-group">
				<input type="text" value="${loginUser.phone}" readonly />
			</div>
		</div>

		<!-- 비밀번호 -->
		<div class="user-info">
			<label>비밀번호</label>
			<div class="input-group password-group" style="position: relative;">
				<!-- 실제 비밀번호 길이에 맞춰 * 표시 -->
				<input type="password" id="passwordInput" name="password"
					value="${loginUser.password}" readonly style="letter-spacing: 2px;" />

				<!-- 눈 아이콘 -->
				<span id="togglePassword"
					style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); cursor: pointer;">
					<img src="${cpath}/resources/images/eye.svg" alt="비밀번호 보기"
					width="20" />
				</span>
			</div>
		</div>

		<!-- 새 비밀번호 입력 -->
		<div class="user-info">
			<label>새 비밀번호 (변경 시 입력)</label>
			<div class="input-group password-group" style="position: relative;">
				<input type="password" id="newPasswordInput" name="newPassword"
					placeholder="새 비밀번호를 입력해주세요" />
				<!-- 눈 아이콘 추가 -->
				<span id="toggleNewPassword"
					style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%); cursor: pointer;">
					<img src="${cpath}/resources/images/eye.svg" alt="비밀번호 보기"
					width="20" />
				</span>
			</div>
		</div>

		<!-- 닉네임 -->
		<div class="user-info">
			<label>닉네임</label>
			<div class="input-group">
				<input type="text" name="nickname" value="${loginUser.nickname}" />
			</div>
		</div>

		<!-- 생년월일 -->
		<div class="user-info">
			<label>생년월일</label>
			<div class="input-group">
				<input type="text"
					value="<fmt:formatDate value='${loginUser.birth}' pattern='yyyy-MM-dd' />"
					readonly />
			</div>
		</div>

		<!-- 성별 -->
		<div class="user-info">
			<label>성별</label>
			<div class="input-group">
				<input type="text" value="${loginUser.gender}" readonly />
			</div>
		</div>

		<!-- 파트너 여부 -->
		<div class="user-info">
			<label>파트너 여부</label>
			<div class="input-group with-btn">
				<input type="text" value="${loginUser.isPartner}" readonly />

				<c:choose>
					<c:when test="${loginUser.isPartner eq 'N'}">
						<button class="btn side-btn" type="button" id="registerPartnerBtn">등록하기</button>
					</c:when>
					<c:when test="${loginUser.isPartner eq 'Y'}">
						<button class="btn side-btn" type="button" id="cancelPartnerTriggerBtn">해지하기</button>
					</c:when>
				</c:choose>
			</div>
		</div>

		<!-- 시/도 + 시/군/구 -->
		<div class="user-info">
			<label>주소 (시/도 시/군/구)</label>
			<div class="input-group">
				<input type="text" value="${loginUser.sido} ${loginUser.sigungu}"
					readonly />
			</div>
		</div>

		<div class="btn-group">
			<!-- 다시 생성 버튼 -->
			<button class="btn" type="button" onclick="history.back()">이전</button>
			<button class="btn filled" type="submit">수정</button>
		</div>
	</form>
</div>

<!-- 모달 영역 -->
<div id="resultModal">
	<p id="modalMsg">회원 정보가 수정되었습니다.</p>
	<button id="closeModalBtn">확인</button>
</div>

<!-- 파트너 등록 계약 모달 -->
<div id="partnerModal">
	<p id="modalTitle">파트너 등록 계약</p>
	<p id="modalDesc">
		소상공인 파트너로 등록하면 판매 기능이 활성화되며,<br /> 수수료 및 약관에 동의한 것으로 간주됩니다.<br />
		계속하시겠습니까?
	</p>
	<div style="margin-top: 15px;">
		<label><input type="checkbox" id="agreeCheckbox" /> 약관에
			동의합니다.</label>
	</div>
	<div style="margin-top: 20px;">
		<button id="cancelPartnerBtn">취소</button>
		<button id="confirmPartnerBtn">등록</button>
	</div>
</div>

<!-- 모달 배경 -->
<div id="modalBackdrop"></div>
