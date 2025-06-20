package com.takku.project.controllerTest;

import com.takku.project.controller.CouponController;
import com.takku.project.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CouponControllerTest {

    @InjectMocks
    private CouponController couponController;

    @Mock
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 쿠폰 사용 처리 테스트
    @Test
    void useCoupon_shouldRedirectToCouponPage() {
        // given
        String couponCode = "ABC123";

        // when
        String result = couponController.useCoupon(couponCode);

        // then
        assertEquals("redirect:/mypage/coupon", result);
        verify(couponService).updateCouponUseStatus(couponCode, "사용됨");
    }

    // 리뷰 상태 업데이트 테스트
    @Test
    void markReviewed_shouldRedirectToCouponPage() {
        // given
        Integer couponId = 1001;

        // when
        String result = couponController.markReviewed(couponId);

        // then
        assertEquals("redirect:/mypage/coupon", result);
        verify(couponService).updateCouponReviewed(couponId, 1);
    }
}