package com.takku.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.takku.project.domain.OrderDTO;

@Controller
public class MypageController {

	/*
	 * @GetMapping("/user/mypage") public String myPage(Model model) {
	 * model.addAttribute("pageName", "마이페이지"); return "user.mypage"; }
	 */
	
	  @GetMapping("/user/mypage")
	    public String myPage(Model model) {
	        // 페이지명 전달
	        model.addAttribute("pageName", "마이페이지");
	        
	        // 테스트용 더미 데이터 생성
	        List<OrderDTO> testOrderList = new ArrayList<>();
	        testOrderList.add(OrderDTO.builder()
	            .orderId(1)
	            .userId(1001)
	            .fundingId(2001)
	            .qty(2)
	            .amount(30000)
	            .usePoint(0)
	            .discountAmount(0)
	            .status("결제완료")
	            .fundingStatus("진행중")
	            .purchasedAt(Date.valueOf("2025-06-10"))
	            .refundAt(null)
	            .build());
	        
	        testOrderList.add(OrderDTO.builder()
	            .orderId(2)
	            .userId(1001)
	            .fundingId(2002)
	            .qty(1)
	            .amount(15000)
	            .usePoint(1000)
	            .discountAmount(1000)
	            .status("결제취소")
	            .fundingStatus("종료")
	            .purchasedAt(Date.valueOf("2025-05-30"))
	            .refundAt(Date.valueOf("2025-06-01"))
	            .build());
	        
	        // 모델에 테스트 데이터 넣기
	        model.addAttribute("orderList", testOrderList);

	        // 뷰 이름 반환 (mypage.jsp)
	        return "user.mypage";
	    }
}
