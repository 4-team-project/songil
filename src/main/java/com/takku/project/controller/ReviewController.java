package com.takku.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.ReviewDTO;
import com.takku.project.service.CouponService;
import com.takku.project.service.ReviewService;

@Controller
public class ReviewController {
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private ReviewService reviewService;
	
	//리뷰 작성폼
	@GetMapping("/review/write/{couponId}")
	public String reviewForm(@PathVariable("couponId") String couponId, Model model) {
		CouponDTO coupon = couponService.selectByCouponCode(couponId);
		model.addAttribute("couponDTO", coupon);
		return "review_write";
	}
	
	//리뷰 등록 처리
	@PostMapping("/review")
	public String submitReview(ReviewDTO reviewDTO, RedirectAttributes ra) {
		int result = reviewService.insertReview(reviewDTO);
		if(result > 0) {
			ra.addFlashAttribute("resultMessage", "리뷰가 등록되었습니다.");
		}else {
			ra.addFlashAttribute("resultMessage", "리뷰 등록을 실패하였습니다.");
		}
		return "redirect:/songil/review";
	}
	
	//상품 리뷰 조회
	@GetMapping("/product/{productId}/review")
	public String productReviewList(@PathVariable("productId") Integer productId, Model model) {
		List<ReviewDTO> reviewList = reviewService.reviewByProductId(productId);
		model.addAttribute("reviewList", reviewList);
		return "review_list";
	}
}
