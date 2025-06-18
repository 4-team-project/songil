package com.takku.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.OrderDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.OrderService;

@Controller
@RequestMapping("/order/")
public class OrderController {
	
	@Autowired
	private FundingService fundingService;
	
	@Autowired
	private OrderService orderService;

	//주문 폼
	@GetMapping("/{fundgindId}")
	public String orderForm(Integer fundingId, Model model) {
		FundingDTO fundingDTO = fundingService.selectByFundingId(fundingId);
		model.addAttribute("fundingDTO", fundingDTO);
		return "orderForm"; //orderForm.jsp
	}
	
	//주문 처리
	@GetMapping
	public String processOrder(OrderDTO orderDTO, Model model) {
		int result = orderService.insertOrder(orderDTO);
		if(result > 0) {
			model.addAttribute("resultMessage", "주문이 성공적으로 완료되었습니다.");
		}else {
			model.addAttribute("resultMessage", "주문 처리에 실패했습니다.");
		}
		return "redirect:/mypage/order";
	}
	
}
