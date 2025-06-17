package com.takku.project.service;

import java.util.List;

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
		List<FundingDTO> funding = sqlSession.selectList(namespace + "selectAllFunding");
		return funding;
	}
	@Override
	public FundingDTO selectByFundingId(Integer fundingId) {
		FundingDTO funding = sqlSession.selectOne(namespace + "selectByFundingId", fundingId);
		return funding;
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
		int result = sqlSession.delete(namespace+"deleteFunding", fundingId);
		return result;
	}
}
