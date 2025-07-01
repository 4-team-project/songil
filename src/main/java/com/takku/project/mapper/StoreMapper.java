package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.StoreDTO;

public interface StoreMapper {

	int insertStore(StoreDTO store);

	StoreDTO selectStoreById(Integer storeId);

	int updateStore(StoreDTO store);

	int deleteStore(Integer storeId);

	int countByBusinessNumber(String businessNumber);

	Integer findStoreIdByUserId(int userId);

	StoreDTO selectStoreNameByUserId(int userId);

	// 사용자 상점 전체 조회
	List<StoreDTO> selectStoreListByUserId(int userId);
}
