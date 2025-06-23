package com.takku.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController {

    @GetMapping("/user/mypage")
    public String myPage(Model model) {
        model.addAttribute("pageName", "마이페이지");
        return "user.mypage";
    }
}
