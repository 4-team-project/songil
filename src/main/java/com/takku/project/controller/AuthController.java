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
	
	//회원가입 폼
	@GetMapping("/signup")
	public String signup() {
	
		return "auth/signup";
	}

	//회원가입 처리
	@PostMapping("/signup")
	public String signup(UserDTO userDTO, RedirectAttributes redirectAttributes) {
		int result = userService.insertUser(userDTO);
		redirectAttributes.addFlashAttribute("resultMessage", result>0? "회원가입 성공" : "회원가입 실패" );
		return "redirect:/auth/login";
	}
	
	//로그인 폼
	@GetMapping("/login")
	public String login() {
	
		return "auth/login";
	}
	
	//로그인 처리
	@PostMapping("/login")
	public String login(String phone, String password, HttpSession session, RedirectAttributes redirectAttributes) {
		UserDTO user = userService.selectByPhone(phone, password);
		redirectAttributes.addFlashAttribute("resultMessage", user!=null? "로그인 성공" : "로그인 실패");
		return "redirect:main";
	}
	
	//로그아웃
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/auth/login";
	}
	

}
