package com.takku.project.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.service.CouponService;
import com.takku.project.service.FundingService;
import com.takku.project.service.ImageService;
import com.takku.project.service.ProductService;
import com.takku.project.service.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CouponController {

	@Autowired
	private CouponService couponService;

	@Autowired
	private FundingService fundingService;

	@Autowired
	private ProductService productService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private ImageService imageService;

	/**
	 * [1] 쿠폰 발급 + QR 코드 출력 페이지
	 */
	// 여기 접근할때 localhost말고 본인 IP로 들어가야함 -> baseurl때문
	@GetMapping("/coupon/issue")
	public String issueCoupon(@RequestParam("fundingId") int fundingId, Model model, HttpServletRequest request) {

		int userId = 5; // TODO: 로그인 유저로 교체

		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		int storeId = funding.getStoreId();
		Date fundingEndDate = funding.getEndDate();

		String couponCode = couponService.createCouponAndReturnCode(fundingId, userId, storeId, fundingEndDate);

		// ✅ 현재 서버 정보로 QR찍고 들어갈 URL 동적 생성
		String scheme = request.getScheme(); // http 또는 https
		String serverName = request.getServerName(); // ex: 192.168.0.47 or 본인 IP
		int serverPort = request.getServerPort(); // ex: 9999
		String contextPath = request.getContextPath(); // ex: /project

		String baseUrl = scheme + "://" + serverName + ":" + serverPort + contextPath
				+ "/coupon/sellerCheck?couponCode=" + couponCode;

		String qrImageUrl = "";
		try {
			String encodedUrl = URLEncoder.encode(baseUrl, "UTF-8");
			qrImageUrl = "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + encodedUrl;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		model.addAttribute("qrImageUrl", qrImageUrl);
		model.addAttribute("couponCode", couponCode);
		return "coupon.issuedQR";
	}

	/**
	 * [2] 가맹점에서 QR로 접근 시 쿠폰 확인
	 */
	@GetMapping("/coupon/sellerCheck")
	public String sellerCheck(Model model, @RequestParam("couponCode") String couponCode) {
		CouponDTO coupon = couponService.selectByCouponCode(couponCode);
		model.addAttribute("coupon", coupon);

		if (coupon != null) {
			FundingDTO funding = fundingService.selectFundingByFundingId(coupon.getFundingId());
			ProductDTO product = productService.selectByProductId(funding.getProductId());
			StoreDTO store = storeService.selectStoreById(funding.getStoreId());
			List<ImageDTO> image = imageService.selectImagesByProductId(product.getProductId());

			model.addAttribute("pageName", "쿠폰 확인");
			model.addAttribute("funding", funding);
			model.addAttribute("product", product);
			model.addAttribute("store", store); // ⭐️ storeName 전달
			model.addAttribute("image", image);
		}

		return "coupon.sellerCheck";
	}

	/**
	 * [3] 쿠폰 사용 처리
	 */
	@PostMapping("/{couponCode}/use")
	public String useCoupon(@PathVariable("couponCode") String couponCode) {
		couponService.updateCouponUseStatus(couponCode, "사용");
		return "coupon/useCheck";
	}

	/**
	 * [4] 리뷰 작성 체크
	 */
	@PostMapping("/{couponId}/reviewed")
	public String markReviewed(@PathVariable("couponId") Integer couponId) {
		couponService.updateCouponReviewed(couponId);
		return "redirect:/coupon/user/list";
	}

	/**
	 * [5] 사용자 쿠폰 목록 조회
	 */
	@GetMapping("/user/coupon")
	public String userCouponList(Model model) {
		model.addAttribute("pageName", "내 쿠폰함");

		int userId = 5; // TODO: 실제 로그인 사용자
		List<CouponDTO> coupons = couponService.selectCouponByUserId(userId);
		model.addAttribute("coupons", coupons);

		Map<Integer, FundingDTO> fundingMap = new HashMap<>();
		Map<Integer, ProductDTO> productMap = new HashMap<>();
		Map<Integer, StoreDTO> storeMap = new HashMap<>();

		for (CouponDTO coupon : coupons) {
			int fundingId = coupon.getFundingId();
			if (!fundingMap.containsKey(fundingId)) {
				FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
				fundingMap.put(fundingId, funding);

				int productId = funding.getProductId();
				if (!productMap.containsKey(productId)) {
					ProductDTO product = productService.selectByProductId(productId);
					productMap.put(productId, product);
				}

				int storeId = funding.getStoreId();
				if (!storeMap.containsKey(storeId)) {
					StoreDTO store = storeService.selectStoreById(storeId);
					storeMap.put(storeId, store);
				}
			}
		}

		model.addAttribute("fundingMap", fundingMap);
		model.addAttribute("productMap", productMap);
		model.addAttribute("storeMap", storeMap);

		return "user.coupon";
	}

	/**
	 * [6] 쿠폰 상세 페이지
	 */
	@PostMapping("/user/coupon/detail")
	public String couponDetailPage(Model model, @RequestParam("couponId") int couponId) {

		model.addAttribute("pageName", "쿠폰 상세정보");

		CouponDTO coupon = couponService.selectByCouponId(couponId);
		FundingDTO funding = fundingService.selectFundingByFundingId(coupon.getFundingId());
		ProductDTO product = productService.selectByProductId(funding.getProductId());
		StoreDTO store = storeService.selectStoreById(funding.getFundingId());

		model.addAttribute("coupon", coupon);
		model.addAttribute("funding", funding);
		model.addAttribute("product", product);
		model.addAttribute("store", store);

		return "user.coupon_detail";
	}
}
