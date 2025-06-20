package com.takku.project.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.controller.StoreController;
import com.takku.project.domain.StoreDTO;
import com.takku.project.service.StoreService;

public class StoreControllerTest {

    @InjectMocks
    private StoreController storeController;

    @Mock
    private StoreService storeService;

    @Mock
    private RedirectAttributes redirectAttributes;
    
    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //상점 등록 테스트
    @Test
    void insertStore_shouldRedirectToList() {
        // given
        StoreDTO storeDTO = StoreDTO.builder()
        	.userId(1000)
        	.businessNumber("test")
            .storeName("테스트 상점")
            .sido("test")
            .sigungu("test")
            .categoryId(99)
            .description("테스트 설명")
            .build();

        when(storeService.insertStore(storeDTO)).thenReturn(1);

        // when
        String result = storeController.insertStore(storeDTO, redirectAttributes);

        // then
        assertEquals("redirect:/seller/store/list", result);
        verify(storeService).insertStore(storeDTO);
        verify(redirectAttributes).addFlashAttribute("resultMessage", "상점이 등록되었습니다.");
    }
    
    //상점 수정 폼 테스트
    @Test
    void showEditForm_shouldReturnStoreEdit() {
        // given
        Integer storeId = 8;
        StoreDTO store = StoreDTO.builder().storeId(storeId).storeName("변경 테스트").build();

        when(storeService.selectStoreById(storeId)).thenReturn(store);

        // when
        String result = storeController.showEditForm(storeId, model);

        // then
        assertEquals("store_edit", result);
        verify(storeService).selectStoreById(storeId);
        verify(model).addAttribute("storeDTO", store);
    }
    
    //상점 수정 처리 테스트
    @Test
    void updateStore_shouldRedirectSsellerStoreListStoreId() {
        // given
        Integer storeId = 8;
        StoreDTO store = StoreDTO.builder().storeId(storeId).storeName("변경 테스트").description("설명 변경 테스트").build();
        
        when(storeService.updateStore(store)).thenReturn(1);

        // when
        String result = storeController.updateStore(storeId, store, redirectAttributes);

        // then
        assertEquals("redirect:/seller/store/list?storeId=" + storeId, result);
        verify(storeService).updateStore(store);
        verify(redirectAttributes).addFlashAttribute("resultMessage", "상점이 수정되었습니다.");
    }
    
    //상점 삭제 처리 테스트
    @Test
    void deleteStore_shouldRedirectSellerStoreList() {
        // given
        Integer storeId = 8;
        
        
        when(storeService.deleteStore(storeId)).thenReturn(1);

        // when
        String result = storeController.deleteStore(storeId, redirectAttributes);

        // then
        assertEquals("redirect:/seller/store/list", result);
        verify(storeService).deleteStore(storeId);
        verify(redirectAttributes).addFlashAttribute("resultMessage", "상점이 삭제되었습니다.");
    }
    
    
}