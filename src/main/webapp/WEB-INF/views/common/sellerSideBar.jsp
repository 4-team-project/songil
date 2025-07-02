<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/init.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<style>
.sidebar {
	width: 320px;
	background-color: #FFF6F0;
	padding: 20px;
	flex-shrink: 0;
	box-sizing: border-box;
	cursor: url('${cpath}/resources/images/cursor.svg') 2 2, auto;
}

.store-info {
	display: flex;
	flex-direction: column;
	gap: 4px;
	margin-bottom: 40px;
	margin-top: 20px;
	font-size: 18px;
	color: #444;
	position: relative;
}

.store-info strong {
	color: #FF9670;
	font-size: 20px;
}

.change-store-btn {
	align-self: flex-start;
	margin-top: 4px;
	padding: 10px 10px;
	font-size: 13px;
	background-color: #FF9670;
	color: #fff;
	border: none;
	border-radius: 6px;
	cursor:
		url('${pageContext.request.contextPath}/resources/images/cursor.svg')
		2 2, auto;
}

.change-store-btn:hover {
	background-color: #ffb199;
	transform: scale(1.2);
}

.menu ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

.menu-item {
	display: flex;
	align-items: center;
	gap: 14px;
	padding: 18px 20px;
	margin-bottom: 12px;
	border-radius: 8px;
	cursor:
		url('${pageContext.request.contextPath}/resources/images/cursor.svg')
		2 2, auto;
	font-size: 18px;
	transition: background-color 0.2s, transform 0.2s;
}

.menu-item img {
	width: 24px;
	height: 24px;
}

.menu-item:hover {
	background-color: rgb(255, 68, 10, 10%);
	transform: scale(1.2);
}

.menu-item.active {
	background-color: #FF9670;
	color: white;
	font-weight: bold;
}
</style>

<body>
	<aside class="sidebar">
		<div class="store-info">
			<span>[현재 상점]</span> <strong>김밥식 맛집(마포구)</strong>
			<button class="change-store-btn">변경</button>
		</div>

		<nav class="menu">
			<ul>
				<li class="menu-item active" data-name="home"
					data-url="${cpath}/seller/home" onclick="activateMenu(this)">
					<img src="${cpath}/resources/images/sideBar/home_active.svg"
					alt="home" /> <span>홈</span>
				</li>
				<li class="menu-item" data-name="add"
					data-url="${cpath}/seller/fundings/create-step1" onclick="activateMenu(this)">
					<img src="${cpath}/resources/images/sideBar/add.svg" alt="add" />
					<span>펀딩 만들기</span>
				</li>
				<li class="menu-item" data-name="funding"
					data-url="${cpath}/seller/store" onclick="activateMenu(this)">
					<img src="${cpath}/resources/images/sideBar/funding.svg"
					alt="funding" /> <span>펀딩 현황</span>
				</li>
				<!-- TODO:여기 storeId는 세션에꺼 읽어서 넘겨주기 -->
				<li class="menu-item" data-name="statistics"
					data-url="${cpath}/seller/stats?storeId=2"
					onclick="activateMenu(this)"><img
					src="${cpath}/resources/images/sideBar/statistics.svg"
					alt="statistics" /> <span>통계</span></li>
				<li class="menu-item" data-name="money"
					data-url="${cpath}/seller/store" onclick="activateMenu(this)">
					<img src="${cpath}/resources/images/sideBar/money.svg" alt="money" />
					<span>정산</span>
				</li>
				<li class="menu-item" data-name="store"
					data-url="${cpath}/seller/store" onclick="activateMenu(this)">
					<img src="${cpath}/resources/images/sideBar/store.svg" alt="store" />
					<span>상점 관리</span>
				</li>
				<li class="menu-item" data-name="move"
					data-url="${cpath}/user/home" onclick="activateMenu(this)">
					<img src="${cpath}/resources/images/sideBar/move.svg" alt="move" />
					<span>펀딩 사이트로 이동</span>
				</li>
				<li class="menu-item" data-name="mypage"
					data-url="${cpath}/seller/store" onclick="activateMenu(this)">
					<img src="${cpath}/resources/images/sideBar/mypage.svg"
					alt="mypage" /> <span>내 정보</span>
				</li>
			</ul>
		</nav>
	</aside>

	<script>
  const cpath = '${cpath}';

  function imgExists(url, callback) {
    const img = new Image();
    img.onload = () => callback(true);
    img.onerror = () => callback(false);
    img.src = url;
  }

  function updateMenuStyle(activeName) {
    document.querySelectorAll('.menu-item').forEach(item => {
      const itemName = item.getAttribute('data-name');
      const img = item.querySelector('img');

      if (itemName === activeName) {
        item.classList.add('active');
        const activeSrc = `\${cpath}/resources/images/sideBar/\${itemName}_active.svg`;
        if (img) {
          imgExists(activeSrc, exists => {
            img.src = exists ? activeSrc : `\${cpath}/resources/images/sideBar/\${itemName}.svg`;
          });
        }
      } else {
        item.classList.remove('active');
        if (img) {
          img.src = `\${cpath}/resources/images/sideBar/\${itemName}.svg`;
        }
      }
    });
  }

  function activateMenu(el) {
    const target = el.closest('.menu-item');
    if (!target) return;

    const name = target.getAttribute('data-name');
    const url = target.getAttribute('data-url');

    if (!name || !url) {
      console.warn("data-name 또는 data-url이 비어있음:", target);
      return;
    }

    localStorage.setItem('selectedMenu', name);

    window.location.href = url;
  }

  document.addEventListener('DOMContentLoaded', () => {
	  const path = window.location.pathname; 
	  let menuName = 'home'; 

	  if (path.includes('/seller/fundings/create-step1')) menuName = 'add';
	  else if (path.includes('/seller/funding')) menuName = 'funding';
	  else if (path.includes('/seller/stats')) menuName = 'statistics';
	  else if (path.includes('/seller/money')) menuName = 'money';
	  else if (path.includes('/seller/store')) menuName = 'store';
	  else if (path.includes('/seller/move')) menuName = 'move';
	  else if (path.includes('/seller/mypage')) menuName = 'mypage';
	  else if (path.includes('/seller/home')) menuName = 'home'; 

	  localStorage.setItem('selectedMenu', menuName);
	  updateMenuStyle(menuName);
	});
</script>




</body>

</html>