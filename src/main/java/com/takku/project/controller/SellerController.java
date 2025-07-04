package com.takku.project.controller;

import com.takku.project.domain.*;
import com.takku.project.service.*;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/seller")
@Api(tags = "소상공인 관련 API")
public class SellerController {

	@Autowired
	UserService userService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private StoreStatsService statsService;

	@GetMapping("/mypage")
	@ApiOperation(value = "소상공인 마이페이지", notes = "소상공인의 기본 정보를 확인할 수 있는 마이페이지입니다.")
	public String myPage(HttpSession session, Model model) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		UserDTO user = userService.selectByUserId(loginUser.getUserId());
		model.addAttribute("loginUser", user);
		return "seller.mypage";
	}

	@PostMapping("/mypage/update")
	@ApiOperation(value = "판매자 정보 수정", notes = "소상공인의 프로필 정보를 수정합니다.")
	public String updateMyPage(@ModelAttribute UserDTO updatedUser, HttpSession session,
			RedirectAttributes redirectAttributes) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/auth/login";
		}
		UserDTO user = userService.selectByUserId(loginUser.getUserId());
		updatedUser.setUserId(user.getUserId());
		userService.updateUser(updatedUser);
		session.setAttribute("loginUser", updatedUser);
		redirectAttributes.addFlashAttribute("updateSuccess", true);
		return "redirect:/seller/mypage";
	}

	@PostMapping("/partner/change")
	@ResponseBody
	@ApiOperation(value = "파트너 상태 변경", notes = "소상공인의 파트너 등록/해제를 처리합니다.")
	public String changePartnerStatus(@RequestParam("action") String action, HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		UserDTO user = userService.selectByUserId(loginUser.getUserId());
		String newStatus = action.equals("register") ? "Y" : "N";
		user.setIsPartner(newStatus);
		userService.updateUser(user);
		session.setAttribute("loginUser", user);
		return "success";
	}

	@GetMapping("/home")
	@ApiOperation(value = "판매자 홈 대시보드", notes = "소상공인의 홈 화면에서 통계 데이터를 확인합니다.")
	public String getMain(@RequestParam(required = false) String msg, Model model, HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		UserDTO user = userService.selectByUserId(loginUser.getUserId());
		model.addAttribute("userDTO", user);

		StoreDTO store = storeService.selectStoreNameByUserId(user.getUserId());
		if (store != null) {
			Integer storeId = store.getStoreId();
			model.addAttribute("storeDTO", store);
			session.setAttribute("store", store);

			model.addAttribute("todayOrderCount", statsService.countTodayOrdersByStoreId(storeId));
			model.addAttribute("todaySales", statsService.sumTodaySalesByStoreId(storeId));
			model.addAttribute("ongoingFundingCount", statsService.countOngoingFundingsByStoreId(storeId));
			model.addAttribute("upcomingFundingCount", statsService.countUpcomingFundingsByStoreId(storeId));
			model.addAttribute("orderStats", statsService.getMonthlyOrderStats(storeId));
			model.addAttribute("popularProducts", statsService.getPopularProducts(storeId));
			model.addAttribute("tagStats", statsService.getTagStats(storeId));
			model.addAttribute("topRePurchased", statsService.getTopRePurchasedProducts(storeId));
			model.addAttribute("ageDistribution", statsService.getAgeDistribution());
			model.addAttribute("genderRatio", statsService.getGenderRatio());
			model.addAttribute("topTagsByGroup", statsService.getTopTagsByAgeGender());
		}
		return "seller.home";
	}

	@GetMapping("/stats")
	@ApiOperation(value = "매장 통계 조회", notes = "소상공인이 자신의 매장에 대한 통계를 조회합니다.")
	public String getStoreStats(Model model, HttpSession session) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
		UserDTO user = userService.selectByUserId(loginUser.getUserId());
		model.addAttribute("userDTO", user);

		StoreDTO store = (StoreDTO) session.getAttribute("store");
		if (store == null) {
			return "seller.home";
		}

		Integer storeId = store.getStoreId();
		model.addAttribute("storeDTO", store);
		model.addAttribute("orderStats", statsService.getMonthlyOrderStats(storeId));
		model.addAttribute("popularProducts", statsService.getPopularProducts(storeId));
		model.addAttribute("tagStats", statsService.getTagStats(storeId));
		model.addAttribute("topRePurchased", statsService.getTopRePurchasedProducts(storeId));
		return "seller/stats";
	}
}
