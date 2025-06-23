const regionData = {
  "서울특별시": ["강남구", "서초구", "종로구", "중구", "송파구", "영등포구"],
  "부산광역시": ["해운대구", "수영구", "동래구", "부산진구", "사하구"],
  "경기도": ["수원시", "용인시", "성남시", "안양시", "고양시"],
  // 다른 시/도도 추가로 넣기
};

window.onload = function () {
  const sidoSelect = document.getElementById("sidoSelect");
  const sigunguSelect = document.getElementById("sigunguSelect");

  // 시/도 채우기
  Object.keys(regionData).forEach(sido => {
    const option = document.createElement("option");
    option.value = sido;
    option.textContent = sido;
    sidoSelect.appendChild(option);
  });

  // 시/도 변경 시 시/군/구 채우기
  sidoSelect.addEventListener("change", () => {
    const selectedSido = sidoSelect.value;
    const sigungus = regionData[selectedSido] || [];

    sigunguSelect.innerHTML = '<option disabled selected>시/군/구 선택</option>';
    sigungus.forEach(sigungu => {
      const option = document.createElement("option");
      option.value = sigungu;
      option.textContent = sigungu;
      sigunguSelect.appendChild(option);
    });
  });
};
