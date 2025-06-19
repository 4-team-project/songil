package com.takku.project.serviceTest;

import com.takku.project.domain.SettlementDTO;
import com.takku.project.service.SettlementService;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SettlementServiceTest {

    @InjectMocks
    private SettlementService settlementService;

    @Mock
    private SqlSession sqlSession;

    private final String namespace = "com.takku.project.mapper.SettlementMapper.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insertSettlement_shouldReturn1() {
        SettlementDTO dto = getTestDTO();

        when(sqlSession.insert(namespace + "insertSettlement", dto)).thenReturn(1);

        int result = settlementService.insertSettlement(dto);

        assertEquals(1, result);
        verify(sqlSession).insert(namespace + "insertSettlement", dto);
    }

    @Test
    void selectSettlementByStoreId_shouldReturnList() {
        List<Object> expected = Arrays.asList(getTestDTO(), getTestDTO());
        Integer storeId = 5;

        when(sqlSession.selectList(namespace + "getSettlementByStoreId", storeId)).thenReturn(expected);

        List<SettlementDTO> result = settlementService.selectSettlementByStoreId(storeId);

        assertEquals(2, result.size());
        verify(sqlSession).selectList(namespace + "getSettlementByStoreId", storeId);
    }

    @Test
    void updateSettlementStatus_shouldReturn1() {
        Map<String, Object> params = new HashMap<>();
        params.put("settlementId", 10);
        params.put("status", "완료");

        when(sqlSession.update(namespace + "updateSettlementStatus", params)).thenReturn(1);

        int result = settlementService.updateSettlementStatus(params);

        assertEquals(1, result);
        verify(sqlSession).update(namespace + "updateSettlementStatus", params);
    }

    @Test
    void selectSettlementById_shouldReturnDto() {
        SettlementDTO expected = getTestDTO();
        Integer settlementId = 3;

        when(sqlSession.selectOne(namespace + "getSettlementById", settlementId)).thenReturn(expected);

        SettlementDTO result = settlementService.selectSettlementById(settlementId);

        assertNotNull(result);
        assertEquals(expected.getAmount(), result.getAmount());
        verify(sqlSession).selectOne(namespace + "getSettlementById", settlementId);
    }

    private SettlementDTO getTestDTO() {
        return SettlementDTO.builder()
                .settlementId(1)
                .fundingId(2)
                .storeId(3)
                .fee(500)
                .amount(10000)
                .status("대기")
                .build();
    }
}
