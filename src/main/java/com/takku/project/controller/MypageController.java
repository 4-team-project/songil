package com.takku.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.takku.project.domain.OrderDTO;
import com.takku.project.service.OrderService;

@Controller
public class MypageController {

	/*
	 * @GetMapping("/user/mypage") public String myPage(Model model) {
	 * model.addAttribute("pageName", "마이페이지"); return "user.mypage"; }
	 */

	@Autowired
	OrderService orderService;
	
	
	@GetMapping("/user/mypage")
	public String myPage(Model model) {
		// 페이지명 전달
		model.addAttribute("pageName", "마이페이지");

		// 테스트용 더미 데이터 생성
//		List<OrderDTO> testOrderList = new ArrayList<>();
//		testOrderList.add(OrderDTO.builder()
//				.orderId(13)
//				.userId(5)

//				.productName("???").qty(2).amount(17000).status("결제완료").paymentMethod("카드")
//				.fundingName("수제 돈까스 정식 할인 펀딩")
//				.purchasedAt(Date.valueOf("2025-06-18"))
//
//				.build());
//
//		testOrderList.add(OrderDTO.builder().orderId(13).userId(2).fundingId(4).fundingName("펀딩2").productName("파스타222")
//				.qty(1).amount(15000).usePoint(1000).discountAmount(1000).status("결제완료").paymentMethod("카드")
//				.fundingStatus("펀딩 진행 중").purchasedAt(Date.valueOf("2025-06-12")).build());

		
		//위에서 userid 세션에서 꺼내오는 거 나중에 추가하기
		// 모델에 테스트 데이터 넣기
		model.addAttribute("orderList", orderService.selectByUserId(5)); //임시 userid

		// 뷰 이름 반환 (mypage.jsp)
		return "user.mypage";
	}
}
