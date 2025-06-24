package com.takku.project.controller;

import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseBody;

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

	//주문 폼
	@GetMapping("/{fundingId}")
	public String orderForm(Integer fundingId, Model model) {
		FundingDTO fundingDTO = fundingService.selectFundingByFundingId(fundingId);
		model.addAttribute("fundingDTO", fundingDTO);
		return "orderForm"; 
	}
	
	
	
	//주문 처리
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private UserService userService;

	// 二쇰Ц �뤌
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
		return "user.order"; // 寃곗젣 �럹�씠吏� 蹂댁뿬二쇨린
	}

	// 二쇰Ц 泥섎━
	@PostMapping("/payment")
	public String processOrder(@RequestParam int fundingId, @RequestParam int quantity, @RequestParam int totalPrice,
			@RequestParam String imp_uid, @RequestParam String merchant_uid, Model model) {
		// �윉� �꽌踰꾩뿉�꽌 諛섎뱶�떆 寃곗젣 �젙蹂� 寃�利�
		// �븘�엫�룷�듃 寃곗젣 寃�利� �삁�떆 (�꽑�깮�쟻)
		UserDTO loginUser = userService.selectByUserId(5);

		// 二쇰Ц �젙蹂� �깮�꽦
		OrderDTO order = new OrderDTO();
		order.setUserId(loginUser.getUserId());
		order.setFundingId(fundingId);
		order.setQty(quantity);
		order.setAmount(totalPrice);
		order.setImpUid(imp_uid);
		order.setMerchantUid(merchant_uid);

		int result = orderService.insertOrder(order);
		model.addAttribute("isSuccess", result > 0);
		return "user.payment";
	}
	
	 @GetMapping("/detail")
	    @ResponseBody
	    public Map<String, Object> getOrderDetail(@RequestParam("orderId") int orderId) {
		 OrderDTO order = orderService.selectOrderByOrderId(orderId);  
		    String fundingName = orderService.getFundingNameByOrderId(orderId);

		    Map<String, Object> result = new HashMap<>();
		    result.put("fundingName", fundingName);          
		    result.put("qty", order.getQty());
		    result.put("purchasedAt", order.getPurchasedAt());
		    result.put("paymentMethod", order.getPaymentMethod());
		    result.put("status", order.getStatus());

		    return result;
	    }

}
