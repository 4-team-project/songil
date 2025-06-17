package com.takku.project.mapper;

import java.util.List;


import com.takku.project.domain.CouponDTO;

public interface CouponMapper {


    // 쿠폰 발급 (insert)
    void insertCoupon(CouponDTO coupon);

    // 특정 사용자의 쿠폰 목록 조회
    List<CouponDTO> selectCouponsByUserId(Integer userId);

    // 쿠폰 사용 상태 업데이트
    void updateCouponUseStatus(Integer couponId, String useStatus);

    // 리뷰 여부 업데이트
    void updateCouponReviewed(Integer couponId, Integer reviewed);
    
    // 특정 쿠폰 상세 조회 (예: QR코드로 쿠폰 확인 등)
    CouponDTO selectCouponByCouponCode(String couponCode);
}