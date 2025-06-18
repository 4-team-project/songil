package com.takku.project.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.OrderDTO;
import com.takku.project.mapper.OrderMapper;

@Service
public class OrderService implements OrderMapper{

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.OrderMapper.";
	
	@Override
	public List<OrderDTO> selectByUserId(Integer userId) {
		List<OrderDTO> orderList = sqlSession.selectList(namespace + "selectByUserId", userId);
		return orderList;
	}
	
	@Override	
	public int insertOrder(OrderDTO order) {
		int result = sqlSession.insert(namespace + "insertOrder", order);
		return result;
	}
	
	@Override
	public int updateOrderFundingStatus(OrderDTO order) {
		int result = sqlSession.update(namespace + "updateOrderFundingStatus", order);
		return result;
	}
	
	@Override
	public int updateOrderRefundAtStatus(OrderDTO order) {
		int result = sqlSession.update(namespace + "updateOrderRefundAtStatus", order);
		return result;
	}
}
