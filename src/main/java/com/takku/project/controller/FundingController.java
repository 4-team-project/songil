package com.takku.project.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.ReviewDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.ImageService;
import com.takku.project.service.ProductService;
import com.takku.project.service.ReviewService;
import com.takku.project.service.StoreService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "펀딩 사용자 페이지 API")
@Controller
@RequestMapping("/fundings")
public class FundingController {

	@Autowired
	private FundingService fundingService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ImageService imageService;

	@Autowired
	StoreService storeService;

	@Autowired
	ReviewService reviewService;

	@ApiOperation(value = "펀딩 검색 (페이징)", notes = "검색 조건에 따라 펀딩을 필터링하고 페이징된 목록을 조회합니다.")
	@GetMapping("/ajax")
	public String searchFundingWithPaging(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String sido,
			@RequestParam(required = false) String sigungu, @RequestParam(defaultValue = "latest") String sort,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, Model model) {

		if (categoryId != null && categoryId == 0) {
			categoryId = null;
		}
	    // 값 출력
	    System.out.println("sido: " + sido);
	    System.out.println("sigungu: " + sigungu);

		List<FundingDTO> fundingList = fundingService.getFundingsByConditionWithPaging(keyword, categoryId, sido,
				sigungu, sort, page, size);
		int total = fundingService.getFundingCountByCondition(keyword, categoryId, sido, sigungu);
		int totalPages = (int) Math.ceil((double) total / size);

		Map<Integer, Long> daysLeftMap = new HashMap<>();
		for (FundingDTO funding : fundingList) {
			List<ImageDTO> images = funding.getImages();
			if (images == null || images.isEmpty()) {
				images = imageService.selectImagesByFundingId(funding.getFundingId());
				funding.setImages(images);
			}
			long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), funding.getEndDate().toLocalDate());
			daysLeftMap.put(funding.getFundingId(), daysLeft);
		}

		model.addAttribute("fundinglist", fundingList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("sort", sort);
		model.addAttribute("daysLeftMap", daysLeftMap);

		return "common/funding";
	}

	@ApiOperation(value = "펀딩 검색 (JSON 응답)", notes = "검색 조건에 따라 펀딩을 필터링하고 JSON 응답으로 반환합니다.")
	@GetMapping("/search/json")
	@ResponseBody
	public Map<String, Object> searchFundingJson(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String sido,
			@RequestParam(required = false) String sigungu, @RequestParam(defaultValue = "latest") String sort,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {

		List<FundingDTO> fundingList = fundingService.getFundingsByConditionWithPaging(keyword, categoryId, sido,
				sigungu, sort, page, size);
		int total = fundingService.getFundingCountByCondition(keyword, categoryId, sido, sigungu);
		int totalPages = (int) Math.ceil((double) total / size);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("fundinglist", fundingList);
		result.put("currentPage", page);
		result.put("totalPages", totalPages);
		result.put("sort", sort);

		return result;
	}

	@ApiOperation(value = "펀딩 전체 목록 조회", notes = "기본 조건으로 펀딩 목록을 조회합니다.")
	@GetMapping
	public String getFundings(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String sido,
			@RequestParam(required = false) String sigungu, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, Model model) {

		List<FundingDTO> fundingList = fundingService.getFundingsByConditionWithPaging(keyword, categoryId, sido,
				sigungu, "latest", page, size);
		int total = fundingService.getFundingCountByCondition(keyword, categoryId, sido, sigungu);
		int totalPages = (int) Math.ceil((double) total / size);

		Map<Integer, Long> daysLeftMap = new HashMap<>();
		for (FundingDTO funding : fundingList) {
			long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), funding.getEndDate().toLocalDate());
			daysLeftMap.put(funding.getFundingId(), daysLeft);
		}

		model.addAttribute("fundinglist", fundingList);
		model.addAttribute("daysLeftMap", daysLeftMap);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("sort", "latest");

		return "user.funding-list";
	}

	@ApiOperation(value = "펀딩 상세 조회", notes = "특정 펀딩의 상세 정보를 조회합니다.")
	@GetMapping("/{fundingId}")
	public String getFundingDetail(@PathVariable("fundingId") int fundingId, Model model) {
		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		if (funding == null)
			return "error/error";
		
		ProductDTO product = productService.selectByProductId(funding.getProductId());
		List<ImageDTO> productImages = imageService.selectImagesByProductId(funding.getProductId());
		StoreDTO store = storeService.selectStoreById(funding.getStoreId());
		List<ReviewDTO> reviewlist = reviewService.reviewByProductId(funding.getProductId());

		double avgRating = reviewlist.stream()
			    .mapToInt(ReviewDTO::getRating)
			    .average()
			    .orElse(0.0);		
		int reviewCount = reviewlist.size();

		model.addAttribute("funding", funding);
		model.addAttribute("store", store);
		model.addAttribute("product", product);
		model.addAttribute("productImages", productImages);
		model.addAttribute("reviewlist", reviewlist);
		model.addAttribute("avgRating", avgRating);
		model.addAttribute("reviewCount", reviewCount);

		return "user.funding_detail";
	}
}
