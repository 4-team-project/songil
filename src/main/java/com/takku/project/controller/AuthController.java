
package com.takku.project.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.UserDTO;
import com.takku.project.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UserService userService;

	// 회원가입 폼
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("pageName", "회원가입");
		return "auth.signup";
	}

	// 회원가입 처리
	@PostMapping("/signup")
	public String signup(UserDTO userDTO, RedirectAttributes redirectAttributes) {
		int result = userService.insertUser(userDTO);
		redirectAttributes.addFlashAttribute("resultMessage", result > 0 ? "회원가입 성공" : "회원가입 실패");
		return "redirect:/auth/login";
	}

	// 로그인 폼
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("pageName", "로그인");
		return "auth.login";
	}

	// 로그인 처리
	@PostMapping("/login")
	public String login(String phone, String password, String userType, HttpSession session, RedirectAttributes redirectAttributes) {
		// 입력된 번호를 010-0000-0000 형식으로 포맷팅
	    if (phone != null && phone.matches("^\\d{10,11}$")) {
	        if (phone.length() == 11) {
	            phone = phone.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
	        } else if (phone.length() == 10) {
	            phone = phone.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
	        }
	    }
	
		UserDTO user = userService.selectByPhone(phone, password, userType);
		if (user != null) {
	        session.setAttribute("loginUser", user);  // 전역에서 사용 가능
	        redirectAttributes.addFlashAttribute("resultMessage", "로그인 성공");
	        return "redirect:/user/home";  // 로그인 성공 후 이동할 페이지
	    } else {
	        redirectAttributes.addFlashAttribute("resultMessage", "로그인 실패: 정보를 확인해주세요");
	        return "redirect:/auth/login";  // 로그인 폼으로 다시 이동
	    }
	}

	// 로그아웃
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/auth/login";
	}

}

