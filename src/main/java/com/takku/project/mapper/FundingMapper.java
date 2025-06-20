package com.takku.project.mapper;

import java.util.Date;
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
	
	//만료일 가져오기
	Date selectEndDateByFundingId(int fundingId);
}
