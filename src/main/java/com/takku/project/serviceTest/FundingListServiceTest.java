package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.takku.project.domain.FundingDTO;
import com.takku.project.mapper.FundingListMapper;
import com.takku.project.service.FundingListService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FundingListServiceTest {

    @Mock
    private FundingListMapper fundingListMapper;

    @InjectMocks
    private FundingListService fundingListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("펀딩정렬_최신순")
    void selectFundingListBySort() {

        List<FundingDTO> mockList = Arrays.asList(new FundingDTO());
        when(fundingListMapper.selectFundingListBySort("latest")).thenReturn(mockList);

        List<FundingDTO> result = fundingListService.selectFundingListBySort(null);

        assertEquals(mockList, result);
        verify(fundingListMapper).selectFundingListBySort("latest");
    }

    @Test
    @DisplayName("선택된 값에 따라 펀딩정렬")
    void selectFundingListBySort_withSort_shouldUseGivenSort() {
        List<FundingDTO> mockList = Arrays.asList(new FundingDTO());
        when(fundingListMapper.selectFundingListBySort("popular")).thenReturn(mockList);

        List<FundingDTO> result = fundingListService.selectFundingListBySort("popular");

        assertEquals(mockList, result);
        verify(fundingListMapper).selectFundingListBySort("popular");
    }

  
}
