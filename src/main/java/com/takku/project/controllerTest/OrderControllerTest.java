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

import com.takku.project.controller.OrderController;
import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.OrderDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.OrderService;
import com.takku.project.service.ReviewService;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private FundingService fundingService;
    
    @Mock
    private OrderService orderService;
    
    @Mock
    private RedirectAttributes redirectAttributes;
    
    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    //주문 폼 테스트
    @Test
    void orderForm_shouldReturnOrderFormView() {
        // given
        Integer fundingId = 101;
        FundingDTO fundingDTO = FundingDTO.builder().fundingId(fundingId).fundingName("�׽�Ʈ �ݵ�").build();
        when(fundingService.selectFundingByFundingId(fundingId)).thenReturn(fundingDTO);

        // when
        String viewName = orderController.orderForm(fundingId, model);

        // then
        assertEquals("orderForm", viewName);
        verify(fundingService).selectFundingByFundingId(fundingId);
        verify(model).addAttribute("fundingDTO", fundingDTO);
    }
    
    //주문 처리 테스트
    @Test
    void processOrder_shouldRedirectMypageOrder() {
        // given
        OrderDTO orderDTO = OrderDTO.builder().orderId(1).userId(21).fundingId(101).build();
        when(orderService.insertOrder(orderDTO)).thenReturn(1);

        // when
        String viewName = orderController.processOrder(orderDTO, model);

        // then
        assertEquals("redirect:/mypage/order", viewName);
        verify(orderService).insertOrder(orderDTO);
        verify(model).addAttribute("resultMessage", "�ֹ��� ���������� �Ϸ�Ǿ����ϴ�.");
    }
    
   
}