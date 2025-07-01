$(document).ready(function() {
    console.log("jQuery document ready event fired! Starting initial data load.");

    const fundingListContainer = document.getElementById('fundingListContainer');
    const fundingTabs = document.querySelector('.funding-tabs');
    const fundingCountSummary = document.getElementById('fundingCountSummary');
    let allFundings = [];

    // --- 초기 상점 목록 및 첫 번째 상점 펀딩 로드 로직 시작 ---
    const userId = document.getElementById('currentUserId').value; 
    
    if (!userId || isNaN(Number(userId))) {
        alert("User ID가 유효하지 않아 초기 펀딩을 불러올 수 없습니다.");
        console.error("오류: 유효하지 않은 userId 값:", userId);
        fundingListContainer.innerHTML = '<p>User ID가 없어 초기 펀딩을 불러올 수 없습니다.</p>';
        updateFundingCounts();
        return; 
    }

    $.ajax({
        url: `/seller/stores/byUser?userId=${userId}`,
        method: 'GET',
        dataType: 'json',
        success: function(stores) {
            const container = document.getElementById('storeListContainer');
            container.innerHTML = '<h4>다른 지점들:</h4>';

            if (stores.length === 0) {
                container.innerHTML += '<p>다른 지점이 없습니다.</p>';
                fundingListContainer.innerHTML = '<p>상점 정보가 없어 펀딩을 불러올 수 없습니다.</p>';
                allFundings = []; 
                updateFundingCounts();
                return;
            }

            const initialStoreId = stores[0].storeId;
            loadAndDisplayFundings(initialStoreId);

            stores.forEach(store => {
                const btn = document.createElement('button');
                btn.textContent = store.storeName;
                
                btn.addEventListener('click', function() {
                    const selectedStoreId = store.storeId; 
                    console.log("DEBUG: 펀딩 요청에 사용될 storeId:", selectedStoreId);

                    if (!selectedStoreId || isNaN(Number(selectedStoreId))) {
                        alert("선택한 상점의 ID가 유효하지 않습니다.");
                        console.error("오류: 유효하지 않은 selectedStoreId 값:", selectedStoreId);
                        return;
                    }
                    loadAndDisplayFundings(selectedStoreId);
                });
                container.appendChild(btn);
            });
        },
        error: function(xhr, status, error) {
            console.error('AJAX 요청 실패 (상점 목록):');
            console.error('xhr 객체:', xhr);
            console.error('상태 코드 (status):', status);
            console.error('에러 메시지 (error):', error);

            let errorMessage = '상점 목록을 불러오지 못했습니다.';
            if (xhr && xhr.responseText) {
                errorMessage += ` (서버 응답: ${xhr.responseText})`;
            } else if (error) {
                errorMessage += ` (오류 상세: ${error})`;
            } else if (status) {
                errorMessage += ` (상태: ${status})`;
            }
            document.getElementById('storeListContainer').innerHTML = `<p>${errorMessage}</p>`;
            fundingListContainer.innerHTML = `<p>${errorMessage}</p>`;
            allFundings = []; 
            updateFundingCounts();
        }
    });

    const showStoresBtn = document.getElementById('showStoresBtn');
    if (showStoresBtn) {
        showStoresBtn.addEventListener('click', (event) => {
            console.log("'다른 지점 보기' 버튼이 수동으로 클릭되었습니다. (현재는 자동 로딩)");
        });
    }

    function loadAndDisplayFundings(storeId, initialStatus = 'all') {
        $.ajax({
            url: `/seller/fundings/byStore?storeId=${storeId}`,
            method: 'GET',
            dataType: 'json',
            success: function(fundings) {
                allFundings = fundings; 
                updateFundingCounts();
                displayFundings(initialStatus); 
                setActiveTab(initialStatus); 
            },
            error: function(xhr, status, error) {
                console.error('AJAX 요청 실패 (펀딩 목록):');
                console.error('xhr 객체:', xhr);
                console.error('상태 코드 (status):', status);
                console.error('에러 메시지 (error):', error);

                let errorMessage = '펀딩 정보를 불러오지 못했습니다.';
                if (xhr && xhr.responseText) {
                    errorMessage += ` (서버 응답: ${xhr.responseText})`;
                } else if (error) {
                    errorMessage += ` (오류 상세: ${error})`;
                } else if (status) {
                    errorMessage += ` (상태: ${status})`;
                }
                fundingListContainer.innerHTML = `<p>${errorMessage}</p>`;
                allFundings = []; 
                updateFundingCounts();
            }
        });
    }

    // ★★★ 펀딩을 필터링하여 화면에 표시하는 함수 (수정) ★★★
    function displayFundings(statusToFilter) {
        let filteredFundings = [];
        if (statusToFilter === 'all') {
            filteredFundings = allFundings;
        } else if (statusToFilter === '종료') { 
            filteredFundings = allFundings.filter(f => f.status === '성공' || f.status === '실패');
        } else {
            filteredFundings = allFundings.filter(f => f.status === statusToFilter);
        }

        fundingListContainer.innerHTML = ''; 

        if (filteredFundings.length === 0) {
            fundingListContainer.innerHTML = `<p>이 상태에 해당하는 펀딩이 없습니다.</p>`;
            return;
        }

        // 펀딩 카드 생성 및 클릭 이벤트 바인딩
        filteredFundings.forEach(f => {
            const name = f.fundingName || '이름 없음';
            const desc = f.fundingDesc || '설명 없음';
            const current = Number(f.currentQty || 0);
            const target = Number(f.targetQty || 0);
            const rate = target > 0 ? Math.floor(current * 100 / target) : 0;

            const startDateObj = new Date(f.startDate);
            const endDateObj = new Date(f.endDate);

            let startDate = '날짜 정보 없음';
            if (!isNaN(startDateObj.getTime())) {
                startDate = startDateObj.toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' });
            }

            let endDate = '날짜 정보 없음';
            if (!isNaN(endDateObj.getTime())) {
                endDate = endDateObj.toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' });
            }
            
            const status = f.status || '상태 없음'; 
            const fundingId = f.fundingId; // ★ fundingId 가져오기

            const fundingCard = document.createElement('div');
            fundingCard.className = 'funding-card';
            fundingCard.dataset.status = status;
            fundingCard.dataset.fundingId = fundingId; // ★ data-funding-id 속성 추가

            fundingCard.innerHTML = `
                <h3>${name}</h3>
                <p><strong>설명:</strong> ${desc}</p>
                <p><strong>달성률:</strong> ${rate}%</p>
                <p><strong>시작일:</strong> ${startDate}</p>
                <p><strong>종료일:</strong> ${endDate}</p>
                <p><strong>상태:</strong> ${status}</p>
            `;

            // ★ 펀딩 카드 클릭 이벤트 리스너 추가 ★
            fundingCard.addEventListener('click', function() {
                const clickedFundingId = this.dataset.fundingId; // 클릭된 카드의 fundingId 가져오기
                if (clickedFundingId) {
                    // sellerFundingStats.jsp로 이동하면서 fundingId를 쿼리 파라미터로 전달
                    window.location.href = `/seller/stats?fundingId=${clickedFundingId}`;
                } else {
                    console.error("펀딩 ID를 찾을 수 없습니다.");
                }
            });

            fundingListContainer.appendChild(fundingCard);
        });
    }

    function setActiveTab(activeStatus) {
        if (fundingTabs) {
            const buttons = fundingTabs.querySelectorAll('button');
            buttons.forEach(button => {
                if (button.dataset.status === activeStatus) {
                    button.classList.add('active');
                } else {
                    button.classList.remove('active');
                }
            });
        }
    }

    function updateFundingCounts() {
        if (!fundingCountSummary) {
            console.error("오류: 'fundingCountSummary' 요소를 찾을 수 없습니다. HTML ID를 확인해주세요.");
            return;
        }

        const allCount = allFundings.length;
        const inProgressCount = allFundings.filter(f => f.status === '진행중').length;
        const scheduledCount = allFundings.filter(f => f.status === '준비중').length; 
        const endedCount = allFundings.filter(f => f.status === '성공' || f.status === '실패').length;

        fundingCountSummary.innerHTML = `
            총 펀딩 개수: <span>${allCount}</span>개 | 
            진행중: <span>${inProgressCount}</span>개 | 
            준비중: <span>${scheduledCount}</span>개 | 
            종료: <span>${endedCount}</span>개
        `;
        console.log("펀딩 개수 업데이트 완료:", fundingCountSummary.innerHTML);
    }

    // ★★★ 탭 버튼 클릭 이벤트 리스너 ★★★
    if (fundingTabs) {
        fundingTabs.addEventListener('click', function(event) {
            const clickedButton = event.target;
            const buttonElement = clickedButton.closest('button'); 
            if (buttonElement) {
                const status = buttonElement.dataset.status; 
                if (status) {
                    displayFundings(status); 
                    setActiveTab(status); 
                }
            }
        });
    }
});