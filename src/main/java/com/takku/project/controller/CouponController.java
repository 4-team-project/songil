package com.takku.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CouponController {

    @GetMapping("/user/coupon")
    public String homePage() {
        return "pages/user/coupon"; 
    }
}
