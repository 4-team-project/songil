package com.takku.project.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.takku.project.domain.FundingDTO;
import com.takku.project.service.FundingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FundingSchedulerController {

	@Autowired
	private FundingService fundingService;

	@Scheduled(cron = "0 0 0 * * *")
	public void checkFundingResultsAndIssueCoupons() {
		log.info(">> 스케줄러 실행 시작");

		LocalDate today = LocalDate.now();

		// 1. 마감된 펀딩 처리
		List<FundingDTO> endedFundings = fundingService.selectByFundingStatus("진행중");
		for (FundingDTO funding : endedFundings) {
			if (funding.getEndDate() == null) {
				log.warn("펀딩 종료일 없음: fundingId={}", funding.getFundingId());
				continue;
			}

			LocalDate endDate = funding.getEndDate().toLocalDate();
			if (!today.isBefore(endDate)) {
				fundingService.processEndedFunding(funding, today);
			}
		}

		// 2. 시작 펀딩 처리
		List<FundingDTO> isStartFundings = fundingService.selectByFundingStatus("준비중");
		for (FundingDTO funding : isStartFundings) {
			if (funding.getStartDate() == null) {
				log.warn("펀딩 시작일 없음: fundingId={}", funding.getFundingId());
				continue;
			}

			LocalDate startDate = funding.getStartDate().toLocalDate();
			if (!today.isBefore(startDate)) {
				fundingService.updateFundingStatusIfExpired(funding.getFundingId(), "진행중");
				log.info("펀딩 시작 처리: {}", funding.getFundingId());
			}
		}

		log.info(">> 스케줄러 실행 종료");
	}
}
