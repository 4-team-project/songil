<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>

<style>
.alert-popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  font-family: 'Spoqa Han Sans Neo';
}

.alert-popup {
  background: #fff;
  border-radius: 12px;
  padding: 30px 24px;
  width: 320px;
  text-align: center;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.alert-popup-icon {
  font-size: 36px;
  margin-bottom: 12px;
}

.alert-popup-message {
  font-size: 17px;
  color: #333;
  margin-bottom: 24px;
  line-height: 1.4;
}

.alert-popup-buttons {
  display: flex;
  justify-content: center;
  gap: 12px;
}

.alert-btn {
  padding: 10px 20px;
  font-size: 15px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  min-width: 80px;
}

.alert-btn.confirm {
  background-color: #FF9670;
  color: white;
}

.alert-btn.cancel {
  background-color: #e0e0e0;
  color: #333;
}

</style>


<div id="alertPopup" class="alert-popup-overlay" style="display: none;">
  <div class="alert-popup">
    <div class="alert-popup-icon" id="alertIcon">⚠️</div>
    <div class="alert-popup-message" id="alertMessage">메시지 내용</div>
    <div class="alert-popup-buttons">
      <button id="alertConfirmBtn" class="alert-btn confirm">확인</button>
      <button id="alertCancelBtn" class="alert-btn cancel" style="display: none;">취소</button>
    </div>
  </div>
</div>

<script>
function showPopupAlert({ type = 'info', message = '', onConfirm = null, onCancel = null }) {
	  const overlay = document.getElementById('alertPopup');
	  const icon = document.getElementById('alertIcon');
	  const msg = document.getElementById('alertMessage');
	  const confirmBtn = document.getElementById('alertConfirmBtn');
	  const cancelBtn = document.getElementById('alertCancelBtn');

	  const icons = {
	    success: '✅',
	    error: '❌',
	    warning: '⚠️',
	    info: 'ℹ️'
	  };

	  icon.textContent = icons[type] || icons.info;
	  msg.textContent = message;
	  cancelBtn.style.display = onCancel ? 'inline-block' : 'none';

	  confirmBtn.onclick = () => {
	    overlay.style.display = 'none';
	    if (onConfirm) onConfirm();
	  };
	  cancelBtn.onclick = () => {
	    overlay.style.display = 'none';
	    if (onCancel) onCancel();
	  };

	  overlay.style.display = 'flex';
	}

</script>
