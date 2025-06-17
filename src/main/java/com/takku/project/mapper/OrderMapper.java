package com.takku.project.mapper;

import com.takku.project.domain.OrderDTO;

public interface OrderMapper {
	
	//주문 구매자 ID로 조회
	OrderDTO selectByUserId(Integer userId);
	
	//주문 생성
	int insertOrder(OrderDTO order);
	
	//주문 수정
	int updateOrder(OrderDTO order);
	
}
