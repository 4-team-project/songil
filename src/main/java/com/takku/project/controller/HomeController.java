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

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.ImageService;

@Controller
public class HomeController {

    @Autowired
    FundingService fundingService;

    @Autowired
    ImageService imageService;

    @GetMapping("/user/home")
    public String homePage(Model model) {
        List<FundingDTO> ongoingFundingList = fundingService.selectByFundingStatus("진행중");

        Map<Integer, Long> fundingDaysLeftMap = new HashMap<>();

        for (FundingDTO funding : ongoingFundingList) {
            List<ImageDTO> images = imageService.selectImagesByFundingId(funding.getFundingId());
            funding.setImages(images);


            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), funding.getEndDate().toLocalDate());
            fundingDaysLeftMap.put(funding.getFundingId(), daysLeft);
        }

        model.addAttribute("fundinglist", ongoingFundingList);
        model.addAttribute("daysLeftMap", fundingDaysLeftMap); 
        return "user.home";
    }

}

