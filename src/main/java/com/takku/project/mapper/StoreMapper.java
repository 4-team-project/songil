package com.takku.project.mapper;

import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;

public interface StoreMapper {

	//상점 등록
	void insertStore(StoreDTO store);

    // 2. 상점 상세 조회
	StoreDTO selectStoreById(Integer storeId);

    // 3. 상점 정보 수정
    void updateStore(StoreDTO store);

    // 4. 상점 삭제
    void deleteStore(Integer storeId);
}
