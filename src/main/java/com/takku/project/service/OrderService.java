package com.takku.project.service;

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
	public OrderDTO selectByUserId(Integer userId) {
		OrderDTO order = sqlSession.selectOne(namespace + "selectByUserId", userId);
		return order;
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
