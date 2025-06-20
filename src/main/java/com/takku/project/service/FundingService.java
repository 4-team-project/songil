package com.takku.project.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.mapper.FundingMapper;

@Service
public class FundingService implements FundingMapper {

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.FundingMapper.";
	String namespace2 = "com.takku.project.mapper.ImageMapper.";
	
	@Autowired
	private FundingService fundingService;

	@Override
	public List<FundingDTO> selectAllFunding() {
		List<FundingDTO> fundinglist = sqlSession.selectList(namespace + "selectAllFunding");
		return fundinglist;
	}

	@Override
	public FundingDTO selectFundingByFundingId(Integer fundingId) {	
		FundingDTO funding = sqlSession.selectOne(namespace + "selectFundingByFundingId", fundingId);
		List<ImageDTO> images = sqlSession.selectList(namespace2 + "selectImagesByFundingId", fundingId);
		funding.setImages(images);
		return funding;
	}
	
	@Override
    public List<FundingDTO> findFundingByStoreId(int storeId) {
        return sqlSession.selectList(namespace + "selectFundingByStoreId", storeId);
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

	@Override
	public Date selectEndDateByFundingId(int fundingId) {
		 return fundingService.selectEndDateByFundingId(fundingId);
	}
	
	public List<FundingDTO> selectByFundingStatus(String status) {
		List<FundingDTO> fundingList = sqlSession.selectList(namespace + "selectByFundingStatus", status);
		return fundingList;
	}

	@Override
	public int updateFundingStatus(Integer fundingId, String status) {
		Map<String, Object> map = new HashMap<>();
		map.put("fundingId", fundingId);
		map.put("status", status);
		int result = sqlSession.update(namespace + "updateFundingStatus", map);
		return result;
	}
}
