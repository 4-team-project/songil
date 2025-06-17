package com.takku.project.mapper;

import java.util.List;


import com.takku.project.domain.CouponDTO;

public interface CouponMapper {

    // 荑좏룿 諛쒓툒 (insert)
    void insertCoupon(CouponDTO coupon);

    // �듅�젙 �궗�슜�옄�쓽 荑좏룿 紐⑸줉 議고쉶
    List<CouponDTO> selectCouponsByUserId(Integer userId);

    // 荑좏룿 �궗�슜 �긽�깭 �뾽�뜲�씠�듃
    void updateCouponUseStatus(Integer couponId, String useStatus);

    // 由щ럭 �뿬遺� �뾽�뜲�씠�듃
    void updateCouponReviewed(Integer couponId, Integer reviewed);
    
    // �듅�젙 荑좏룿 �긽�꽭 議고쉶 (�삁: QR 肄붾뱶濡� 荑좏룿 �솗�씤 �벑)
    CouponDTO selectByCouponCode(String couponCode);
}