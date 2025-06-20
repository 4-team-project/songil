package com.takku.project.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.domain.ReviewDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.CouponService;
import com.takku.project.service.ImageService;
import com.takku.project.service.ReviewService;

@Controller
public class ReviewController {

	@Autowired
	private CouponService couponService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ImageService imageService;

	// 리뷰 작성폼
	@GetMapping("/review/write/{couponId}")
	public String reviewForm(@PathVariable("couponId") String couponId, Model model) {
		CouponDTO coupon = couponService.selectByCouponCode(couponId);
		model.addAttribute("couponDTO", coupon);
		return "review_write";
	}

	// 리뷰 등록 처리
	@PostMapping("/review")
	public String submitReview(@ModelAttribute ReviewDTO reviewDTO, @RequestParam("images") List<MultipartFile> images,
			@ModelAttribute("loginUser") UserDTO loginUser, HttpSession session,
			RedirectAttributes redirectAttributes) {
		reviewDTO.setUserId(loginUser.getUserId());

		int result = reviewService.insertReview(reviewDTO);

		String uploadDir = session.getServletContext().getRealPath("/resources/images");

		if (result > 0) {
			for (MultipartFile file : images) {
				if (!file.isEmpty()) {
					try {
						String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
						String fullPath = uploadDir + File.separator + fileName;
						file.transferTo(new File(fullPath));

						ImageDTO image = ImageDTO.builder().reviewId(reviewDTO.getReviewId())
								.imageUrl("/resources/images/" + fileName).build();

						imageService.insertImageUrl(image);

					} catch (IOException e) {
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("resultMessage", "리뷰는 저장되었으나 이미지 업로드에 실패했습니다.");
					}
				}
			}
		}
		return "redirect:/mypage/review";
	}

	// 상품 리뷰 조회
	@GetMapping("/product/{productId}/review")
	public String productReviewList(@PathVariable("productId") Integer productId, Model model) {
		List<ReviewDTO> reviewList = reviewService.reviewByProductId(productId);
		model.addAttribute("reviewList", reviewList);
		return "review_list";
	}
}
