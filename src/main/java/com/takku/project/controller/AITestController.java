package com.takku.project.controller;

import com.takku.project.service.AIService;
import com.takku.project.service.AIServiceImpl;
import com.takku.project.domain.AIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
public class AITestController {

	@Autowired
	private AIService aiService;

	@GetMapping("/ai-form")
	public String showForm() {
		return "funding_ai_form";
	}

	@PostMapping("/ai-generate")
	public String generateFundingText(@RequestParam String keyword, @RequestParam String target, Model model) {

		AIResponse aiResponse = aiService.generateText(keyword, target);

		model.addAttribute("aiResponse", aiResponse);
		return "funding_ai_form";
	}

}
