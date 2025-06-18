package com.takku.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.takku.project.domain.FundingDTO;

public interface FundingListMapper {

	  // 정렬 조건을 파라미터로 받는 메서드
    List<FundingDTO> selectFundingListBySort(@Param("sort") String sort);

}
