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

import com.takku.project.controller.ReviewController;
import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.ReviewDTO;
import com.takku.project.service.CouponService;
import com.takku.project.service.ReviewService;

public class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private CouponService couponService;
    
    @Mock
    private RedirectAttributes redirectAttributes;
    
    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    //리뷰 작성폼 테스트
    @Test
    void reviewForm_shouldReturnStoreEdit() {
        // given
    	String couponId = "abc123";
        CouponDTO coupon = CouponDTO.builder().userId(21).couponCode(couponId).build();

        when(couponService.selectByCouponCode(couponId)).thenReturn(coupon);

        // when
        String viewName = reviewController.reviewForm(couponId, model);

        // then
        assertEquals("review_write", viewName);
        verify(couponService).selectByCouponCode(couponId);
        verify(model).addAttribute("couponDTO", coupon);
    }
    
    //리뷰 등록 처리 테스트
    @Test
    void submitReview_shouldRedirectMypageReview() {
        // given
    	Integer userId = 21;
        ReviewDTO review = ReviewDTO.builder().reviewId(1).userId(userId).rating(5).build();

        when(reviewService.insertReview(review)).thenReturn(1);

        // when
        String result = reviewController.submitReview(review, null, null, null, redirectAttributes);

        // then
        assertEquals("redirect:mypage/review", result);
        verify(reviewService).insertReview(review);
    }
    
    //상품 리뷰 조회 테스트
    @Test
    void productReviewList_shouldReturnReviewList() {
    	// given
        Integer productId = 1001;
        List<ReviewDTO> mockReviewList = Arrays.asList(
            ReviewDTO.builder().reviewId(1).productId(productId).rating(5).content("���ƿ�!").build(),
            ReviewDTO.builder().reviewId(2).productId(productId).rating(4).content("�����ƿ�").build()
        );

        when(reviewService.reviewByProductId(productId)).thenReturn(mockReviewList);

        // when
        String viewName = reviewController.productReviewList(productId, model);

        // then
        assertEquals("review_list", viewName);
        verify(reviewService).reviewByProductId(productId);
        verify(model).addAttribute("reviewList", mockReviewList);
    }
    
}