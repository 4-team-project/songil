package com.takku.project.controller;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.UserDTO;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/seller")
public class SellerController {

	@Autowired
	UserService userService;

	// 1. 소상공인 내 정보 메인
	@GetMapping("/mypage")
	public String myPage(HttpSession session, Model model) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

		// 테스트용
		UserDTO user = userService.selectByUserId(3);

		model.addAttribute("loginUser", user);
		return "seller.mypage";
	}

	// 회원정보 수정 처리
	@PostMapping("/mypage/update")
	public String updateMyPage(@ModelAttribute UserDTO updatedUser, HttpSession session,
			RedirectAttributes redirectAttributes) {
		// 세션에서 현재 로그인한 사용자 정보 가져오기
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

		// 테스트용
		UserDTO user = userService.selectByUserId(3);

		if (loginUser == null) {
			// 로그인 세션 만료 시 로그인 페이지로 리다이렉트
			return "redirect:/auth/login";
		}

		// 기존 로그인 사용자 ID 기준으로 값 보정
		updatedUser.setUserId(user.getUserId());

		// DB에 업데이트
		userService.updateUser(updatedUser);

		// 세션 정보도 최신값으로 갱신
		session.setAttribute("loginUser", updatedUser);

		// 알림 메시지 등 전달할 수 있음 (선택)
		redirectAttributes.addFlashAttribute("updateSuccess", true);

		return "redirect:/seller/mypage";
	}
}
