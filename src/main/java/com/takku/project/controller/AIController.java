package com.takku.project.controller;

import com.takku.project.domain.AIResponse;
import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.stats.SummaryResponse;
import com.takku.project.service.AIService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ai")
@Api(tags = "AI 컨트롤러", description = "AI 기반 텍스트 생성 및 추천 기능 API")
public class AIController {

	@Autowired
	private AIService aiService;

	// ======= [JSON 응답: 리뷰 요약 결과] =======
	@GetMapping(value = "/api/summary/{productId}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "리뷰 요약 조회 (JSON)", notes = "FastAPI 서버를 통해 해당 상품의 리뷰 요약 리스트를 반환합니다.")
	public ResponseEntity<?> getSummaryJson(@PathVariable int productId) {
		try {
			SummaryResponse summary = aiService.getReviewSummary(productId);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(summary);
		} catch (Exception e) {
			return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON)
					.body("{\"error\": \"" + e.getMessage() + "\"}");
		}
	}
// storeStatsController @GetMapping("/product/stats") 에서 확인 가능
//	// ======= [view 응답: 리뷰 요약 결과] =======
//	@GetMapping("/summary/{productId}")
//	@ApiOperation(value = "리뷰 요약 조회 (View)", notes = "FastAPI를 통해 상품 리뷰를 요약하고 HTML 뷰로 보여줍니다.")
//	public String getSummaryView(@PathVariable int productId, Model model) {
//		try {
//			List<String> summaryList = aiService.getReviewSummary(productId);
//			model.addAttribute("productId", productId);
//			model.addAttribute("summaryList", summaryList != null ? summaryList : List.of());
//		} catch (Exception e) {
//			model.addAttribute("summaryError", "리뷰가 없습니다.");
//			model.addAttribute("summaryList", List.of());
//		}
//		return "seller/productStats";
//	}

	// ======= [JSON 응답: 추천 결과] =======
	@GetMapping(value = "/api/recommend/{userId}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "추천 결과 조회 (JSON)", notes = "Flask 서버를 통해 유저 기반 추천 결과를 JSON으로 반환합니다.")
	public ResponseEntity<?> getRecommendationsJson(@PathVariable int userId) {
		try {
			List<FundingDTO> recommendationList = aiService.getRecommendations(userId);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(recommendationList);
		} catch (Exception e) {
			return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON)
					.body("{\"error\":\"" + e.getMessage() + "\"}");
		}
	}

	// ======= [JSON 응답: 글 생성 결과] =======
	@PostMapping(value = "/api/ai-generate", produces = "application/json; charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "상품 홍보글 생성 (JSON)", notes = "AI를 통해 키워드와 타겟을 기반으로 상품 홍보글을 생성하여 JSON으로 반환합니다.")
	public ResponseEntity<AIResponse> generateFundingTextJson(@RequestParam String keyword,
			@RequestParam String target) {
		try {
			AIResponse aiResponse = aiService.generateText(keyword, target);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(aiResponse);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(AIResponse.builder().content("error: " + e.getMessage()).build());
		}
	}

	// ======= [뷰 응답: 추천 결과] =======
	@GetMapping("/recommend-view/{userId}")
	@ApiOperation(value = "추천 결과 조회 (View)", notes = "추천 결과를 HTML 뷰에 표시합니다.")
	public String getRecommendationsView(@PathVariable int userId, Model model) {
		try {
			List<FundingDTO> recommendationList = aiService.getRecommendations(userId);
			model.addAttribute("recommendList", recommendationList);
			System.out.println("추천 펀딩 수: " + recommendationList.size());

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("recommendError", e.getMessage());
		}
		return "user.home";
	}

	// ======= [뷰 응답: 글 생성 폼] =======
	@GetMapping("/ai-form")
	@ApiOperation(value = "홍보글 생성 폼 페이지", notes = "상품 홍보글 생성을 위한 입력 폼을 반환합니다.")
	public String showForm() {
		return "pages/seller/funding_ai_form";
	}

	// ======= [뷰 응답: 글 생성 실행] =======
	@PostMapping("/ai-generate")
	@ApiOperation(value = "상품 홍보글 생성 실행 (View)", notes = "AI를 통해 생성된 홍보글을 HTML 뷰에 표시합니다.")
	public String generateFundingTextView(@RequestParam String keyword, @RequestParam String target, Model model) {
		try {
			AIResponse aiResponse = aiService.generateText(keyword, target);
			model.addAttribute("aiResponse", aiResponse);
		} catch (Exception e) {
			model.addAttribute("aiError", e.getMessage());
		}
		return "pages/seller/funding_ai_form";
	}
}
