package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.FundingDTO;

public interface FundingListMapper {

	List<FundingDTO> selectFundingListBySort(String sort);

}
