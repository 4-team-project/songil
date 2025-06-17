package com.takku.project.mapper;

import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;

public interface StoreMapper {

	//���� ���
	void insertStore(StoreDTO store);

    // 2. ���� �� ��ȸ
	StoreDTO selectStoreById(Integer storeId);

    // 3. ���� ���� ����
    void updateStore(StoreDTO store);

    // 4. ���� ����
    void deleteStore(Integer storeId);
}
