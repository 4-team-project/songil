package com.takku.project.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.OrderDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private FundingService fundingService;
	
	@Autowired
	private OrderService orderService;

	//주문 폼
	@GetMapping("/{fundingId}")
	public String orderForm(Integer fundingId, Model model) {
		FundingDTO fundingDTO = fundingService.selectFundingByFundingId(fundingId);
		model.addAttribute("fundingDTO", fundingDTO);
		return "orderForm"; 
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
	
	 @GetMapping("/detail")
	    @ResponseBody
	    public Map<String, Object> getOrderDetail(@RequestParam("orderId") int orderId) {
		 OrderDTO order = orderService.selectOrderByOrderId(orderId);  
		    String fundingName = orderService.getFundingNameByOrderId(orderId);

		    Map<String, Object> result = new HashMap<>();
		    result.put("fundingName", fundingName);           // ✅ 이 부분이 중요
		    result.put("qty", order.getQty());
		    result.put("purchasedAt", order.getPurchasedAt());
		    result.put("paymentMethod", order.getPaymentMethod());
		    result.put("status", order.getStatus());

		    return result;
	    }
	
}
