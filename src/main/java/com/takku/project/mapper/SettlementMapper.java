package com.takku.project.mapper;

import java.util.List;
import java.util.Map;

import com.takku.project.domain.SettlementDTO;

public interface SettlementMapper {

	// 1. 정산 등록
    void insertSettlement(SettlementDTO vo);

    // 2. 상점 ID로 정산 내역 조회
    List<SettlementDTO> getSettlementsByStoreId(Integer storeId);

    // 3. 정산 상태 변경
    void updateSettlementStatus(Map<String, Object> params);

    // 4. 정산 상세 조회
    SettlementDTO getSettlementById(Integer settlementId);
}
