package com.takku.project.controllerTest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.takku.project.controller.FundingController;
import com.takku.project.domain.FundingDTO;
import com.takku.project.service.FundingService;

public class FundingControllerTest {

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
    @DisplayName("조건 검색 있을 때 필터링된 펀딩 리스트 반환")
    void getFundings_test() throws Exception {
        FundingDTO funding = new FundingDTO();
        funding.setFundingId(1);
        funding.setFundingName("테스트 펀딩");

        when(fundingService.selectFundingByCondition(anyString(), any(), anyString(), anyString())).thenReturn(Arrays.asList(funding));

        mockMvc.perform(get("/fundings")
                        .param("keyword", "test")
                        .param("categoryId", "1")
                        .param("sido", "서울")
                        .param("sigungu", "강남구"))
                .andExpect(model().attributeExists("fundinglist"))
                .andExpect(view().name("user/main"));
    }

    @Test
    @DisplayName("조건 없이 모든 펀딩 리스트 반환")
    void getFundings_test2() throws Exception {
        FundingDTO funding = new FundingDTO();
        funding.setFundingId(2);
        funding.setFundingName("전체 펀딩");

        when(fundingService.selectAllFunding())
                .thenReturn(Arrays.asList(funding));

        mockMvc.perform(get("/fundings"))
                .andExpect(model().attributeExists("fundinglist"))
                .andExpect(view().name("user/main"));
    }

    @Test
    @DisplayName("상세 페이지 조회 성공")
    void getFundingDetail_test3() throws Exception {
        FundingDTO funding = new FundingDTO();
        funding.setFundingId(1);
        funding.setFundingName("인기순");

        when(fundingService.selectFundingByFundingId(1))
                .thenReturn(funding);

        mockMvc.perform(get("/fundings/1"))
                .andExpect(model().attributeExists("funding"))
                .andExpect(view().name("user/main_detail"));
    }

    @Test
    @DisplayName("상세 페이지 조회 실패 (펀딩 없음)")
    void getFundingDetail_notFound_shouldReturnErrorPage() throws Exception {
        when(fundingService.selectFundingByFundingId(99)).thenReturn(null);

        mockMvc.perform(get("/fundings/99"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"));
    }
}
