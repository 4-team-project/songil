package com.takku.project.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.SettlementDTO;
import com.takku.project.mapper.SettlementMapper;

@Service
public class SettlementService implements SettlementMapper{
	
	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.SettlementMapper.";
	
	@Override
	public int insertSettlement(SettlementDTO dto) {
		int result = sqlSession.insert(namespace + "insertSettlement", dto);
		return result;
	}
	
	@Override
	public List<SettlementDTO> selectSettlementByStoreId(Integer storeId) {
		List<SettlementDTO> settList = sqlSession.selectList(namespace + "getSettlementByStoreId", storeId);
		return settList;
	}
	
	@Override
	public int updateSettlementStatus(Map<String, Object> params) {
		int result = sqlSession.update(namespace + "updateSettlementStatus", params);
		return result;
	}
	
	@Override
	public SettlementDTO selectSettlementById(Integer settlementId) {
		SettlementDTO sett = sqlSession.selectOne(namespace + "getSettlementById", settlementId);
		return sett;
	}
}
