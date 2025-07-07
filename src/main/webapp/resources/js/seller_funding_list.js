let allFundings = [];
const fundingListContainer = document.getElementById('fundingListContainer');
const fundingTabs = document.querySelector('.funding-tabs');
const fundingCountSummary = document.getElementById('fundingCountSummary'); // fundingCountSummary 요소도 전역으로 선언



$(document).ready(function() {


    // --- 초기 상점 목록 및 첫 번째 상점 펀딩 로드 로직 시작 ---
    const userId = document.getElementById('currentUserId').value;

    if (!userId || isNaN(Number(userId))) {
        alert("User ID가 유효하지 않아 초기 펀딩을 불러올 수 없습니다.");
        if (fundingListContainer) fundingListContainer.innerHTML = '<p>User ID가 없어 초기 펀딩을 불러올 수 없습니다.</p>';
        updateFundingCounts(); // 데이터가 없으므로 0으로 업데이트
        return;
    }

    $.ajax({
        url: `${cpath}/seller/store/stores/byUser?userId=${userId}`,
        method: 'GET',
        dataType: 'json',
        success: function(stores) {
            const container = document.getElementById('storeListContainer');
            if (!container) {
                return;
            }
            container.innerHTML = '<h4>다른 지점들:</h4>';

            if (stores.length === 0) {
                container.innerHTML += '<p>다른 지점이 없습니다.</p>';
                if (fundingListContainer) fundingListContainer.innerHTML = '<p>상점 정보가 없어 펀딩을 불러올 수 없습니다.</p>';
                allFundings = [];
                updateFundingCounts();
                return;
            }

            const initialStoreId = stores[0].storeId;
            loadAndDisplayFundings(initialStoreId); // 첫 번째 상점 펀딩 로드

            stores.forEach(store => {
                const btn = document.createElement('button');
                btn.textContent = store.storeName;
                btn.addEventListener('click', function() {
                    const selectedStoreId = store.storeId;
                    if (!selectedStoreId || isNaN(Number(selectedStoreId))) {
                        alert("선택한 상점의 ID가 유효하지 않습니다.");
                        return;
                    }
                    loadAndDisplayFundings(selectedStoreId);
                });
                container.appendChild(btn);
            });
        },
        error: function(xhr, status, error) {

            let errorMessage = '상점 목록을 불러오지 못했습니다.';
            if (xhr && xhr.responseText) {
                errorMessage += ` (서버 응답: ${xhr.responseText})`;
            } else if (error) {
                errorMessage += ` (오류 상세: ${error})`;
            } else if (status) {
                errorMessage += ` (상태: ${status})`;
            }
            if (document.getElementById('storeListContainer')) document.getElementById('storeListContainer').innerHTML = `<p>${errorMessage}</p>`;
            if (fundingListContainer) fundingListContainer.innerHTML = `<p>${errorMessage}</p>`;
            allFundings = [];
            updateFundingCounts();
        }
    });

    const showStoresBtn = document.getElementById('showStoresBtn');

    if (showStoresBtn && storeListContainer) {
        showStoresBtn.addEventListener('click', (event) => {

            if (storeListContainer.style.display === 'none' || storeListContainer.style.display === '') {
                storeListContainer.style.display = 'block';
                if (!storeListContainer.querySelector('h4')) {
                    const title = document.createElement('h4');
                    title.textContent = '다른 지점들:';
                    storeListContainer.prepend(title);
                }
            } else {
                storeListContainer.style.display = 'none';
            }
        });
    }

    // --- 펀딩 로드 및 표시 함수 ---
    function loadAndDisplayFundings(storeId, initialStatus = 'all') {
        $.ajax({
            url: `${cpath}/seller/store/fundings/byStore?storeId=${storeId}`,
            method: 'GET',
            dataType: 'json',
            success: function(fundings) {
                allFundings = fundings;
                updateFundingCounts(); // 전체 통계 업데이트
                displayFundings(initialStatus); // 초기 상태 (all)로 펀딩 표시
                setActiveTab(initialStatus); // 초기 탭 활성화 (all)
            },
            error: function(xhr, status, error) {


                let errorMessage = '펀딩 정보를 불러오지 못했습니다.';
                if (xhr && xhr.responseText) {
                    errorMessage += ` (서버 응답: ${xhr.responseText})`;
                } else if (error) {
                    errorMessage += ` (오류 상세: ${error})`;
                } else if (status) {
                    errorMessage += ` (상태: ${status})`;
                }
                if (fundingListContainer) fundingListContainer.innerHTML = `<p>${errorMessage}</p>`;
                allFundings = [];
                updateFundingCounts();
            }
        });
    }

    // --- 펀딩 카드 생성 및 표시 함수 ---
    function displayFundings(statusToFilter) {
        let filteredFundings = [];
        if (statusToFilter === 'all') {
            filteredFundings = allFundings;
        } else if (statusToFilter === '종료') {
            // "종료" 상태는 '성공' 또는 '실패'를 포함
            filteredFundings = allFundings.filter(f => f.status === '성공' || f.status === '실패');
        } else {
            filteredFundings = allFundings.filter(f => f.status === statusToFilter);
        }

        if (fundingListContainer) fundingListContainer.innerHTML = ''; // 기존 내용 지우기

        if (filteredFundings.length === 0) {
            if (fundingListContainer) fundingListContainer.innerHTML = `<p>이 상태에 해당하는 펀딩이 없습니다.</p>`;
            // 필터링된 개수를 0으로 업데이트 (상단 요약)
            $('#currentFundingCount').text(0);
            $('#currentFilterStatus').text(getDisplayStatusText(statusToFilter));
              $('#currentFilterStatus, #currentFundingCount') // 두 요소 동시 선택
            .removeClass('status-in-progress status-scheduled status-ended status-all') // 모든 상태 클래스 제거
            .addClass(statusClass); // 새로운 상태 클래스 추가
        return;
        }

        filteredFundings.forEach((f, index) => {
            const name = f.fundingName || '이름 없음';
            const current = Number(f.currentQty || 0);
            const target = Number(f.targetQty || 1);
            const rate = Math.floor((current * 100) / target);

            const startDate = f.startDate ? new Date(f.startDate).toLocaleDateString('ko-KR') : '';
            const endDate = f.endDate ? new Date(f.endDate).toLocaleDateString('ko-KR') : '';
            const status = f.status || '상태 없음';

            const fundingCard = document.createElement('div');
            fundingCard.className = 'funding-card';
            fundingCard.dataset.fundingId = f.fundingId;

            fundingCard.innerHTML = `
                <div class="funding-info">
                    <h3 class="funding-title">${index + 1}. ${name}</h3>
                    <div class="funding-date">${startDate} ~ ${endDate}</div>
                    <div class="progress-bar-wrapper">
                        <div class="progress-bar-container">
                            <div class="progress-bar" style="width: ${rate}%"></div>
                        </div>
                        <div class="progress-percentage">${rate}%</div>
                    </div>
                </div>
                <span class="funding-status ${getStatusClass(status)}">
            ${(status === '성공' || status === '실패') ? '종료' : status}
        </span>
            `;

            fundingCard.addEventListener('click', function() {
                const id = this.dataset.fundingId;
                if (id) {
                    window.location.href = `${cpath}/seller/store/funding/stats?fundingId=${id}`;
                }
            });

            if (fundingListContainer) fundingListContainer.appendChild(fundingCard);
        });

        // 필터링된 개수와 상태 텍스트 업데이트 (상단 요약)
        $('#currentFundingCount').text(filteredFundings.length);
        $('#currentFilterStatus').text(getDisplayStatusText(statusToFilter));
          $('#currentFilterStatus, #currentFundingCount') // 두 요소 동시 선택
        .removeClass('status-in-progress status-scheduled status-ended status-all') // 모든 상태 클래스 제거
        .addClass(getStatusClassForFilterStatus(statusToFilter)); // 새로운 상태 클래스 추가
    }

    // --- 헬퍼 함수들 ---
    function getStatusClass(status) {
        if (status === '진행중') return 'status-in-progress';
        if (status === '준비중') return 'status-scheduled';
        // '종료' 외에 '성공', '실패'도 종료로 간주
        if (status === '성공' || status === '실패') return 'status-ended';
        return 'status-ended'; // 기본값 (알 수 없는 상태는 종료로 처리)
    }
    
    function getStatusClassForFilterStatus(status) {
    if (status === '진행중') return 'status-in-progress';
    if (status === '준비중') return 'status-scheduled';
    if (status === '종료') return 'status-ended'; // 탭의 '종료' 상태에 대응
    if (status === 'all') return 'status-all'; // '전체' 탭에 대한 클래스
    return ''; // 기본값 (클래스 없음)
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

    // 상단 요약에 표시될 텍스트를 반환하는 헬퍼 함수
    function getDisplayStatusText(status) {
        if (status === 'all') return '전체';
        if (status === '진행중') return '진행 중인';
        if (status === '준비중') return '준비 중인';
        if (status === '종료') return '종료된'; // '종료' 탭 클릭 시 표시될 텍스트
        return status; // 그 외의 경우
    }


    // --- 펀딩 개수 요약 정보 업데이트 함수 (모든 펀딩 기준) ---
    function updateFundingCounts() {
        if (!fundingCountSummary) {
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
    }

    // ★★★ 탭 버튼 클릭 이벤트 리스너 (기존 로직과 통합) ★★★
    // 이전에 중복되었던 탭 클릭 로직을 제거하고, loadAndDisplayFundings 호출 후
    // 펀딩 데이터를 기반으로 탭 클릭 이벤트 리스너를 다시 설정합니다.

    // 탭 클릭 이벤트는 HTML에 직접 data-status가 있으므로,
    // 이 위치에 이벤트 리스너를 한 번만 정의하면 됩니다.
    $('.funding-tabs button').on('click', function() {
        // 모든 탭의 active 클래스 제거
        $('.funding-tabs button').removeClass('active');
        // 클릭된 탭에 active 클래스 추가
        $(this).addClass('active');

        // 선택된 상태 값 가져오기
        const selectedStatus = $(this).data('status');


        displayFundings(selectedStatus);
    });


}); 