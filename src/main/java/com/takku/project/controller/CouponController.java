package com.takku.project.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.FundingDTO;
import com.takku.project.mapper.FundingMapper;
import com.takku.project.service.CouponService;
import com.takku.project.service.FundingService;

@Controller
public class CouponController {

	@Autowired
	private CouponService couponService;

	@Autowired
	private FundingService fundingService;
	

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
		  	CouponDTO coupon = couponService.selectByCouponCode(couponCode);  // 이 메서드는 Service/Mapper에 구현돼 있어야 함
		    model.addAttribute("coupon", coupon);
		  return "coupon/sellerCheck";
	  }
  
  @GetMapping("/user/coupon")
    public String homePage() {
        return "pages/user/coupon"; 
    }

	// 쿠폰 사용 처리
	@PostMapping("/{couponCode}/use")
	public String useCoupon(@PathVariable("couponCode") String couponCode) {
		System.out.println("*******************couponCode"+couponCode);
		//couponService.updateCouponUseStatus(couponCode, "사용됨");
		return "coupon/useCheck";
	}

	// 리뷰 작성 후, 해당 쿠폰 리뷰 상태 업데이트
	@PostMapping("/{couponId}/reviewed")
	public String markReviewed(@PathVariable("couponId") Integer couponId) {
		couponService.updateCouponReviewed(couponId, 1);
		return "redirect:/mypage/coupon";
	}
}