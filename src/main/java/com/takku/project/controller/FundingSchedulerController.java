package com.takku.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.takku.project.domain.FundingDTO;
import com.takku.project.service.CouponService;
import com.takku.project.service.FundingService;



@Component
public class FundingSchedulerController {

    @Autowired
    private FundingService fundingService;

    @Autowired
    private CouponService couponService;

    // 매일 자정에 실행 (00:00)
    @Scheduled(cron = "0 0 0 * * *")
    public void checkFundingResultsAndIssueCoupons() {
        System.out.println(">> 스케줄러 실행: 마감 펀딩 확인");
        String status = "진행중";
        List<FundingDTO> endedFundings = fundingService.selectByFundingStatus(status);

        for (FundingDTO funding : endedFundings) {

            if (funding.getCurrentQty() >= funding.getTargetQty()) {
                fundingService.updateFundingStatus(funding.getFundingId(), "성공");
				/* 쿠폰 발급 부분 */
            } else {
                fundingService.updateFundingStatus(funding.getFundingId(), "실패");
            }
        	
        }
        
    }
}
