package com.takku.project.mapper;

import com.takku.project.domain.StoreDTO;

public interface StoreMapper {

	//占쏙옙占쏙옙 占쏙옙占�
	int insertStore(StoreDTO store);

    // 2. 占쏙옙占쏙옙 占쏙옙 占쏙옙회
	StoreDTO selectStoreById(Integer storeId);

    // 3. 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙
	int updateStore(StoreDTO store);

    // 4. 占쏙옙占쏙옙 占쏙옙占쏙옙
	int deleteStore(Integer storeId);
}
