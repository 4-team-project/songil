<%@ include file="/WEB-INF/views/common/init.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet"
	href="${cpath}/resources/css/pages/seller/productDetail.css">
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
let selectedFiles = [];

const productImages = [
	<c:forEach var="img" items="${productDTO.images}" varStatus="loop">
		"${img.imageUrl}"<c:if test="${!loop.last}">,</c:if>
	</c:forEach>
];

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
const redirectUrl = document.getElementById("redirectUrl")?.value;
function submitProduct() {
	  const productId = document.getElementById("productId").value;
	  const productData = {
	    productId: productId || null,
	    productName: document.getElementById('productName').value,
	    price: parseInt(document.getElementById('productPrice').value),
	    description: document.getElementById('productDescription').value,
	    storeId: 1,
	    images: selectedFiles.map(file => {
	    	  const name = file.name || file.imageUrl; 
	    	  return {
	    	    imageUrl: name.startsWith("/image/") ? name : "/image/" + name
	    	  };
	    	})  
	  };
	  
	  const formData = new FormData();
	  formData.append("product", JSON.stringify(productData));
	  selectedFiles.forEach(file => {
		  if (!file.isExisting) formData.append("images", file);
		});
	  
	  console.log(formData);
	  const url = productId
	    ? `${cpath}/seller/product/update/${productId}`
	    : `${cpath}/seller/product/insert`;

	  const method = "POST";

	  fetch(url, {
		  method: "POST",
		  body: formData
		})
		.then(res => res.text())
		.then(msg => {
		  alert((productId ? "수정" : "등록") + " 결과: " + msg);

		  if (redirectUrl) {
		    location.href = redirectUrl;
		  } else {
		    location.href = `${cpath}/seller/product`;
		  }
		})
	  .catch(err => alert("오류: " + err));
	}

document.addEventListener('DOMContentLoaded', () => {
  const productId = document.getElementById("productId")?.value;
  const urlParams = new URLSearchParams(window.location.search);
  const redirect = urlParams.get('redirect');
  if (productId) {
    fetch(`${cpath}/seller/product/info/${productId}`)
      .then(res => res.json())
      .then(product => {
    	  console.log("불러온 상품:", product);
        document.getElementById('productName').value = product.productName;
        document.getElementById('productPrice').value = product.price;
        document.getElementById('productDescription').value = product.description;

        if (product.images && product.images.length > 0) {
          const preview = document.getElementById('preview-list');
          const fileCountText = document.getElementById('file-count-text');

          product.images.forEach(img => {
        	  const wrapper = document.createElement('div');
        	  wrapper.className = 'preview-item';

        	  const image = document.createElement('img');
        	  image.src = img.imageUrl;
        	  image.alt = '기존 이미지';

        	  const delBtn = document.createElement('div');
        	  delBtn.className = 'delete-btn';
        	  delBtn.innerHTML = '×';
        	  delBtn.onclick = () => {
        	    wrapper.remove();
        	    selectedFiles = selectedFiles.filter(f => f.name !== img.imageUrl && f.imageUrl !== img.imageUrl);
        	    fileCountText.textContent = `선택한 사진 ${selectedFiles.length} / 3`;
        	  };

        	  wrapper.appendChild(image);
        	  wrapper.appendChild(delBtn);
        	  preview.appendChild(wrapper);

        	  selectedFiles.push({ name: img.imageUrl, isExisting: true });
        	});
         console.log(selectedFiles);

        	fileCountText.textContent = `선택한 사진 \${selectedFiles.length} / 3`;

        }
      });
  }
  
  const backBtn = document.querySelector('.complete-back-btn');
  if (redirect && backBtn) {
    backBtn.onclick = function () {
      location.href = redirect;
    };
  }
});
</script>


