package com.takku.project.controller;

import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.domain.stats.OrderStatsDTO;
import com.takku.project.domain.stats.PopularProductDTO;
import com.takku.project.domain.stats.ProductRePurchaseDTO;
import com.takku.project.domain.stats.SummaryResponse;
import com.takku.project.domain.stats.TagStatsDTO;
import com.takku.project.service.AIService;
import com.takku.project.service.FundingService;
import com.takku.project.service.ProductService;
import com.takku.project.service.StoreService;
import com.takku.project.service.StoreStatsService;
import com.takku.project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class StoreStatsController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private FundingService fundingService;

	@Autowired
	private StoreStatsService statsService;

	@Autowired
	private UserService userService;

	@Autowired
	private AIService aiService;

	@Autowired
	private ProductService productService;

	@GetMapping("/seller/home")
	public String getMain(Model model) {
		Integer storeId = 1;
		Integer userId = 1;

		UserDTO user = userService.selectByUserId(userId);
		StoreDTO store = storeService.selectStoreById(storeId);

		// 통계 조회
		int todayOrderCount = statsService.countTodayOrdersByStoreId(storeId);
		int todaySales = statsService.sumTodaySalesByStoreId(storeId);
		int ongoingFundingCount = statsService.countOngoingFundingsByStoreId(storeId);
		int upcomingFundingCount = statsService.countUpcomingFundingsByStoreId(storeId);

		// Model에 추가
		model.addAttribute("userDTO", user);
		model.addAttribute("storeDTO", store);
		model.addAttribute("todayOrderCount", todayOrderCount);
		model.addAttribute("todaySales", todaySales);
		model.addAttribute("ongoingFundingCount", ongoingFundingCount);
		model.addAttribute("upcomingFundingCount", upcomingFundingCount);

		// 기존 통계
		model.addAttribute("orderStats", statsService.getMonthlyOrderStats(storeId));
		model.addAttribute("popularProducts", statsService.getPopularProducts(storeId));
		model.addAttribute("tagStats", statsService.getTagStats(storeId));
		model.addAttribute("topRePurchased", statsService.getTopRePurchasedProducts(storeId));
		model.addAttribute("ageDistribution", statsService.getAgeDistribution());
		model.addAttribute("genderRatio", statsService.getGenderRatio());
		model.addAttribute("topTagsByGroup", statsService.getTopTagsByAgeGender());

		System.out.println(">>> user.home 컨트롤러 도달");

		return "seller.home";
	}

	// ------------ 밑은 테스트 용 컨트롤러 ------------

	@GetMapping("/store/stats")
	public String getStoreStats(@RequestParam("storeId") int storeId, Model model, HttpSession session) {

		// TODO: 로그인 유저가 해당 storeId의 소유자인지 검증
		// 예시:
		// User loginUser = (User) session.getAttribute("loginUser");
		// if (loginUser == null || !loginUser.ownsStore(storeId)) {
		// return "redirect:/error/unauthorized";
		// }
		Integer userId = 1;
		UserDTO user = userService.selectByUserId(userId);
		StoreDTO store = storeService.selectStoreById(storeId);

		// 통계 데이터 조회
		List<OrderStatsDTO> orderStats = statsService.getMonthlyOrderStats(storeId);
		List<PopularProductDTO> popularProducts = statsService.getPopularProducts(storeId);
		List<TagStatsDTO> tagStats = statsService.getTagStats(storeId);
		List<ProductRePurchaseDTO> topRePurchased = statsService.getTopRePurchasedProducts(storeId);
		// View 전달
		model.addAttribute("userDTO", user);
		model.addAttribute("storeDTO", store);
		model.addAttribute("orderStats", orderStats);
		model.addAttribute("popularProducts", popularProducts);
		model.addAttribute("tagStats", tagStats);
		model.addAttribute("topRePurchased", topRePurchased);

		return "seller/stats";
	}

	@GetMapping("/platform-stats")
	public String platformStats(Model model) {
		model.addAttribute("ageDistribution", statsService.getAgeDistribution());
		model.addAttribute("genderRatio", statsService.getGenderRatio());
		model.addAttribute("topTagsByGroup", statsService.getTopTagsByAgeGender());

		return "seller/platformStats";
	}

	@GetMapping("/product/stats")
	public String getProductStats(@RequestParam("productId") int productId, Model model) {
		model.addAttribute("productId", productId);
		model.addAttribute("productStats", statsService.getProductMonthlyStats(productId));
		model.addAttribute("productAgeStats", statsService.getProductAgeStats(productId));
		model.addAttribute("productGenderStats", statsService.getProductGenderStats(productId));
		model.addAttribute("productDTO", productService.selectByProductId(productId));
		try {
			SummaryResponse summary = aiService.getReviewSummary(productId); // 변경된 반환값
			model.addAttribute("positiveSummary", summary.getPositive());
			model.addAttribute("negativeSummary", summary.getNegative());
		} catch (Exception e) {
			model.addAttribute("summaryListError", "리뷰 요약을 불러오지 못했습니다.");
		}

		return "seller/productStats";
	}

	@GetMapping("/funding/stats")
	public String getFundingStats(@RequestParam("fundingId") int fundingId, Model model) {

		// 펀딩 통계 데이터 조회
		int todayFundingAmount = statsService.getTodayFundingAmount(fundingId);
		int completeOrders = statsService.getFundingCompleteOrderCount(fundingId);
		int refundOrders = statsService.getFundingRefundOrderCount(fundingId);

		model.addAttribute("fundingId", fundingId);
		model.addAttribute("todayFundingAmount", todayFundingAmount);
		model.addAttribute("completeOrders", completeOrders);
		model.addAttribute("refundOrders", refundOrders);
		model.addAttribute("fundingGenderStats", statsService.getFundingGenderRatio(fundingId));
		model.addAttribute("fundingAgeStats", statsService.getFundingAgeDistribution(fundingId));

		return "seller/fundingStats";
	}

}
