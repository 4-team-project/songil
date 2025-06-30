function sendSearchData() {
  const searchText = document.getElementById("searchText").value.trim();

  if (searchText !== "") {
    const encodedSearch = encodeURIComponent(searchText);

    if (typeof loadFundings === 'function') {
      window.currentPage = 1;
      
      const filters = document.querySelectorAll('.funding-filter');
      filters.forEach(f => f.classList.remove('selected'));
      
      const defaultFilter = document.querySelector('[data-sort-id="popular"]');
      if (defaultFilter) defaultFilter.classList.add('selected');
    
      const sidoBtn = document.getElementById('sidoButton');
      const sigunguBtn = document.getElementById('sigunguButton');
      if (sidoBtn) {
        sidoBtn.innerText = '시/도 선택';
        sidoBtn.classList.remove('selected');
      }
      if (sigunguBtn) {
        sigunguBtn.innerText = '시/군/구 선택';
        sigunguBtn.classList.remove('selected');
      }
      
      loadFundings({
    	  keyword: encodedSearch,
    	  sort: 'popular',
    	  sido: '',         
    	  sigungu: ''
    	});
    
    } else {
      window.location.href = `${cpath}/fundings/search/json?search=${encodedSearch}`;
    }
  }
}

document.getElementById("searchText").addEventListener("keydown", function (e) {
  if (e.key === "Enter") {
    sendSearchData();
  }
});