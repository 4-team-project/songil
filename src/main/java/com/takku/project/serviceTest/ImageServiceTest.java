package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.takku.project.domain.ImageDTO;
import com.takku.project.service.ImageService;

public class ImageServiceTest {

    @Mock
    private SqlSession sqlSession;

    @InjectMocks
    private ImageService imageService;
    private final String namespace = "com.takku.project.mapper.ImageMapper.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insertImageUrl_insert() {
        ImageDTO image = new ImageDTO();
        image.setImageUrl("https://example.com/test.jpg");

        when(sqlSession.insert(namespace + "insertImageUrl", image)).thenReturn(1);

        int result = imageService.insertImageUrl(image);

        assertEquals(1, result);
        verify(sqlSession).insert(namespace + "insertImageUrl", image);
    }

    @Test
    void deleteImageUrl_delete() {
        String imageUrl = "https://example.com/test.jpg";

        when(sqlSession.delete(namespace + "deleteImageUrl", imageUrl)).thenReturn(1);

        int result = imageService.deleteImageUrl(imageUrl);

        assertEquals(1, result);
        verify(sqlSession).delete(namespace + "deleteImageUrl", imageUrl);
    }
}
