package com.takku.project.controllerTest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.takku.project.controller.FundingController;
import com.takku.project.domain.FundingDTO;
import com.takku.project.service.FundingService;

class FundingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FundingService fundingService;

    @InjectMocks
    private FundingController fundingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(fundingController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    @DisplayName("펀딩 검색 결과 반환")
    void searchFunding_shouldReturnFilteredList() throws Exception {
        FundingDTO funding = new FundingDTO();
        funding.setFundingId(1);
        funding.setFundingName("테스트");

        when(fundingService.getFundingsByConditionWithPaging(any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(funding));
        when(fundingService.getFundingCountByCondition(any(), any(), any(), any()))
                .thenReturn(1);

        mockMvc.perform(get("/fundings/search")
                .param("keyword", "테스트")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("fundinglist"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("pages/user/funding_search"));
    }

    @Test
    @DisplayName("전체 펀딩 목록 조회")
    void getAllFundings_shouldReturnList() throws Exception {
        FundingDTO funding = new FundingDTO();
        funding.setFundingId(1);

        when(fundingService.getFundingsByConditionWithPaging(any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(funding));
        when(fundingService.getFundingCountByCondition(any(), any(), any(), any()))
                .thenReturn(1);

        mockMvc.perform(get("/fundings"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("fundinglist"))
                .andExpect(view().name("pages/user/home"));
    }

    @Test
    @DisplayName("펀딩 상세 조회 성공")
    void getFundingDetail_shouldReturnPage() throws Exception {
        FundingDTO funding = new FundingDTO();
        funding.setFundingId(1);

        when(fundingService.selectFundingByFundingId(1)).thenReturn(funding);

        mockMvc.perform(get("/fundings/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("funding"))
                .andExpect(view().name("pages/user/funding_detail"));
    }

    @Test
    @DisplayName("펀딩 상세 조회 실패 - 없는 ID")
    void getFundingDetail_shouldReturnErrorPageWhenNull() throws Exception {
        when(fundingService.selectFundingByFundingId(99)).thenReturn(null);

        mockMvc.perform(get("/fundings/99"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"));
    }
}
