package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.takku.project.domain.CouponDTO;
import com.takku.project.service.CouponService;

public class CouponServiceTest {

    @Mock
    private SqlSession sqlSession;

    @InjectMocks
    private CouponService couponService;

    private final String namespace = "com.takku.project.mapper.CouponMapper.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("쿠폰 입력")
    void insertCoupon() {
        CouponDTO coupon = CouponDTO.builder()
            .couponId(1)
            .couponCode("TEST")
            .useStatus("NEW")
            .userId(100)
            .createdAt(new Date(System.currentTimeMillis()))
            .expiredAt(new Date(System.currentTimeMillis() + 86400000))
            .build();

        when(sqlSession.insert(namespace + "insertCoupon", coupon)).thenReturn(1);

        int result = couponService.insertCoupon(coupon);

        assertEquals(1, result);
        verify(sqlSession).insert(namespace + "insertCoupon", coupon);
    }

    @Test
    @DisplayName("사용자ID로 쿠폰 조회")
    void selectCouponByUserId() {
        Integer userId = 100;
        List<Object> mockList = Arrays.asList(
            CouponDTO.builder().couponId(1).userId(userId).build(),
            CouponDTO.builder().couponId(2).userId(userId).build()
        );

       when(sqlSession.selectList(namespace + "selectCouponByUserId", userId)).thenReturn(mockList);

        List<CouponDTO> result = couponService.selectCouponByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sqlSession).selectList(namespace + "selectCouponByUserId", userId);
    }

    @Test
    @DisplayName("쿠폰 상태 수정")
    void updateCouponUseStatus() {
        String couponCode = "ABC123";
        String useStatus = "사용됨";

        when(sqlSession.update(eq(namespace + "updateCouponUseStatus"), any(Map.class))).thenReturn(1);

        int result = couponService.updateCouponUseStatus(couponCode, useStatus);

        assertEquals(1, result);
        verify(sqlSession).update(eq(namespace + "updateCouponUseStatus"), any(Map.class));
    }

    @Test
    @DisplayName("쿠폰 리뷰 수정")
    void updateCouponReviewed() {
        Integer couponId = 1;
        Integer reviewed = 1;

        when(sqlSession.update(eq(namespace + "updateCouponReviewed"), any(Map.class))).thenReturn(1);

        int result = couponService.updateCouponReviewed(couponId, reviewed);

        assertEquals(1, result);
        verify(sqlSession).update(eq(namespace + "updateCouponReviewed"), any(Map.class));
    }

    @Test
    @DisplayName("쿠폰코드 조회")
    void selectByCouponCode() {
        String couponCode = "TESTCODE";
        CouponDTO coupon = CouponDTO.builder()
            .couponId(1)
            .couponCode(couponCode)
            .build();

        when(sqlSession.selectOne(namespace + "selectByCouponCode", couponCode)).thenReturn(coupon);

        CouponDTO result = couponService.selectByCouponCode(couponCode);

        assertNotNull(result);
        assertEquals(couponCode, result.getCouponCode());
        verify(sqlSession).selectOne(namespace + "selectByCouponCode", couponCode);
    }
}
