package com.takku.project.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.ImageDTO;
import com.takku.project.domain.ReviewDTO;
import com.takku.project.mapper.ReviewMapper;

@Service
public class ReviewService implements ReviewMapper{
	
	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.ReviewMapper.";
	
	@Override
	public int insertReview(ReviewDTO review) {
		int result = sqlSession.insert(namespace+"insertReview", review);
		return result;
	}
	
	@Override
	public int deleteReview(Integer reviewId) {
		int result = sqlSession.delete(namespace+"deleteReview", reviewId);
		return result;
	}
	
	@Override
	public int updateReview(ReviewDTO review) {
		int result = sqlSession.update(namespace+"updateReview", review);
		return result;
	}
	
	@Override
	public List<ReviewDTO> reviewByProductId(Integer productId) {
		List<ReviewDTO> rewiewList = sqlSession.selectList(namespace + "reviewByProductId", productId);
		
		for (ReviewDTO review : rewiewList) {
	        List<ImageDTO> images = sqlSession.selectList(namespace + "selectImagesByReviewId", review.getReviewId());
	        review.setImages(images);
	    }
		return rewiewList;
	}
	
	@Override
	public List<ReviewDTO> reviewByUserID(Integer userId) {
		List<ReviewDTO> rewiewList = sqlSession.selectList(namespace + "reviewByUserID", userId);
		
		for (ReviewDTO review : rewiewList) {
	        List<ImageDTO> images = sqlSession.selectList(namespace + "selectImagesByReviewId", review.getReviewId());
	        review.setImages(images);
	    }
		return rewiewList;
	}

}
