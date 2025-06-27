package com.takku.project.mapper;

import java.util.List;

public interface TagMapper {

	List<String> selectTagNamesByFundingId(Integer fundingId);

}
