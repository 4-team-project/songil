package com.takku.project.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.controller.SettlementController;
import com.takku.project.domain.SettlementDTO;
import com.takku.project.service.SettlementService;
import com.takku.project.service.StoreService;

public class SettlementControllerTest {

    @InjectMocks
    private SettlementController settlementController;

    @Mock
    private StoreService storeService;
    
    @Mock
    private SettlementService settlementService;

    @Mock
    private RedirectAttributes redirectAttributes;
    
    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //정산 목록 테스트
    @Test
    void getSettlement_shouldReturnMypage() {
        // given
    	Integer storeId = 8;
    	
    	List<SettlementDTO> settlementList = Arrays.asList(
    			SettlementDTO.builder().fundingId(1).storeId(storeId).amount(5000).build(),
    			SettlementDTO.builder().fundingId(2).storeId(storeId).amount(4000).build()
            );
    	

        when(settlementService.selectSettlementByStoreId(storeId)).thenReturn(settlementList);

        // when
        String viewName = settlementController.getSettlement(model, storeId);

        // then
        assertEquals("seller_settlement", viewName);
        verify(settlementService).selectSettlementByStoreId(storeId);
        verify(model).addAttribute("settlementList", settlementList);
    }
    
}