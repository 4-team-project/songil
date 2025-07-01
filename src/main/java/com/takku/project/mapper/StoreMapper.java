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

	// 1. 오늘 참여 수
	int countTodayOrdersByStoreId(int storeId);

	// 2. 오늘 매출 합계
	Integer sumTodaySalesByStoreId(int storeId);

	// 3. 진행 중인 펀딩 수
	int countOngoingFundingsByStoreId(int storeId);

	// 4. 진행 예정인 펀딩 수
	int countUpcomingFundingsByStoreId(int storeId);

	// 사용자 상점 전체 조회
	List<StoreDTO> selectStoreListByUserId(int userId);

}
