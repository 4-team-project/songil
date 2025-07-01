package com.takku.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreManagement {
	
	@GetMapping("/seller/storeManagement")
    public String homePage() {
		return "seller.storeManagement";
	}

}
