<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<link rel="stylesheet" type="text/css"
	href="${cpath}/resources/css/pages/auth_signup.css">

<script>
	$("#cancelJoinBtn").on("click", function() {
		location.href = "${cpath}/auth/login";
	});

	$(function() {
		// 모달 열기 함수
		function showModal(message, callback) {
			$("#modalMsg").text(message);
			$("#resultModal").fadeIn();

			$("#closeModalBtn").off("click").on("click", function() {
				$("#resultModal").fadeOut(function() {
					if (callback) callback();
				});
			});
		}

		$("#cancelJoinBtn").on("click", function() {
			location.href = "${cpath}/auth/login";
		});

		// 인증번호 전송
		$("#sendAuthCodeBtn").on("click", function() {
			const phone = $("input[name='phone']").val();
			if (!phone) {
				showModal("휴대폰 번호를 입력하세요.", function() {
					$("input[name='phone']").focus();
				});
				return;
			}

			$.post("${cpath}/auth/send-auth-code", { phone: phone }, function(res) {
				if (res === "success") {
					$("#authCodeSection").show();
				} else {
					showModal("인증번호 전송 실패. 다시 시도해주세요.");
				}
			});
		});

		// 인증번호 확인
		$("#verifyAuthCodeBtn").on("click", function() {
			const inputCode = $("#authCodeInput").val();
			if (!inputCode) {
				showModal("인증번호를 입력하세요.", function() {
					$("#authCodeInput").focus();
				});
				return;
			}

			$.post("${cpath}/auth/verify-auth-code", { inputCode: inputCode }, function(res) {
				if (res === "success") {
					$("#authSuccessMessage").show();
					$("#authVerified").val("true");
				} else {
					showModal("인증번호가 일치하지 않습니다.", function() {
						$("#authCodeInput").focus();
					});
				}
			});
		});

		// 가입하기 버튼 클릭 시 본인인증 확인
		$("#joinForm").on("submit", function(e) {
			const authVerified = $("#authVerified").val();
			if (authVerified !== "true") {
				e.preventDefault();
				showModal("본인인증이 완료되어야 가입할 수 있습니다.", function() {
					$("#sendAuthCodeBtn").focus();
				});
			}
		});

		// 중복 계정 확인 함수
		function checkDuplicate() {
			const phone = $("input[name='phone']").val();
			const userType = $("input[name='userType']:checked").val();

			if (!phone || !userType) {
				$("#duplicateMsg").hide();
				return;
			}

			$.post("${cpath}/auth/check-duplicate", { phone: phone, userType: userType }, function(res) {
				if (res.exists) {
					$("#duplicateMsg").text("중복된 계정입니다. 다른 번호를 입력하세요.").show();
				} else {
					$("#duplicateMsg").hide();
				}
			});
		}

		$("input[name='userType']").on("change", checkDuplicate);
		$("input[name='phone']").on("blur", checkDuplicate);

		$("#joinForm").on("submit", function(e) {
			if ($("#duplicateMsg").is(":visible")) {
				e.preventDefault();
				showModal("중복된 계정이므로 가입할 수 없습니다.");
			}
		});
	});
</script>


<div class="signup-container">
	<img src="${cpath}/resources/images/logo.svg" class="logo-img" />

	<form id="joinForm" method="post" action="${cpath}/auth/signup">
		<!-- 회원 유형 선택 (선택 사항) -->
		<div class="modal-info">
			<p>
				<strong>회원 유형</strong> <label><input type="radio"
					name="userType" value="사용자" checked /> 사용자</label> <label
					style="margin-left: 10px;"><input type="radio"
					name="userType" value="소상공인" /> 소상공인</label>
			</p>
			<div class="phone-input-row">
				<strong>휴대폰번호(ID)</strong>
				<div class="phone-auth-wrap">
					<input type="text" name="phone" required class="modal-input"
						placeholder="숫자만 입력" />
					<button type="button" class="auth-btn" id="sendAuthCodeBtn">본인인증</button>
				</div>
			</div>
			<!-- 중복된 계정 메시지 -->
			<div class="duplicate-msg-wrap">
				<p id="duplicateMsg">중복된 계정입니다. 다른 번호를 입력하세요.</p>
			</div>
			<!-- 인증번호 입력 섹션 -->
			<div id="authCodeSection" class="auth-code-section"
				style="display: none;">
				<strong>인증번호</strong>
				<div class="phone-auth-wrap">
					<input type="text" id="authCodeInput" class="modal-input"
						placeholder="인증번호 입력" />
					<button type="button" class="auth-btn" id="verifyAuthCodeBtn">확인</button>
				</div>
			</div>
			<!-- 인증 성공 메시지 -->
			<p id="authSuccessMessage"
				style="display: none; color: green; font-size: 14px; margin-left: 113px;">
				본인인증이 완료되었습니다.</p>
			<input type="hidden" name="authVerified" id="authVerified"
				value="false" />
			<p>
				<strong>비밀번호</strong> <input type="password" name="password"
					required class="modal-input" />
			</p>
			<p>
				<strong>이름</strong> <input type="text" name="name" required
					class="modal-input" />
			</p>
			<p>
				<strong>성별</strong> <label><input type="radio" name="gender"
					value="남" checked /> 남</label> <label style="margin-left: 10px;"><input
					type="radio" name="gender" value="여" /> 여</label>
			</p>
			<p>
				<strong>생년월일</strong> <input type="text" name="birth" required
					class="modal-input" placeholder="yyyy-mm-dd" />
			</p>
			<p>
				<strong>닉네임</strong> <input type="text" name="nickname" required
					class="modal-input" />
			</p>
			<p>
				<strong>시/도</strong> <input type="text" name="sido" required
					class="modal-input" placeholder="ex)서울, 부산, 광주" />
			</p>
			<p>
				<strong>시/군/구</strong> <input type="text" name="sigungu" required
					class="modal-input" placeholder="ex)마포구, 영등포구" />
			</p>
		</div>

		<div class="modal-buttons">
			<button type="submit" class="modal-btn confirm">가입하기</button>
			<button type="button" class="modal-btn cancel" id="cancelJoinBtn">취소</button>
		</div>
	</form>

	<!-- 모달 영역 (CSS는 auth_signup.css에 있음) -->
	<div id="resultModal">
		<p id="modalMsg"></p>
		<button id="closeModalBtn">확인</button>
	</div>
</div>

<%-- 메시지 출력 스크립트 --%>
<c:if test="${not empty resultMessage}">
	<script>
		$(function() {
			var isSuccess = "${isSuccess}" === "true";
			$("#modalMsg").text("${resultMessage}");
			$("#resultModal").fadeIn();

			$("#closeModalBtn").on("click", function() {
				$("#resultModal").fadeOut(function() {
					if (isSuccess) {
						location.href = "${cpath}/auth/login";
					}
				});
			});
		});
	</script>
</c:if>