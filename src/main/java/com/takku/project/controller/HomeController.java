package com.takku.project.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.service.AIService;
import com.takku.project.service.FundingService;
import com.takku.project.service.ImageService;
import com.takku.project.service.StoreService;

@Controller
public class HomeController {

    @Autowired
    FundingService fundingService;

    @Autowired
    ImageService imageService;

    @Autowired
    AIService aiService; 

    @GetMapping("/user/home")
    public String homePage(@RequestParam(defaultValue = "1") int userId, Model model) {

        List<FundingDTO> recommendList = aiService.getRecommendations(userId); 
        for (FundingDTO funding : recommendList) {
            List<ImageDTO> images = imageService.selectImagesByFundingId(funding.getFundingId());
            funding.setImages(images);
            long days = ChronoUnit.DAYS.between(LocalDate.now(), funding.getEndDate().toLocalDate());
            funding.setDaysLeft((int) Math.max(days, 0));  
        }

        List<FundingDTO> ongoingFundingList = fundingService.selectByFundingStatusWithJoin("진행중");

        for (FundingDTO funding : ongoingFundingList) {
            List<ImageDTO> images = imageService.selectImagesByFundingId(funding.getFundingId());
            funding.setImages(images);

            long days = ChronoUnit.DAYS.between(LocalDate.now(), funding.getEndDate().toLocalDate());
            funding.setDaysLeft((int) Math.max(days, 0));  
        }

        model.addAttribute("recommendList", recommendList); 
        model.addAttribute("fundinglist", ongoingFundingList); 

        return "user.home";
    }
}
