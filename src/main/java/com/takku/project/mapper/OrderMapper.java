package com.takku.project.mapper;

import com.takku.project.domain.OrderDTO;

public interface OrderMapper {
	
	//주문 구매자 ID로 조회
	OrderDTO selectByUserId(Integer userId);
	
	//주문 생성
	int insertOrder(OrderDTO order);
	
	//주문 펀딩 상태 수정
	int updateOrderFundingStatus(OrderDTO order);
	
	//주문 결제 상태 및 환불일 수정 
	int updateOrderRefundAtStatus(OrderDTO order);
}
