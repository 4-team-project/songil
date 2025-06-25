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

@Service
public class FundingService {

	@Autowired
	private SqlSession sqlSession;

	private final String namespace = "com.takku.project.mapper.FundingMapper.";
	private final String imageNamespace = "com.takku.project.mapper.ImageMapper.";

	/**
	 * 조건 + 정렬 + 페이징을 포함한 펀딩 조회
	 */
	public List<FundingDTO> getFundingsByConditionWithPaging(String keyword, Integer categoryId, String sido,
			String sigungu, String sort, int page, int size) {

		int startRow = (page - 1) * size + 1;
		int endRow = page * size;

		Map<String, Object> param = new HashMap<>();
		param.put("keyword", keyword);
		param.put("categoryId", categoryId);
		param.put("sido", sido);
		param.put("sigungu", sigungu);
		param.put("sort", sort);
		param.put("startRow", startRow);
		param.put("endRow", endRow);

		List<FundingDTO> list = sqlSession.selectList(namespace + "selectFundingByConditionWithPaging", param);

		for (FundingDTO funding : list) {
			List<ImageDTO> images = sqlSession.selectList(imageNamespace + "selectImagesByFundingId",
					funding.getFundingId());
			List<String> tags = sqlSession.selectList(namespace + "selectTagsByFundingId", funding.getFundingId());

			funding.setImages(images);
			funding.setTagList(tags);
		}

		return list;
	}

	/**
	 * 페이징 기반 조건 검색용 전체 개수
	 */
	public int getFundingCountByCondition(String keyword, Integer categoryId, String sido, String sigungu) {
		Map<String, Object> param = new HashMap<>();
		param.put("keyword", keyword);
		param.put("categoryId", categoryId);
		param.put("sido", sido);
		param.put("sigungu", sigungu);
		return sqlSession.selectOne(namespace + "countFundingByCondition", param);
	}

	/**
	 * 전체 펀딩 목록 조회 (썸네일 포함)
	 */
	public List<FundingDTO> selectAllFunding() {
		return sqlSession.selectList(namespace + "selectAllFunding");
	}

	/**
	 * 펀딩 상세 조회 (이미지 + 태그 포함)
	 */
	public FundingDTO selectFundingByFundingId(Integer fundingId) {
		FundingDTO funding = sqlSession.selectOne(namespace + "selectFundingByFundingId", fundingId);
		if (funding != null) {
			List<ImageDTO> images = sqlSession.selectList(imageNamespace + "selectImagesByFundingId", fundingId);
			List<String> tags = sqlSession.selectList(namespace + "selectTagsByFundingId", fundingId);
			funding.setImages(images);
			funding.setTagList(tags);
		}
		return funding;
	}

	/**
	 * 상점 ID로 펀딩 목록 조회
	 */
	public List<FundingDTO> findFundingByStoreId(int storeId) {
		return sqlSession.selectList(namespace + "findFundingByStoreId", storeId);
	}

	/**
	 * 신규 펀딩 등록
	 */
	public int insertFunding(FundingDTO funding) {
		return sqlSession.insert(namespace + "insertFunding", funding);
	}

	/**
	 * 펀딩 정보 수정
	 */
	public int updateFunding(FundingDTO funding) {
		return sqlSession.update(namespace + "updateFunding", funding);
	}

	/**
	 * 펀딩 삭제
	 */
	public int deleteFunding(Integer fundingId) {
		return sqlSession.delete(namespace + "deleteFunding", fundingId);
	}

	/**
	 * 펀딩 종료일 조회
	 */
	public Date selectEndDateByFundingId(int fundingId) {
		return sqlSession.selectOne(namespace + "selectEndDateByFundingId", fundingId);
	}

	/**
	 * 특정 상태의 펀딩 목록 조회
	 */
	public List<FundingDTO> selectByFundingStatus(String status) {
		return sqlSession.selectList(namespace + "selectByFundingStatus", status);
	}

	/**
	 * 펀딩 상태 변경
	 */
	public int updateFundingStatus(Integer fundingId, String status) {
		Map<String, Object> param = new HashMap<>();
		param.put("fundingId", fundingId);
		param.put("status", status);
		return sqlSession.update(namespace + "updateFundingStatus", param);
	}

	/**
	 * 종료된 펀딩 상태 변경
	 */
	public int updateFundingStatusIfExpired(Integer fundingId, String status) {
		Map<String, Object> param = new HashMap<>();
		param.put("fundingId", fundingId);
		param.put("status", status);
		return sqlSession.update(namespace + "updateFundingStatusIfExpired", param);
	}
	
	//사용자별 펀딩 상태 조회
	public List<FundingDTO> selectFundingListByStatus(int userId, String status)  {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("status", status);
		return sqlSession.selectList(namespace+"selectFundingListByStatus", param);
	}
}
