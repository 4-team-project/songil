package com.takku.project.controller;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		UserDTO user = userService.selectByUserId(7); // test

		model.addAttribute("pageName", "결제하기");
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
			@RequestParam int usePoint, @RequestParam String imp_uid, @RequestParam String merchant_uid,
			RedirectAttributes redirectAttributes) {

		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		UserDTO loginUser = userService.selectByUserId(7); // test

		int finalPrice = totalPrice - usePoint;

		OrderDTO order = new OrderDTO();
		order.setUserId(loginUser.getUserId());
		order.setFundingId(fundingId);
		order.setQty(quantity);
		order.setAmount(totalPrice);
		order.setUsePoint(usePoint);
		order.setDiscountAmount(finalPrice);
		order.setStatus("결제완료");
		order.setFundingStatus("펀딩 진행중");
		order.setImpUid(imp_uid);
		order.setMerchantUid(merchant_uid);

		userService.updatePointAfterPayment(loginUser.getUserId(), usePoint);
		int result = orderService.insertOrder(order);
		redirectAttributes.addAttribute("orderId", order.getOrderId());
		redirectAttributes.addAttribute("success", result > 0);
		return "redirect:payment/result";
	}

	// 주문 결과
	@GetMapping("/payment/result")
	public String paymentResult(@RequestParam int orderId, @RequestParam boolean success, Model model) {
		OrderDTO saveOrder = orderService.selectOrderByOrderId(orderId);
		FundingDTO funding = fundingService.selectFundingByFundingId(saveOrder.getFundingId());

		model.addAttribute("funding", funding);
		model.addAttribute("saveOrder", saveOrder);
		model.addAttribute("isSuccess", success);
		return "user.payment";
	}
	

	@GetMapping("/detail")
	@ResponseBody
	public Map<String, Object> getOrderDetail(@RequestParam("orderId") int orderId) {
		OrderDTO order = orderService.selectOrderByOrderId(orderId);
		String fundingName = orderService.getFundingNameByOrderId(orderId);

		Map<String, Object> result = new HashMap<>();
		result.put("orderId", orderId);
		result.put("fundingName", fundingName);
		result.put("qty", order.getQty());
		result.put("purchasedAt", order.getPurchasedAt());
		result.put("paymentMethod", order.getPaymentMethod());
		result.put("status", order.getStatus());

		return result;
	}

	@GetMapping("/list")
	public String getOrdersByStatus(@RequestParam String status, Model model) {
		List<OrderDTO> orderList = new ArrayList<>();

		if ("allbuylist".equals(status)) {
			orderList = orderService.selectByUserId(5); // 전체 조회
		} else if ("complete".equals(status)) {
			orderList = orderService.getOrdersByUserAndStatus(5, "결제완료"); // 임시 userid!!!!!!!!!!!!!
		} else if ("cancel".equals(status)) {
			orderList = orderService.getOrdersByUserAndStatus(5, "환불"); // 임시 userid!!!!!!!!!!!!!
		} else if ("null".equals(status)) {
			orderList = orderService.selectByUserId(5);
		}

		model.addAttribute("orderList", orderList);
		return "pages/user/mypage_orderList";
	}

	@PostMapping("/cancel")
	@ResponseBody
	public int updateOrderFundingStatus(@RequestParam("orderId") Integer orderId) {
		OrderDTO order = orderService.selectOrderByOrderId(orderId);
		if (order == null)
			return 0;

		// 1. 주문 상태 업데이트
		int result = orderService.updateOrderFundingStatus(orderId);
		
		// 2. 포인트 복원 (결제에 포인트 사용한 경우만)
	    if (result > 0 && order.getUsePoint() > 0) {
	        userService.restorePointAfterCancel(order.getUserId(), order.getUsePoint());
	    }

		return result;
	}
}
