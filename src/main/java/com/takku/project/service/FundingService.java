package com.takku.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.FundingDTO;
import com.takku.project.mapper.FundingMapper;

@Service
public class FundingService implements FundingMapper {

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.FundingMapper.";

	@Override
	public List<FundingDTO> selectAllFunding() {
		List<FundingDTO> fundinglist = sqlSession.selectList(namespace + "selectAllFunding");
		return fundinglist;
	}

	@Override
	public FundingDTO selectFundingByFundingId(Integer fundingId) {
		FundingDTO funding = sqlSession.selectOne(namespace + "selectByFundingId", fundingId);
		return funding;
	}

	@Override
	public List<FundingDTO> selectFundingByCondition(String keyword, Integer categoryId, String sido, String sigungu) {
		Map<String, Object> map = new HashMap<>();
		map.put("keyword", keyword);
		map.put("categoryId", categoryId);
		map.put("sido", sido);
		map.put("sigungu", sigungu);

	    return sqlSession.selectList(namespace + "selectFundingByCondition", map);
	}
	
	@Override
	public int insertFunding(FundingDTO funding) {
		int result = sqlSession.insert(namespace + "insertFunding", funding);
		return result;
	}

	@Override
	public int updateFunding(FundingDTO funding) {
		int result = sqlSession.insert(namespace + "updateFunding", funding);
		return result;
	}

	@Override
	public int deleteFunding(Integer fundingId) {
		int result = sqlSession.delete(namespace + "deleteFunding", fundingId);
		return result;
	}
}
