package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.ReviewDTO;

public interface ReviewMapper {
//리뷰 등록
	int insertReview(ReviewDTO review);
	
//리뷰 삭제
	int deleteReview(Integer reviewId);
	
//리뷰 수정
	int updateReview(ReviewDTO review);
	
//리뷰 조회
	List<ReviewDTO> selectAllReview();
}
