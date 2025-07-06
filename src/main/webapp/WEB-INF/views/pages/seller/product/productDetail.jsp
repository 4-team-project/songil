<%@ include file="/WEB-INF/views/common/init.jsp"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/productDetail.css">
<input type="hidden" id="storeId" value="${storeId}" />
<input type="hidden" id="productId" value="${productDTO.productId}" />
<input type="hidden" id="redirectUrl" value="${redirectUrl}" />
<div class="main-title-box">
	<div class="main-title">상점에 새롭게 추가할 메뉴에 대한 정보를 입력해주세요</div>
</div>
<div class="content-box">
	<div class="content-text">메뉴 이름</div>
	<input type="text" id="productName" placeholder="메뉴 이름을 입력하세요"
		class="content-input" />
</div>
<div class="content-box">
	<div class="content-text">메뉴의 원래 가격(정가)</div>
	<input type="text" id="productPrice" placeholder="메뉴 이름을 입력하세요"
		class="content-input" />
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
			style="margin-top: 8px; color: #888; font-size: 15px;">선택한 사진 0
			/ 3</div>
	</div>
</div>
<div class="complete-back-btn-box">
	<div class="complete-back-btn" onclick="history.back()">이전</div>
	<button onclick="submitProduct()" class="complete-back-btn">수정
		완료</button>
</div>
<script>

let selectedFiles = []; // 새로 추가될 파일 객체들 (multipart/form-data로 전송)
let keptExistingImageUrls = []; // 유지될 기존 이미지 URL (JSON payload로 전송, cpath 없음)

function handleFiles(fileList) {
  const preview = document.getElementById('preview-list');
  const fileCountText = document.getElementById('file-count-text');
  const maxFiles = 3;

  const currentTotalImages = selectedFiles.length + keptExistingImageUrls.length;
  const remainingSlots = maxFiles - currentTotalImages;
  
  if (remainingSlots <= 0) {
    alert("사진은 최대 " + maxFiles + "개까지 선택할 수 있습니다.");
    document.getElementById('images').value = '';
    return;
  }
  
  const filesToAdd = Array.from(fileList).slice(0, remainingSlots); 

  filesToAdd.forEach((file) => {
    if (selectedFiles.some(f => f.name === file.name && f.size === file.size)) {
        console.warn("Skipping duplicate file:", file.name);
        return;
    }

    selectedFiles.push(file);
    console.log("handleFiles - selectedFiles 추가:", file.name, "현재 selectedFiles:", selectedFiles);

    const reader = new FileReader();
    reader.onload = (e) => {
      const wrapper = document.createElement('div');
      wrapper.className = 'preview-item';

      const img = document.createElement('img');
      img.src = e.target.result; // 미리보기 URL

      const delBtn = document.createElement('div');
      delBtn.className = 'delete-btn';
      delBtn.innerHTML = '×';

      delBtn.onclick = () => {
        wrapper.remove();
        selectedFiles = selectedFiles.filter(f => f !== file);
        updateFileCountText();
        console.log("delBtn click (new) - selectedFiles 제거:", file.name, "현재 selectedFiles:", selectedFiles);
      };

      wrapper.appendChild(img);
      wrapper.appendChild(delBtn);
      preview.appendChild(wrapper);
      
      updateFileCountText();
    };
    reader.readAsDataURL(file);
  });

  document.getElementById('images').value = '';
}

function updateFileCountText() {
    const fileCountText = document.getElementById('file-count-text');
    const maxFiles = 3;
    fileCountText.textContent = `선택한 사진 ${selectedFiles.length + keptExistingImageUrls.length} / ${maxFiles}`;
    console.log("updateFileCountText - 총 이미지 개수:", selectedFiles.length + keptExistingImageUrls.length);
}

const redirectUrl = document.getElementById("redirectUrl")?.value;

