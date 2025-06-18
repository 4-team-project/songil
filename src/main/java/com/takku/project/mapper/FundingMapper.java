package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.FundingDTO;

public interface FundingMapper {

	//펀딩 전체 조회
	List<FundingDTO> selectAllFunding();
	
	//펀딩 번호로 상세 조회
	FundingDTO selectByFundingId(Integer fundingId);
	
	//등록
	int insertFunding(FundingDTO funding);
	
	//수정
	int updateFunding(FundingDTO funding);
	
	//삭제
	int deleteFunding(Integer fundingId);
}
