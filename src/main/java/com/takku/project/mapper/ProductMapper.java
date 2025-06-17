package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.ProductDTO;

public interface ProductMapper {

	// 1. 상품 등록
    void insertProduct(ProductDTO productVO);

    // 2. 상점 ID로 상품 목록 조회
    List<ProductDTO> getProductsByStoreId(Integer storeId);

    // 3. 상품 수정
    void updateProduct(ProductDTO productVO);

    // 4. 상품 삭제
    void deleteProduct(Integer productId);
}
