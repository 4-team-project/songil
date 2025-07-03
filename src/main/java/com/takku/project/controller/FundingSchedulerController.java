package com.takku.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.SettlementDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.CouponService;
import com.takku.project.service.FundingService;
import com.takku.project.service.SettlementService;
import com.takku.project.service.StoreService;
import com.takku.project.service.UserService;

@Component
public class FundingSchedulerController {

	@Autowired
	private FundingService fundingService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private SettlementService settlementService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private UserService userService;
	
	// 매일 자정에 실행 (00:00)
	@Scheduled(cron = "0 0 0 * * *")
	public void checkFundingResultsAndIssueCoupons() {
		System.out.println(">> 스케줄러 실행: 마감 펀딩 확인");
		String status = "진행중";
		List<FundingDTO> endedFundings = fundingService.selectByFundingStatus(status);

		for (FundingDTO funding : endedFundings) {		       
			if (funding.getCurrentQty() >= funding.getTargetQty()) {			
				fundingService.updateFundingStatusIfExpired(funding.getFundingId(), "성공");
				
				StoreDTO store = storeService.selectStoreById(funding.getStoreId());
				UserDTO user = userService.selectByUserId(store.getUserId());
				
				// 정산 처리
				String isPartner = user.getIsPartner();
				double feeRate = "N".equals(isPartner) ? 0.068 : 0.02;
				
                int totalAmount = funding.getSalePrice() * funding.getCurrentQty();
                int fee = (int) (totalAmount * feeRate);
                int settlementAmount = totalAmount - fee;

                SettlementDTO settlement = new SettlementDTO();
                settlement.setFundingId(funding.getFundingId());
                settlement.setStoreId(funding.getStoreId());
                settlement.setFee(fee);
                settlement.setAmount(settlementAmount);
                settlement.setStatus("완료");

                settlementService.insertSettlement(settlement);
				/* 쿠폰 발급 부분 */
			} else {
				fundingService.updateFundingStatusIfExpired(funding.getFundingId(), "실패");
			}

		}

	}
}
