package com.takku.project.controller;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.OrderDTO;
import com.takku.project.domain.ReviewDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.CouponService;
import com.takku.project.service.OrderService;
import com.takku.project.service.ReviewService;
import com.takku.project.service.UserService;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mypage")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	ReviewService reveiwService;
	
	@Autowired
	CouponService couponService;
	
	@Autowired
	OrderService orderService;

	// 1. 마이페이지 메인 (내 정보 보기)
	@GetMapping
	public String myPage(@ModelAttribute("loginUser") UserDTO loginUser, Model model) {
		UserDTO user = userService.selectByUserId(loginUser.getUserId());
		model.addAttribute("user", user);

		return "mypage"; // mypage.jsp
	}

	// 2. 회원정보 수정 폼
	@GetMapping("/edit")
	public String editForm(@ModelAttribute("loginUser") UserDTO loginUser, Model model) {
		UserDTO user = userService.selectByUserId(loginUser.getUserId());
		model.addAttribute("user", user);

		return "mypage_edit"; // mypage_edit.jsp
	}

	// 3. 회원정보 수정 처리
	@PutMapping
	public String updateUser(@ModelAttribute("loginUser") UserDTO loginUser, @ModelAttribute UserDTO user,
			RedirectAttributes redirectAttributes) {
		user.setUserId(loginUser.getUserId()); // 세션 정보 기준으로 userId 고정

		int result = userService.updateUser(user);

		String resultMessage = (result > 0) ? "회원정보가 수정되었습니다." : "수정 실패!";
		redirectAttributes.addFlashAttribute("resultMessage", resultMessage);

		return "redirect:/mypage";
	}

	// 4. 내 리뷰 관리
	@GetMapping("/reviews")
	public String myReviews(@ModelAttribute("loginUser") UserDTO loginUser, Model model) {
		List<ReviewDTO> reviewList = reveiwService.reviewByUserID(loginUser.getUserId());
		model.addAttribute("reviewList", reviewList);

		return "mypage_reviews"; // mypage_reviews.jsp로 이동
	}
	
	//5.내 쿠폰함
	@GetMapping("/coupons")
	public String myCoupons(@ModelAttribute("loginUser") UserDTO loginUser, Model model) {
	    List<CouponDTO> couponList = couponService.selectCouponByUserId(loginUser.getUserId());
	    model.addAttribute("couponList", couponList);
	    return "mypage_coupons";
	}
	
	//6.내 주문내역 조회
	@GetMapping("/orders")
	public String myOrders(@ModelAttribute("loginUser") UserDTO loginUser, Model model) {
	    List<OrderDTO> orderList = orderService.selectByUserId(loginUser.getUserId());
	    model.addAttribute("orderList", orderList);
	    return "mypage_orders";
	}
}
