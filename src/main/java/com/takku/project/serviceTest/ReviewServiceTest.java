package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.takku.project.domain.ReviewDTO;
import com.takku.project.service.ReviewService;

public class ReviewServiceTest {

    @Mock
    private SqlSession sqlSession;

    @InjectMocks
    private ReviewService reviewService;

    private final String namespace = "com.takku.project.mapper.ReviewMapper.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insertReview_shouldReturnSuccess() {
        ReviewDTO review = new ReviewDTO();
        when(sqlSession.insert(namespace + "insertReview", review)).thenReturn(1);

        int result = reviewService.insertReview(review);

        assertEquals(1, result);
        verify(sqlSession).insert(namespace + "insertReview", review);
    }

    @Test
    void deleteReview_shouldReturnSuccess() {
        Integer reviewId = 1;
        when(sqlSession.delete(namespace + "deleteReview", reviewId)).thenReturn(1);

        int result = reviewService.deleteReview(reviewId);

        assertEquals(1, result);
        verify(sqlSession).delete(namespace + "deleteReview", reviewId);
    }

    @Test
    void updateReview_shouldReturnSuccess() {
        ReviewDTO review = new ReviewDTO();
        when(sqlSession.update(namespace + "updateReview", review)).thenReturn(1);

        int result = reviewService.updateReview(review);

        assertEquals(1, result);
        verify(sqlSession).update(namespace + "updateReview", review);
    }

    @Test
    void reviewByProductId_shouldReturnList() {
        Integer productId = 1;
        List<ReviewDTO> mockList = Arrays.asList(new ReviewDTO(), new ReviewDTO());

        doReturn(mockList).when(sqlSession)
            .selectList(eq(namespace + "reviewByProductId"), eq(productId));

        List<ReviewDTO> result = reviewService.reviewByProductId(productId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sqlSession).selectList(namespace + "reviewByProductId", productId);
    }

    @Test
    void reviewByUserID_shouldReturnList() {
        Integer userId = 1;
        List<ReviewDTO> mockList = Arrays.asList(new ReviewDTO());

        doReturn(mockList).when(sqlSession)
            .selectList(eq(namespace + "reviewByUserID"), eq(userId));

        List<ReviewDTO> result = reviewService.reviewByUserID(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sqlSession).selectList(namespace + "reviewByUserID", userId);
    }
}
