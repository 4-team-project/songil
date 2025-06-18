package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.FundingDTO;

public interface FundingMapper {

	//펀딩 전체보기
	List<FundingDTO> selectAllFunding();
	
	//펀딩 상세보기
	FundingDTO selectFundingByFundingId(Integer fundingId);
	
	//펀딩 상세검색
	List<FundingDTO> selectFundingByCondition(String keyword, Integer categoryId, String sido, String sigungu);
	
	//펀딩 입력
	int insertFunding(FundingDTO funding);
	
	//펀딩 수정
	int updateFunding(FundingDTO funding);
	
	//펀딩 삭제
	int deleteFunding(Integer fundingId);
}
