package com.takku.project.mapper;

import java.util.List;
import java.util.Map;

import com.takku.project.domain.SettlementDTO;

public interface SettlementMapper {

	// 1. ���� ���
	int insertSettlement(SettlementDTO vo);

    // 2. ���� ID�� ���� ���� ��ȸ
    List<SettlementDTO> getSettlementsByStoreId(Integer storeId);

    // 3. ���� ���� ����
    int updateSettlementStatus(Map<String, Object> params);

    // 4. ���� �� ��ȸ
    SettlementDTO getSettlementById(Integer settlementId);
}
