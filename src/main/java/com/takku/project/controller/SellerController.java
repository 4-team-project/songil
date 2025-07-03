package com.takku.project.controller;

import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.domain.stats.OrderStatsDTO;
import com.takku.project.domain.stats.PopularProductDTO;
import com.takku.project.domain.stats.ProductRePurchaseDTO;
import com.takku.project.domain.stats.TagStatsDTO;
import com.takku.project.service.StoreService;
import com.takku.project.service.StoreStatsService;
import com.takku.project.service.UserService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/seller")
@Api(tags = "판매자 관련 API")
public class SellerController {

	@Autowired
	UserService userService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreStatsService statsService;

	@GetMapping("/mypage")
	@ApiOperation(value = "판매자 마이페이지", notes = "판매자의 기본 정보를 확인할 수 있는 마이페이지입니다.")
	public String myPage(HttpSession session, Model model) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		// TODO 세션에서만 읽어오도록 수정
		UserDTO user = userService.selectByUserId(3); // 테스트용
		model.addAttribute("loginUser", user);
		return "seller.mypage";
	}

	@PostMapping("/mypage/update")
	@ApiOperation(value = "판매자 정보 수정", notes = "판매자의 프로필 정보를 수정합니다.")
	public String updateMyPage(@ModelAttribute UserDTO updatedUser, HttpSession session,
			RedirectAttributes redirectAttributes) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		// TODO 세션에서만 읽어오도록 수정
		UserDTO user = userService.selectByUserId(3); // 테스트용
		if (loginUser == null) {
			return "redirect:/auth/login";
		}
		updatedUser.setUserId(user.getUserId());
		userService.updateUser(updatedUser);
		session.setAttribute("loginUser", updatedUser);
		redirectAttributes.addFlashAttribute("updateSuccess", true);
		return "redirect:/seller/mypage";
	}

	@PostMapping("/partner/change")
	@ResponseBody
	@ApiOperation(value = "파트너 상태 변경", notes = "판매자의 파트너 등록/해제를 처리합니다.")
	public String changePartnerStatus(@RequestParam("action") String action, HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		// TODO 세션에서만 읽어오도록 수정
		UserDTO user = userService.selectByUserId(3); // 테스트용
		String newStatus = action.equals("register") ? "Y" : "N";
		user.setIsPartner(newStatus);
		userService.updateUser(user);
		session.setAttribute("loginUser", loginUser);
		return "success";
	}

	@GetMapping("/home")
	@ApiOperation(value = "판매자 홈 대시보드", notes = "판매자의 홈 화면에서 통계 데이터를 확인합니다.")
	public String getMain(@RequestParam(required = false) String msg, Model model) {
		// TODO 세션에서 읽어오도록 설정
		Integer storeId = 1;
		Integer userId = 1;
		UserDTO user = userService.selectByUserId(userId);
		StoreDTO store = storeService.selectStoreById(storeId);
		int todayOrderCount = statsService.countTodayOrdersByStoreId(storeId);
		int todaySales = statsService.sumTodaySalesByStoreId(storeId);
		int ongoingFundingCount = statsService.countOngoingFundingsByStoreId(storeId);
		int upcomingFundingCount = statsService.countUpcomingFundingsByStoreId(storeId);
		model.addAttribute("userDTO", user);
		model.addAttribute("storeDTO", store);
		model.addAttribute("todayOrderCount", todayOrderCount);
		model.addAttribute("todaySales", todaySales);
		model.addAttribute("ongoingFundingCount", ongoingFundingCount);
		model.addAttribute("upcomingFundingCount", upcomingFundingCount);
		model.addAttribute("orderStats", statsService.getMonthlyOrderStats(storeId));
		model.addAttribute("popularProducts", statsService.getPopularProducts(storeId));
		model.addAttribute("tagStats", statsService.getTagStats(storeId));
		model.addAttribute("topRePurchased", statsService.getTopRePurchasedProducts(storeId));
		model.addAttribute("ageDistribution", statsService.getAgeDistribution());
		model.addAttribute("genderRatio", statsService.getGenderRatio());
		model.addAttribute("topTagsByGroup", statsService.getTopTagsByAgeGender());
		return "seller.home";
	}

	@GetMapping("/stats")
	@ApiOperation(value = "매장 통계 조회", notes = "판매자가 자신의 매장에 대한 통계를 조회합니다.")
	public String getStoreStats(@RequestParam("storeId") int storeId, Model model, HttpSession session) {
		// TODO 세션에서 읽어오도록 설정
		Integer userId = 1;
		UserDTO user = userService.selectByUserId(userId);
		StoreDTO store = storeService.selectStoreById(storeId);
		List<OrderStatsDTO> orderStats = statsService.getMonthlyOrderStats(storeId);
		List<PopularProductDTO> popularProducts = statsService.getPopularProducts(storeId);
		List<TagStatsDTO> tagStats = statsService.getTagStats(storeId);
		List<ProductRePurchaseDTO> topRePurchased = statsService.getTopRePurchasedProducts(storeId);
		model.addAttribute("userDTO", user);
		model.addAttribute("storeDTO", store);
		model.addAttribute("orderStats", orderStats);
		model.addAttribute("popularProducts", popularProducts);
		model.addAttribute("tagStats", tagStats);
		model.addAttribute("topRePurchased", topRePurchased);
		return "seller/stats";
	}
}
