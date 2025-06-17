package com.takku.project.mapper;

import java.util.List;


import com.takku.project.domain.CouponDTO;

public interface CouponMapper {

    // ���� �߱� (insert)
    void insertCoupon(CouponDTO coupon);

    // Ư�� ������� ���� ��� ��ȸ
    List<CouponDTO> selectCouponsByUserId(Integer user_id);

    // ���� ��� ���� ������Ʈ
    void updateCouponUseStatus(Integer coupon_id, String use_status);

    // ���� ���� ������Ʈ
    void updateCouponReviewed(Integer coupon_id, Integer reviewed);
    
    // Ư�� ���� �� ��ȸ (��: QR�ڵ�� ���� Ȯ�� ��)
    CouponDTO selectByCouponCode(String coupon_code);
}