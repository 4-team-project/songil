package com.takku.project.controllerTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.takku.project.controller.FundingListController;
import com.takku.project.domain.FundingDTO;
import com.takku.project.service.FundingListService;

public class FundingListControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FundingListService fundingListService;

    @InjectMocks
    private FundingListController fundingListController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // JSP 뷰 리졸버 설정 ("/WEB-INF/views/funding_list.jsp" 형식 지원)
        InternalResourceViewResolver rr = new InternalResourceViewResolver();
        rr.setPrefix("/WEB-INF/views/");
        rr.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(fundingListController)
                .setViewResolvers(rr)
                .build();
    }

    @Test
    void selectFundingList_withSort_shouldReturnFundingListView() throws Exception {
        FundingDTO testFunding = new FundingDTO();
        testFunding.setFundingName("테스트 펀딩");

        when(fundingListService.selectFundingListBySort("popular"))
                .thenReturn(Arrays.asList(testFunding));

        mockMvc.perform(post("/fundings")
                .param("sort", "popular"))
                .andExpect(status().isOk())
                .andExpect(view().name("funding_list"))
                .andExpect(model().attributeExists("fundingList"));
    }

    @Test
    void selectFundingList_withoutSort_shouldUseDefaultSort() throws Exception {
        FundingDTO dummyFunding = new FundingDTO();
        dummyFunding.setFundingName("기본 정렬 펀딩");

        when(fundingListService.selectFundingListBySort("latest"))
                .thenReturn(Arrays.asList(dummyFunding));

        mockMvc.perform(post("/fundings"))
                .andExpect(status().isOk())
                .andExpect(view().name("funding_list"))
                .andExpect(model().attributeExists("fundingList"));
    }

    @Test
    void sortFundingList_shouldReturnJsonList() throws Exception {
        FundingDTO dummyFunding = new FundingDTO();
        dummyFunding.setFundingId(1);
        dummyFunding.setFundingName("API 펀딩");

        when(fundingListService.selectFundingListBySort(anyString()))
                .thenReturn(Arrays.asList(dummyFunding));

        mockMvc.perform(get("/fundings/sort")
                .param("sort", "recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fundingId").value(1))
                .andExpect(jsonPath("$[0].fundingName").value("API 펀딩"));
    }
}
