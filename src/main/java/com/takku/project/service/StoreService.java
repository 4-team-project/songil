package com.takku.project.service;

import org.apache.ibatis.session.SqlSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.StoreDTO;
import com.takku.project.mapper.StoreMapper;

@Service
public class StoreService implements StoreMapper{

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.StoreMapper.";
	
	@Override
	public int insertStore(StoreDTO store) {
		int result = sqlSession.insert(namespace + "insertStore", store);
		return result;
	}

	@Override
	public StoreDTO selectStoreById(Integer storeId) {
		StoreDTO store = sqlSession.selectOne(namespace + "selectStoreById", storeId);
		return store;
	}

	@Override
	public int updateStore(StoreDTO store) {
		int result = sqlSession.update(namespace + "updateStore", store);
		return result;
	}

	@Override
	public int deleteStore(Integer storeId) {
		int result = sqlSession.delete(namespace + "deleteStore", storeId);
		return result;
	}

}
