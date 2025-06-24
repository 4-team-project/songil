package com.takku.project.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.OrderDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.OrderService;
import com.takku.project.service.StoreService;
import com.takku.project.service.UserService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private FundingService fundingService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private StoreService storeService;
	
	@Autowired
	private UserService userService;

	// 주문 폼
	@GetMapping
	public String orderForm(@RequestParam int fundingId, @RequestParam int quantity, @RequestParam int totalPrice,
			Model model) {
		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		StoreDTO store = storeService.selectStoreById(funding.getStoreId());
		UserDTO user = userService.selectByUserId(5);
		
		model.addAttribute("funding", funding);
		model.addAttribute("store", store);
		model.addAttribute("loginUser", user);
		model.addAttribute("quantity", quantity);
		model.addAttribute("totalPrice", totalPrice);
		return "user.order"; 
	}

	// 주문 처리
	@PostMapping("/payment")
	public String processOrder(@RequestParam int fundingId, @RequestParam int quantity, @RequestParam int totalPrice,
			@RequestParam String imp_uid, @RequestParam String merchant_uid, Model model) {

		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		UserDTO loginUser = userService.selectByUserId(5);
		int point = 0;
		
		OrderDTO order = new OrderDTO();
		order.setUserId(loginUser.getUserId());
		order.setFundingId(fundingId);
		order.setQty(quantity);
		order.setAmount(totalPrice);
		order.setUsePoint(point);
		order.setDiscountAmount(totalPrice-point);
		order.setStatus("결제완료");
		order.setFundingStatus(funding.getStatus());
		order.setImpUid(imp_uid);
		order.setMerchantUid(merchant_uid);
		
		System.out.println(order);;
		int result = orderService.insertOrder(order);
		model.addAttribute("isSuccess", result > 0);
		return "user.payment";
	}
}
