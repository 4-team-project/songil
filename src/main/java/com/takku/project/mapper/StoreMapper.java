package com.takku.project.mapper;

import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;

public interface StoreMapper {

	//���� ���
	int insertStore(StoreDTO store);

    // 2. ���� �� ��ȸ
	StoreDTO selectStoreById(Integer storeId);

    // 3. ���� ���� ����
	int updateStore(StoreDTO store);

    // 4. ���� ����
	int deleteStore(Integer storeId);
}
