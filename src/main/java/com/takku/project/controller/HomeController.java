package com.takku.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/user/home")
    public String homePage() {
        return "pages/user/home"; 
    }
}

