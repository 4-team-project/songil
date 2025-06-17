package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.ProductDTO;

public interface ProductMapper {

	// 1. ��ǰ ���
    void insertProduct(ProductDTO productVO);

    // 2. ���� ID�� ��ǰ ��� ��ȸ
    List<ProductDTO> getProductsByStoreId(Integer storeId);

    // 3. ��ǰ ����
    void updateProduct(ProductDTO productVO);

    // 4. ��ǰ ����
    void deleteProduct(Integer productId);
}
