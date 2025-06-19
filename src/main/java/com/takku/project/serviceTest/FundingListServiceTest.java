package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.takku.project.domain.FundingDTO;
import com.takku.project.mapper.FundingListMapper;
import com.takku.project.service.FundingListService;

import org.junit.jupiter.api.BeforeEach;
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
    void selectFundingListBySort_withNull_shouldUseDefaultSort() {
        // given
        List<FundingDTO> mockList = Arrays.asList(new FundingDTO());
        when(fundingListMapper.selectFundingListBySort("latest")).thenReturn(mockList);

        // when
        List<FundingDTO> result = fundingListService.selectFundingListBySort(null);

        // then
        assertEquals(mockList, result);
        verify(fundingListMapper).selectFundingListBySort("latest");
    }

    @Test
    void selectFundingListBySort_withSort_shouldUseGivenSort() {
        // given
        List<FundingDTO> mockList = Arrays.asList(new FundingDTO());
        when(fundingListMapper.selectFundingListBySort("popular")).thenReturn(mockList);

        // when
        List<FundingDTO> result = fundingListService.selectFundingListBySort("popular");

        // then
        assertEquals(mockList, result);
        verify(fundingListMapper).selectFundingListBySort("popular");
    }

    @Test
    void selectFundingListBySort_withBlank_shouldUseDefaultSort() {
        // given
        List<FundingDTO> mockList = Arrays.asList(new FundingDTO());
        when(fundingListMapper.selectFundingListBySort("latest")).thenReturn(mockList);

        // when
        List<FundingDTO> result = fundingListService.selectFundingListBySort("   ");

        // then
        assertEquals(mockList, result);
        verify(fundingListMapper).selectFundingListBySort("latest");
    }
}
