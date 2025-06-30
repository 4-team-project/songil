package com.takku.project.controller;

import com.takku.project.domain.StoreDTO;
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
	private AIService aiService;

	@Autowired
	private ProductService productService;

	@GetMapping("/store/stats")
	public String getStoreStats(@RequestParam("storeId") int storeId, Model model, HttpSession session) {

		// TODO: 로그인 유저가 해당 storeId의 소유자인지 검증
		// 예시:
		// User loginUser = (User) session.getAttribute("loginUser");
		// if (loginUser == null || !loginUser.ownsStore(storeId)) {
		// return "redirect:/error/unauthorized";
		// }

		// 통계 데이터 조회
		List<OrderStatsDTO> orderStats = statsService.getMonthlyOrderStats(storeId);
		List<PopularProductDTO> popularProducts = statsService.getPopularProducts(storeId);
		List<TagStatsDTO> tagStats = statsService.getTagStats(storeId);
		List<ProductRePurchaseDTO> topRePurchased = statsService.getTopRePurchasedProducts(storeId);
		StoreDTO store = storeService.selectStoreById(storeId);
		System.out.println(store);
		System.out.println(fundingService.selectFundingByFundingId(1));
		// View 전달
		model.addAttribute("storeId", storeId); // 필요한 경우 JSP에서 storeId 사용 가능
		model.addAttribute("storeDTO", store);
		model.addAttribute("orderStats", orderStats);
		model.addAttribute("popularProducts", popularProducts);
		model.addAttribute("tagStats", tagStats);
		model.addAttribute("topRePurchased", topRePurchased);

		return "seller/stats"; // 예시 jsp
	}

	@GetMapping("/platform-stats")
	public String platformStats(Model model) {
		model.addAttribute("ageDistribution", statsService.getAgeDistribution());
		model.addAttribute("genderRatio", statsService.getGenderRatio());
		model.addAttribute("topTagsByGroup", statsService.getTopTagsByAgeGender());

		return "seller/platformStats"; // JSP 경로
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

}
