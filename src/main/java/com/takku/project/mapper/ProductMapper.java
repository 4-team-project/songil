package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.ProductDTO;

public interface ProductMapper {

	// 1. ��ǰ ���
	int insertProduct(ProductDTO productVO);

    // 2. ���� ID�� ��ǰ ��� ��ȸ
    List<ProductDTO> getProductsByStoreId(Integer storeId);

    // 3. ��ǰ ����
    int updateProduct(ProductDTO productVO);

    // 4. ��ǰ ����
    int deleteProduct(Integer productId);
}
