package com.takku.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.FundingDTO;
import com.takku.project.mapper.FundingListMapper;

@Service
public class FundingListService implements FundingListMapper {
	
	@Autowired
	private FundingListMapper fundingListMapper;

	@Override
	public List<FundingDTO> selectFundingListBySort(String sort) {
		if(sort == null || sort.trim().isEmpty()) {
			sort = "latest";
		}
		return fundingListMapper.selectFundingListBySort(sort);
	}

}
