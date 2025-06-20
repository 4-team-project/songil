package com.takku.project.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.takku.project.domain.ProductDTO;
import com.takku.project.service.ProductService;

public class ProductServiceTest {

    @Mock
    private SqlSession sqlSession;

    @InjectMocks
    private ProductService productService;

    private final String namespace = "com.takku.project.mapper.ProductMapper.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("상품 등록")
    void insertProduct() {
        ProductDTO product = new ProductDTO();
        when(sqlSession.insert(namespace + "insertProduct", product)).thenReturn(1);

        int result = productService.insertProduct(product);

        assertEquals(1, result);
        verify(sqlSession).insert(namespace + "insertProduct", product);
    }

    @Test
    @DisplayName("상점ID로 상품 조회")
    void selectProductByStoreId() {
        Integer storeId = 1;
        List<ProductDTO> mockList = Arrays.asList(new ProductDTO(), new ProductDTO());

        doReturn(mockList)
            .when(sqlSession)
            .selectList(eq(namespace + "getProductByStoreId"), eq(storeId));

        List<ProductDTO> result = productService.selectProductByStoreId(storeId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sqlSession).selectList(namespace + "getProductByStoreId", storeId);
    }

    @Test
    @DisplayName("상품 수정")
    void updateProduct(){
        ProductDTO product = new ProductDTO();
        when(sqlSession.update(namespace + "updateProduct", product)).thenReturn(1);

        int result = productService.updateProduct(product);

        assertEquals(1, result);
        verify(sqlSession).update(namespace + "updateProduct", product);
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProduct() {
        Integer productId = 10;
        when(sqlSession.delete(namespace + "deleteProduct", productId)).thenReturn(1);

        int result = productService.deleteProduct(productId);

        assertEquals(1, result);
        verify(sqlSession).delete(namespace + "deleteProduct", productId);
    }

    @Test
    @DisplayName("상품ID로 상품 조회")
    void selectByProductId() {
        Integer productId = 10;
        ProductDTO product = new ProductDTO();
        product.setProductId(productId);

        when(sqlSession.selectOne(namespace + "selectByProductId", productId)).thenReturn(product);

        ProductDTO result = productService.selectByProductId(productId);

        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        verify(sqlSession).selectOne(namespace + "selectByProductId", productId);
    }
}
