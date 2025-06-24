package com.takku.project.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.service.CouponService;
import com.takku.project.service.FundingService;
import com.takku.project.service.ProductService;

@Controller
public class CouponController {

	@Autowired
	private CouponService couponService;

	@Autowired
	private FundingService fundingService;

	@Autowired
	private ProductService productService;

	@RequestMapping("/generateQr")
	public String generateQr(Model model) {
		// QR 찍으면 이동할 URL

		//
		FundingDTO data = new FundingDTO();
		data.setFundingId(1);
		data.setFundingName("테스트");

		String targetUrl = "http://192.168.0.84:9999/mypage/coupon/sellerCheck/?couponCode=123&fundingId=1&fundingName=테스트";

		try {
			// URL Encoding (안전하게)
			String encodedUrl = URLEncoder.encode(targetUrl, "UTF-8");

			// api.qrserver.com QR 이미지 URL 생성
			String qrImageUrl = "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + encodedUrl;

			// JSP로 전달
			model.addAttribute("qrImageUrl", qrImageUrl);
			model.addAttribute("fundingId", data.getFundingId());
			model.addAttribute("fundingName", data.getFundingName());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "coupon/createQR";
	}

	@GetMapping("/sellerCheck")
	public String sellerCheck(Model model, String couponCode) {
		CouponDTO coupon = couponService.selectByCouponCode(couponCode);
		model.addAttribute("coupon", coupon);
		return "coupon/sellerCheck";
	}

	// 쿠폰 사용 처리
	@PostMapping("/{couponCode}/use")
	public String useCoupon(@PathVariable("couponCode") String couponCode) {
		System.out.println("*******************couponCode" + couponCode);
		// couponService.updateCouponUseStatus(couponCode, "사용됨");
		return "coupon/useCheck";
	}

	// 리뷰 작성 후, 해당 쿠폰 리뷰 상태 업데이트
	@PostMapping("/{couponId}/reviewed")
	public String markReviewed(@PathVariable("couponId") Integer couponId) {
		couponService.updateCouponReviewed(couponId, 1);
		return "redirect:/mypage/coupon";
	}

	// 내 쿠폰함 페이지
	@GetMapping("/user/coupon")
	public String couponPage(Model model) {
		model.addAttribute("pageName", "내 쿠폰함");
		List<CouponDTO> coupons = couponService.selectCouponByUserId(5); // userId 받아서 해야됨
		model.addAttribute("coupons", coupons);

		Map<Integer, FundingDTO> fundingMap = new HashMap<>();
		Map<Integer, ProductDTO> productMap = new HashMap<>();

		for (CouponDTO coupon : coupons) {
			int fundingId = coupon.getFundingId();

			// 1. Funding 가져오기
			if (!fundingMap.containsKey(fundingId)) {
				FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
				fundingMap.put(fundingId, funding);

				// 2. Product 가져오기
				int productId = funding.getProductId(); // 여기서 productId 꺼냄
				if (!productMap.containsKey(productId)) {
					ProductDTO product = productService.selectByProductId(productId);
					productMap.put(productId, product);
				}
			}
		}
		model.addAttribute("fundingMap", fundingMap);
		model.addAttribute("productMap", productMap);
		return "user.coupon";
	}

	// 쿠폰 상세 보기
	@PostMapping("/user/coupon_detail")
	public String couponDetailPage(Model model, @RequestParam("couponId") int couponId,
			@RequestParam("discountRate") double discountRate) {
		model.addAttribute("pageName", "내 쿠폰함");
		CouponDTO coupon = couponService.selectByCouponId(couponId);
		model.addAttribute("coupon", coupon);
		FundingDTO funding = fundingService.selectFundingByFundingId(coupon.getFundingId());
		model.addAttribute("funding", funding);
		int intDiscountRate = (int) discountRate;
		model.addAttribute("intDiscountRate", intDiscountRate);
		return "user.coupon_detail";
	}
}
