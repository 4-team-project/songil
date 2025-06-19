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

import com.takku.project.controller.UserController;
import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.OrderDTO;
import com.takku.project.domain.ReviewDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.CouponService;
import com.takku.project.service.OrderService;
import com.takku.project.service.ReviewService;
import com.takku.project.service.UserService;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    
    @Mock
    private ReviewService reviewService;
    
    @Mock
    private CouponService couponService;
    
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
    
    

    //마이페이지 메인 (내 정보 보기) 테스트
    @Test
    void showmyPage_shouldReturnMypage() {
        // given
    	Integer userId = 21;
    	UserDTO loginUser = UserDTO.builder().userId(userId).nickname("테스트 유저").build();
    	UserDTO mockDbUser = UserDTO.builder().userId(userId).nickname("DB에서 조회된 유저").build();
    	

        when(userService.selectByUserId(userId)).thenReturn(mockDbUser);

        // when
        String viewName = userController.myPage(loginUser, model);

        // then
        assertEquals("mypage", viewName);
        verify(userService).selectByUserId(userId);
        verify(model).addAttribute("user", mockDbUser);
    }
    
    
    //회원정보 수정 폼 테스트
    @Test
    void editForm_shouldReturnMypageEdit() {
        // given
    	Integer userId = 21;
    	UserDTO loginUser = UserDTO.builder().userId(userId).nickname("테스트 유저").build();
    	UserDTO mockDbUser = UserDTO.builder().userId(userId).nickname("DB에서 조회된 유저").build();
    	

        when(userService.selectByUserId(userId)).thenReturn(mockDbUser);

        // when
        String viewName = userController.editForm(loginUser, model);

        // then
        assertEquals("mypage_edit", viewName);
        verify(userService).selectByUserId(userId);
        verify(model).addAttribute("user", mockDbUser);
    }
    
 
    //회원정보 수정 처리 테스트
    @Test
    void updateUser_shouldRedirectMypage() {
        // given
        Integer userId = 21;
        UserDTO loginUser = UserDTO.builder().userId(userId).build();
        UserDTO user = UserDTO.builder().nickname("변경 테스트").build();
        
        when(userService.updateUser(user)).thenReturn(1);

        // when
        String result = userController.updateUser(loginUser, user, redirectAttributes);

        // then
        assertEquals("redirect:/mypage", result);
        verify(userService).updateUser(user);
        verify(redirectAttributes).addFlashAttribute("resultMessage", "회원정보가 수정되었습니다.");
    }
    
    //내 리뷰 관리 테스트
    @Test
    void myReviews_shouldReturnMypageReviews() {
        // given
    	Integer userId = 21;
    	UserDTO loginUser = UserDTO.builder().userId(userId).nickname("테스트 유저").build();
    	
    	List<ReviewDTO> mockReviewList = Arrays.asList(
                ReviewDTO.builder().reviewId(1).userId(userId).rating(5).build(),
                ReviewDTO.builder().reviewId(2).userId(userId).rating(4).build()
            );
    	

        when(reviewService.reviewByUserID(userId)).thenReturn(mockReviewList);

        // when
        String viewName = userController.myReviews(loginUser, model);

        // then
        assertEquals("mypage_reviews", viewName);
        verify(reviewService).reviewByUserID(userId);
        verify(model).addAttribute("reviewList", mockReviewList);
    }
    
    //내 쿠폰함 테스트
    @Test
    void myCoupons_shouldReturnMypageCoupons() {
        // given
        Integer userId = 21;
        UserDTO loginUser = UserDTO.builder()
            .userId(userId)
            .nickname("테스트유저")
            .build();

        List<CouponDTO> mockCouponList = Arrays.asList(
            CouponDTO.builder().couponId(1).userId(userId).couponCode("ABC123").build(),
            CouponDTO.builder().couponId(2).userId(userId).couponCode("XYZ456").build()
        );

        when(couponService.selectCouponByUserId(userId)).thenReturn(mockCouponList);

        // when
        String viewName = userController.myCoupons(loginUser, model);

        // then
        assertEquals("mypage_coupons", viewName);
        verify(couponService).selectCouponByUserId(userId);
        verify(model).addAttribute("couponList", mockCouponList);
    }
    
    //6.내 주문내역 조회
    @Test
    void myOrders_shouldReturnMypageOrders() {
        // given
        Integer userId = 21;
        UserDTO loginUser = UserDTO.builder()
            .userId(userId)
            .nickname("테스트유저")
            .build();

        List<OrderDTO> mockOrderList = Arrays.asList(
            OrderDTO.builder().orderId(1).userId(userId).build(),
            OrderDTO.builder().orderId(2).userId(userId).build()
        );

        when(orderService.selectByUserId(userId)).thenReturn(mockOrderList);

        // when
        String viewName = userController.myOrders(loginUser, model);

        // then
        assertEquals("mypage_orders", viewName);
        verify(orderService).selectByUserId(userId);
        verify(model).addAttribute("orderList", mockOrderList);
    }
    
}