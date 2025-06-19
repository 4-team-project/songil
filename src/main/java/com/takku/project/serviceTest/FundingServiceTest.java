package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.takku.project.domain.FundingDTO;
import com.takku.project.service.FundingService;

public class FundingServiceTest {

    @Mock
    private SqlSession sqlSession;

    @InjectMocks
    private FundingService fundingService;

    private final String namespace = "com.takku.project.mapper.FundingMapper.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void selectAllFunding_shouldReturnList() {
        List<FundingDTO> mockList = Arrays.asList(new FundingDTO(), new FundingDTO());

        doReturn(mockList).when(sqlSession).selectList(namespace + "selectAllFunding");

        List<FundingDTO> result = fundingService.selectAllFunding();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sqlSession).selectList(namespace + "selectAllFunding");
    }

    @Test
    void selectFundingByFundingId_shouldReturnFunding() {
        Integer fundingId = 1;
        FundingDTO mockFunding = new FundingDTO();
        mockFunding.setFundingId(fundingId);

        when(sqlSession.selectOne(namespace + "selectByFundingId", fundingId)).thenReturn(mockFunding);

        FundingDTO result = fundingService.selectFundingByFundingId(fundingId);

        assertNotNull(result);
        assertEquals(fundingId, result.getFundingId());
        verify(sqlSession).selectOne(namespace + "selectByFundingId", fundingId);
    }

    @Test
    void selectFundingByCondition_shouldReturnFilteredList() {
        String keyword = "test";
        Integer categoryId = 1;
        String sido = "서울";
        String sigungu = "강남구";

        List<FundingDTO> mockList = Arrays.asList(new FundingDTO());

        doReturn(mockList).when(sqlSession)
            .selectList(eq(namespace + "selectFundingByCondition"), anyMap());

        List<FundingDTO> result = fundingService.selectFundingByCondition(keyword, categoryId, sido, sigungu);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(sqlSession).selectList(eq(namespace + "selectFundingByCondition"), anyMap());
    }

    @Test
    void insertFunding_shouldReturnInsertResult() {
        FundingDTO funding = new FundingDTO();

        when(sqlSession.insert(namespace + "insertFunding", funding)).thenReturn(1);

        int result = fundingService.insertFunding(funding);

        assertEquals(1, result);
        verify(sqlSession).insert(namespace + "insertFunding", funding);
    }

   
    @Test
    void updateFunding_shouldReturnUpdateResult() {
        FundingDTO funding = new FundingDTO();

        when(sqlSession.insert(namespace + "updateFunding", funding)).thenReturn(1); // 주의: insert로 되어 있음

        int result = fundingService.updateFunding(funding);

        assertEquals(1, result);
        verify(sqlSession).insert(namespace + "updateFunding", funding);
    }
    
    @Test
    void deleteFunding_shouldReturnDeleteResult() {
        Integer fundingId = 1;

        when(sqlSession.delete(namespace + "deleteFunding", fundingId)).thenReturn(1);

        int result = fundingService.deleteFunding(fundingId);

        assertEquals(1, result);
        verify(sqlSession).delete(namespace + "deleteFunding", fundingId);
    }
}
