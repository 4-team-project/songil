package com.takku.project.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.ProductDTO;
import com.takku.project.mapper.ProductMapper;

@Service
public class ProductService implements ProductMapper{

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.ProductMapper.";
	
	@Override
	public int insertProduct(ProductDTO productVO) {
		int result = sqlSession.insert(namespace+"insertProduct", productVO);
		return result;
	}
	@Override
	public List<ProductDTO> selectProductByStoreId(Integer storeId) {
		List<ProductDTO> proList = sqlSession.selectList(namespace + "getProductByStoreId", storeId);
		return proList;
	}
	@Override
	public int updateProduct(ProductDTO productVO) {
		int result = sqlSession.update(namespace+"updateProduct", productVO);
		return result;
	}
	@Override
	public int deleteProduct(Integer productId) {
		int result = sqlSession.delete(namespace+"deleteProduct", productId);
		return result;
	}
	
	@Override
	public ProductDTO selectByProductId(Integer productId) {
		ProductDTO product = sqlSession.selectOne(namespace+"selectByProductId", productId);
		return product;
	}

}
