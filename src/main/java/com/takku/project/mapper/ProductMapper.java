package com.takku.project.mapper;

import java.util.List;

import com.takku.project.domain.ProductDTO;

public interface ProductMapper {

	// 1. ��ǰ ���
	int insertProduct(ProductDTO productVO);

    // 2. ���� ID�� ��ǰ ��� ��ȸ
    List<ProductDTO> getProductByStoreId(Integer storeId);

    // 3. ��ǰ ����
    int updateProduct(ProductDTO productVO);

    // 4. ��ǰ ����
    int deleteProduct(Integer productId);
    
    // 5. selectByProductId
    ProductDTO selectByProductId(Integer productId);
}