function submitProduct() {
  const productId = document.getElementById("productId").value;
  const storeId = document.getElementById("storeId").value;
  
  const productData = {
    productId: productId || null,
    productName: document.getElementById('productName').value,
    price: parseInt(document.getElementById('productPrice').value),
    description: document.getElementById('productDescription').value,
    storeId: parseInt(storeId),
    // ⭐️ 핵심 변경: 백엔드로 보내기 전에 keptExistingImageUrls에서 cpath 제거
    images: keptExistingImageUrls.map(url => ({
      imageUrl: url.startsWith(cpath) ? url.replace(cpath, '') : url // cpath 제거
    }))
  };
  
  console.log("submitProduct - 전송할 productData (JSON):", productData);
  console.log("submitProduct - 전송할 새 파일 (selectedFiles):", selectedFiles);

  const formData = new FormData();
  formData.append("product", JSON.stringify(productData));
  
  selectedFiles.forEach(file => {
    formData.append("images", file);
  });
  
  const url = productId
    ? `${cpath}/seller/product/update/${productId}`
    : `${cpath}/seller/product/insert`;

  fetch(url, {
      method: "POST",
      body: formData
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(text => Promise.reject(new Error(text)));
        }
        return res.text();
    })
    .then(msg => {
      alert((productId ? "수정" : "등록") + " 결과: " + msg);

      if (redirectUrl) {
        location.href = redirectUrl;
      } else {
        location.href = `${cpath}/seller/store?storeId=${storeId}`;
      }
    })
    .catch(err => {
      console.error("오류:", err);
      alert("오류 발생: " + err.message);
    });
}

document.addEventListener('DOMContentLoaded', () => {
  const productId = document.getElementById("productId")?.value;
  const urlParams = new URLSearchParams(window.location.search);
  const redirect = urlParams.get('redirect');
  
  if (productId) {
    fetch(`${cpath}/seller/product/info/${productId}`)
      .then(res => res.json())
      .then(product => {
        console.log("DOMContentLoaded - 불러온 상품 데이터:", product);
        document.getElementById('productName').value = product.productName;
        document.getElementById('productPrice').value = product.price;
        document.getElementById('productDescription').value = product.description;

        if (product.images && product.images.length > 0) {
          const preview = document.getElementById('preview-list');
          
          product.images.forEach(img => {
            const wrapper = document.createElement('div');
            wrapper.className = 'preview-item';

            const image = document.createElement('img');
            image.src = img.imageUrl; 
            image.alt = '기존 이미지';

            const delBtn = document.createElement('div');
            delBtn.className = 'delete-btn';
            delBtn.innerHTML = '×';
            
            keptExistingImageUrls.push(img.imageUrl);
            console.log("DOMContentLoaded - keptExistingImageUrls 추가:", img.imageUrl, "현재 keptExistingImageUrls:", keptExistingImageUrls);

            delBtn.onclick = () => {
              wrapper.remove();
              keptExistingImageUrls = keptExistingImageUrls.filter(url => url !== img.imageUrl);
              updateFileCountText();
              console.log("delBtn click (existing) - keptExistingImageUrls 제거:", img.imageUrl, "현재 keptExistingImageUrls:", keptExistingImageUrls);
            };

            wrapper.appendChild(image);
            wrapper.appendChild(delBtn);
            preview.appendChild(wrapper);
          });
          
          updateFileCountText();
        } else {
            console.log("DOMContentLoaded - 불러온 상품에 이미지가 없습니다.");
            updateFileCountText();
        }
      })
      .catch(err => {
          console.error("상품 정보를 불러오는 중 오류 발생:", err);
          alert("상품 정보를 불러오는데 실패했습니다.");
      });
  } else {
      console.log("DOMContentLoaded - 신규 상품 등록 모드입니다.");
      updateFileCountText();
  }
  
  const backBtn = document.querySelector('.complete-back-btn');
  if (redirect && backBtn) {
    backBtn.onclick = function () {
      location.href = redirect;
    };
  }
});
</script>