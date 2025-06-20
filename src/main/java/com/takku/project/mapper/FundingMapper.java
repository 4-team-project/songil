package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.FundingDTO;

public interface FundingMapper {

	//�ݵ� ��ü����
	List<FundingDTO> selectAllFunding();
	
	//�ݵ� �󼼺���
	FundingDTO selectFundingByFundingId(Integer fundingId);
	
	//�ݵ� �󼼰˻�
	List<FundingDTO> selectFundingByCondition(String keyword, Integer categoryId, String sido, String sigungu);
	
	//�ݵ� �Է�
	int insertFunding(FundingDTO funding);
	
	//�ݵ� ����
	int updateFunding(FundingDTO funding);
	
	//�ݵ� ����
	int deleteFunding(Integer fundingId);
	
	//스토어 id로 펀딩찾기
	List<FundingDTO> findFundingByStoreId(int storeId);
	
	//펀딩 상태로 펀딩 찾기(진행중, 준비중... etc)
	List<FundingDTO> selectByFundingStatus(String status);
	
	//종료일 된 펀딩 마감하기
	int updateFundingStatus(Integer fundingId, String status);
}
