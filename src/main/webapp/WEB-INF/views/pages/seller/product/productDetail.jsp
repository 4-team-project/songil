<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/productDetail.css">

<div class="main-content">
	<div class="main-title-box">
		<div class="main-title">상점에 새롭게 추가할 메뉴에 대한 정보를 입력해주세요</div>
	</div>
	<div class="content-box">
		<div class="content-text">메뉴 이름</div>
		<input type="text" id="productName" placeholder="메뉴 이름을 입력하세요" class="content-input" />
	</div>
	<div class="content-box">
		<div class="content-text">메뉴의 원래 가격(정가)</div>
		<input type="text" id="productPrice" placeholder="메뉴 이름을 입력하세요" class="content-input" />
	</div>
	<div class="content-box">
		<div class="content-text">메뉴에 대한 설명</div>
		<textarea id="productDescription" class="content-textarea"
			placeholder="상품 설명은 비워도 괜찮아요
		꼭 작성하지 않아도 등록할 수 있어요"></textarea>
	</div>
	<div class="content-box">
		<div class="content-text">
			메뉴 사진을 넣어주세요!<br />[사진 추가하기] 버튼을 누르면 사진을 선택할 수 있어요<br />사진은 3개까지
			가능합니다
		</div>
		<div class="content-input" id="image-preview-container">
			<div class="menu-img-upload-wrapper">
				<label for="images" class="menu-img-btn">사진 추가하기</label> <input
					type="file" id="images" name="images" multiple accept="image/*"
					onchange="handleFiles(this.files)" />
			</div>
			<div id="preview-list" class="preview-list"></div>
			<div id="file-count-text" class="file-count-text"
				style="margin-top: 8px; color: #888; font-size: 15px;">선택한 사진
				0 / 3</div>
		</div>
	</div>
	<div class="complete-back-btn-box">
		<div class="complete-back-btn" onclick="history.back()">이전</div>
		<button onclick="submitProduct()" class="complete-back-btn">수정 완료</button>
	</div>
</div>

<script>
let selectedFiles = [];

function handleFiles(fileList) {
  const preview = document.getElementById('preview-list');
  const fileCountText = document.getElementById('file-count-text');
  const maxFiles = 3;

  const files = Array.from(fileList);
  const remainingSlots = maxFiles - selectedFiles.length;
  
  if (remainingSlots <= 0) {
    alert("사진은 최대 3개까지 선택할 수 있습니다.");
    return;
  }
  
  if (files.length > remainingSlots) {
	    alert("사진은 최대 3개까지 선택할 수 있습니다.");
	  }

  const filesToAdd = files.slice(0, remainingSlots); 

  filesToAdd.forEach((file) => {
    if (!file.type.startsWith('image/')) return;

    const reader = new FileReader();
    reader.onload = (e) => {
      const wrapper = document.createElement('div');
      wrapper.className = 'preview-item';

      const img = document.createElement('img');
      img.src = e.target.result;

      const delBtn = document.createElement('div');
      delBtn.className = 'delete-btn';
      delBtn.innerHTML = '×';

      delBtn.onclick = () => {
        wrapper.remove();
        selectedFiles = selectedFiles.filter(f => f !== file);
        fileCountText.textContent = `선택한 사진 \${selectedFiles.length} / \${maxFiles}`;
      };

      wrapper.appendChild(img);
      wrapper.appendChild(delBtn);
      preview.appendChild(wrapper);

      selectedFiles.push(file);
      fileCountText.textContent = `선택한 사진 \${selectedFiles.length} / \${maxFiles}`;
    };
    reader.readAsDataURL(file);
  });

  document.getElementById('images').value = '';
}

function submitProduct() {
	  const productData = {
	    productName: document.getElementById('productName').value,
	    price: parseInt(document.getElementById('productPrice').value),
	    description: document.getElementById('productDescription').value,
	    storeId: 1, // 임시 ID (로그인 연동 전)
	    images: selectedFiles.map(file => ({
	        imageUrl: file.name 
	      }))
	    };

	  fetch("/takku/seller/product/insert", {
	    method: "POST",
	    headers: {
	      "Content-Type": "application/json"
	    },
	    body: JSON.stringify(productData)
	  })
	  .then(res => res.text())
	  .then(msg => alert("전송" + msg))
	  .catch(err => alert("오류: " + err));
	}

</script>


