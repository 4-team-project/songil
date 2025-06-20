package com.takku.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CouponController {

    @Autowired
    private CouponService couponService;
  
    @GetMapping("/user/coupon")
    public String homePage() {
        return "pages/user/coupon"; 
    }
  
    //쿠폰 사용 처리
    @PostMapping("/{couponCode}/use")
    public String useCoupon(@PathVariable("couponCode") String couponCode) {
        couponService.updateCouponUseStatus(couponCode, "사용됨");
        return "redirect:/mypage/coupon";
    }

    //리뷰 작성 후, 해당 쿠폰 리뷰 상태 업데이트
    @PostMapping("/{couponId}/reviewed")
    public String markReviewed(@PathVariable("couponId") Integer couponId) {
        couponService.updateCouponReviewed(couponId, 1);
        return "redirect:/mypage/coupon";
    }
}