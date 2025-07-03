package com.takku.project.controller;

import java.time.LocalDate;
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
		System.out.println(">> 스케줄러 실행: 마감 펀딩 확인 및 시작 펀딩 확인");

		// 오늘 날짜를 LocalDate로 가져옵니다. (시스템 기본 시간대 기준)
		// 특정 시간대(예: KST)를 명시하려면: LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
		LocalDate today = LocalDate.now();

		// 펀딩 종료 처리 로직
		String endedStatus = "진행중"; // 종료 확인 대상이 되는 현재 상태
		List<FundingDTO> endedFundings = fundingService.selectByFundingStatus(endedStatus);


		for (FundingDTO funding : endedFundings) {
			// FundingDTO의 getEndDate()가 java.sql.Date를 반환한다고 가정
			if (funding.getEndDate() != null) {
				LocalDate endDate = funding.getEndDate().toLocalDate(); // java.sql.Date를 LocalDate로 변환
				
				// 종료일이 오늘이거나 이미 지났는지 확인 (오늘 날짜 >= 종료일)
				if (today.isAfter(endDate) || today.isEqual(endDate)) {
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
				System.out.println("펀딩 실패 처리: " + funding.getFundingId());
			}
		}
	}

		}

		// 펀딩 시작 처리 로직
		String preparingStatus = "준비중"; // 시작 확인 대상이 되는 현재 상태
		List<FundingDTO> isStartFundings = fundingService.selectByFundingStatus(preparingStatus);

		for (FundingDTO funding : isStartFundings) {
			// FundingDTO의 getStartDate()가 java.sql.Date를 반환한다고 가정
			if (funding.getStartDate() != null) {
				LocalDate startDate = funding.getStartDate().toLocalDate(); // java.sql.Date를 LocalDate로 변환

				// 시작일이 오늘이거나 이미 지났는지 확인 (오늘 날짜 >= 시작일)
				if (today.isAfter(startDate) || today.isEqual(startDate)) {
					fundingService.updateFundingStatusIfExpired(funding.getFundingId(), "진행중");
					/* 필요하다면 여기에 시작 시 쿠폰 발급/알림 등의 로직 추가 */
					System.out.println("펀딩 시작 처리: " + funding.getFundingId());
				}
			}
		}
	}
}
