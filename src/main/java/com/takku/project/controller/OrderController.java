package com.takku.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.OrderDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.OrderService;
import com.takku.project.service.StoreService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private FundingService fundingService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private StoreService storeService;

	// 주문 폼
	@GetMapping
	public String orderForm(@RequestParam int fundingId, @RequestParam int quantity, @RequestParam int totalPrice,
			Model model) {
		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		StoreDTO store = storeService.selectStoreById(funding.getStoreId());

		model.addAttribute("funding", funding);
		model.addAttribute("store", store);
		model.addAttribute("quantity", quantity);
		model.addAttribute("totalPrice", totalPrice);
		return "user.order"; // 결제 페이지 보여주기
	}

	// 주문 처리
	@PostMapping("/payment")
	public String processOrder(OrderDTO orderDTO, Model model) {
		int result = orderService.insertOrder(orderDTO);
		if (result > 0) {
			model.addAttribute("resultMessage", "주문이 성공적으로 완료되었습니다.");
		} else {
			model.addAttribute("resultMessage", "주문 처리에 실패했습니다.");
		}
		return "user.payment";
	}

}
