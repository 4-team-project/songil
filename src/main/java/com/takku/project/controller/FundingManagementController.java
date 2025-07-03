package com.takku.project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.TagDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.AIService;
import com.takku.project.service.FundingService;
import com.takku.project.service.ImageService;
import com.takku.project.service.ProductService;
import com.takku.project.service.StoreService;
import com.takku.project.service.TagService;

@Controller
@RequestMapping("/seller/fundings")
public class FundingManagementController {

	@Autowired
	FundingService fundingService;

	@Autowired
	StoreService storeService;

	@Autowired
	ProductService productService;

	@Autowired
	ImageService imageService;

	@Autowired
	TagService tagService;

	@Autowired
	AIService aiService;

	// 펀딩 만들기 -> 한정 상품 펀딩 or 일반 펀딩
	@GetMapping("/create-step1")
	public String selectStoreNameByUserId(Model model, HttpSession session) {

		// TODO 세션으로 나중에 처리
		StoreDTO storeDTO = storeService.selectStoreById(1); // 임시 상점

		model.addAttribute("storeDTO", storeDTO);

		FundingDTO fundingDTO = new FundingDTO();
		fundingDTO.setStoreId(storeDTO.getStoreId());

		session.setAttribute("fundingDTO", fundingDTO);
		session.setAttribute("storeDTO", storeDTO);
		session.removeAttribute("aiRetryCount");

		return "seller.createFunding";
	}

	// 상품 정보
	@GetMapping("/create-step2")
	public String createStep2(@RequestParam("type") String type, Model model, HttpSession session) {

		StoreDTO storeDTO = (StoreDTO) session.getAttribute("storeDTO");
		FundingDTO fundingDTO = (FundingDTO) session.getAttribute("fundingDTO");

		if (storeDTO == null || fundingDTO == null) {
			System.out.println("funding or store dto가 null");
			return "redirect:/seller/fundings/create-step1";
		}

		model.addAttribute("storeDTO", storeDTO);

		if ("general".equals(type)) {
			fundingDTO.setFundingType("일반");
			session.setAttribute("fundingDTO", fundingDTO);
			session.setAttribute("fundingType", "general");
			return "seller.normalFunding";
		} else if ("limited".equals(type)) {
			fundingDTO.setFundingType("한정");
			session.setAttribute("fundingDTO", fundingDTO);
			session.setAttribute("fundingType", "limited");
			return "seller.existMenu";
		} else {
			return "seller.createFunding";
		}
	}

	// 펀딩이름 판매가 최소 판매개수 최대판대매수 인당구매
	@PostMapping("/create-step3")
	public String insertFundingMenuDetail(@ModelAttribute FundingDTO funding, HttpSession session, Model model) {

		FundingDTO fundingDTO = (FundingDTO) session.getAttribute("fundingDTO");

		StoreDTO store = (StoreDTO) session.getAttribute("storeDTO");
		model.addAttribute("storeDTO", store);

		fundingDTO.setProductId(funding.getProductId()); // 상품ID
		fundingDTO.setSalePrice(funding.getSalePrice()); // 판매가
		if (fundingDTO.getFundingType().equals("한정")) {
			fundingDTO.setTargetQty(0);
		} else {
			fundingDTO.setTargetQty(funding.getTargetQty()); // 최소 판매 개수
		}
		fundingDTO.setMaxQty(funding.getMaxQty()); // 최대 판매 개수
		fundingDTO.setPerQty(funding.getPerQty()); // 인당 구매 가능 개수

		// 메뉴 이미지 선택도 가능하게
		ProductDTO productDTO = productService.selectByProductId(fundingDTO.getProductId());
		model.addAttribute("productDTO", productDTO);
		System.out.println(productDTO);
		session.setAttribute("fundingDTO", fundingDTO);

		return "seller.insertDetail";
	}

	@PostMapping(value = "/create-step4", consumes = "application/json")
	public String handleFundingDateAndImages(@RequestBody FundingDTO funding, HttpSession session, Model model) {
		try {
			StoreDTO storeDTO = (StoreDTO) session.getAttribute("storeDTO");
			FundingDTO fundingDTO = (FundingDTO) session.getAttribute("fundingDTO");
			if (storeDTO == null || fundingDTO == null) {
				System.out.println("store or funding DTO가 NULL");
				return "redirect:/seller/fundings/create-step1";
			}

			// 날짜 저장
			fundingDTO.setStartDate(funding.getStartDate());
			fundingDTO.setEndDate(funding.getEndDate());

			// 이미지 리스트 처리 (임시 파일명 그대로 세션에 저장)
			List<ImageDTO> processedImages = new ArrayList<>();
			if (funding.getImages() != null) {
				for (ImageDTO img : funding.getImages()) {
					String fileName = img.getImageUrl(); // UUID.jpg 형식 그대로
					if (fileName != null && fileName.contains(".")) {
						ImageDTO imageDTO = ImageDTO.builder().imageUrl(fileName).build();
						processedImages.add(imageDTO);
					}
				}
			}
			fundingDTO.setImages(processedImages);

			// 상태 설정
			Date today = new Date();
			Date startDate = new Date(funding.getStartDate().getTime());
			fundingDTO.setStatus(startDate.after(today) ? "준비중" : "진행중");

			// 세션 업데이트
			session.setAttribute("fundingDTO", fundingDTO);

			ProductDTO product = productService.selectByProductId(fundingDTO.getProductId());
			model.addAttribute("storeDTO", storeDTO);
			model.addAttribute("product", product);

			return "seller.selectWriteType";

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("에러 발생");
			return "redirect:/seller/fundings/create-step1";
		}
	}

	@GetMapping("/create-step5")
	public String redirectWriteTypePage() {
		return "seller.selectWriteType";
	}

	// 작성 방식 분기 처리만 담당
	@PostMapping("/create-step5")
	public String writeType(@RequestParam("type") String type, HttpSession session) {
		if ("ai".equals(type)) {
			// 초기화 필요
			session.setAttribute("aiRetryCount", 0);
			return "redirect:/seller/fundings/ai-form";
		} else if ("directly".equals(type)) {
			return "redirect:/seller/fundings/direct-form";
		}
		return "redirect:/seller/fundings/selectWriteType";
	}

	@GetMapping("/ai-form")
	public String showAiForm(HttpSession session, Model model) {
		FundingDTO fundingDTO = (FundingDTO) session.getAttribute("fundingDTO");
		ProductDTO product = productService.selectByProductId(fundingDTO.getProductId());
		Integer retryCount = (Integer) session.getAttribute("aiRetryCount");
		if (retryCount == null)
			retryCount = 0;

		model.addAttribute("product", product);
		model.addAttribute("aiRetryCount", retryCount);

		return "seller.aiInsertForm";
	}

	@GetMapping("/direct-form")
	public String showDirectInputForm(HttpSession session, Model model) {
		FundingDTO fundingDTO = (FundingDTO) session.getAttribute("fundingDTO");
		ProductDTO product = productService.selectByProductId(fundingDTO.getProductId());
		model.addAttribute("product", product);
		return "seller.directInsert";
	}

	@PostMapping("/submit-funding")
	public String handleFundingBasicInfo(@ModelAttribute FundingDTO funding, @RequestParam("keywords") String keywords,
			HttpSession session) {

		FundingDTO fundingDTO = (FundingDTO) session.getAttribute("fundingDTO");
		if (fundingDTO == null)
			return "redirect:/seller/fundings/create-step1";

		// 제목/설명 저장
		fundingDTO.setFundingName(funding.getFundingName());
		fundingDTO.setFundingDesc(funding.getFundingDesc());

		// 펀딩 저장
		fundingService.insertFunding(fundingDTO);
		int fundingId = fundingDTO.getFundingId();

		// 이미지 이동 및 DB 저장
		if (fundingDTO.getImages() != null) {
			for (ImageDTO img : fundingDTO.getImages()) {
				try {
					String imageUrl = img.getImageUrl();

					// 상품 이미지 그대로 사용하는 경우 (이미 서버에 존재하는 이미지)
					if (imageUrl != null && imageUrl.startsWith("/image/")) {
						// 그대로 저장 (파일 이동 없이 DB만 insert)
						img.setFundingId(fundingId);
						imageService.insertImageUrl(img);

					} else {
						// 임시 이미지인 경우: 서버 이동 + URL 업데이트
						String newFileName = imageService.moveImageFromTemp(imageUrl); // UUID.jpg 형태
						img.setFundingId(fundingId);
						img.setImageUrl(newFileName);
						imageService.insertImageUrl(img);
					}

				} catch (IOException e) {
					e.printStackTrace(); // 실패해도 다른 이미지 계속 처리
				}
			}
		}

		// 태그 저장
		List<String> tagNames = extractTags(keywords);
		for (String tagName : tagNames) {
			Integer tagId = tagService.getTagIdByName(tagName);
			if (tagId == null) {
				TagDTO tagDTO = new TagDTO();
				tagDTO.setTagName(tagName);
				tagService.insertTag(tagDTO);
				tagId = tagDTO.getTagId();
			}
			tagService.insertFundingTag(fundingId, tagId);
		}

		return "redirect:/seller/fundings/complete";
	}

	@GetMapping("/complete")
	public String fundingComplete(HttpSession session, Model model) {
		FundingDTO funding = (FundingDTO) session.getAttribute("fundingDTO");

		// 예외 처리 (없을 경우 홈으로)
		if (funding == null) {
			return "redirect:/seller/fundings/create-step1";
		}

		model.addAttribute("fundingName", funding.getFundingName());
		model.addAttribute("startDate", funding.getStartDate());
		model.addAttribute("fundingId", funding.getFundingId());

		session.removeAttribute("fundingDTO");
		session.removeAttribute("aiRetryCount");

		return "seller.result";
	}

	// 내 펀딩 목록
	@GetMapping
	public String sellerFundings(@ModelAttribute("loginUser") UserDTO loginUser, Model model) {
		Integer storeId = storeService.findStoreIdByUserId(loginUser.getUserId());
		if (storeId == null) {
			model.addAttribute("fundingList", Collections.emptyList());
			model.addAttribute("message", "상점이 등록되지 않았습니다.");
			return "seller_fundings";
		}

		List<FundingDTO> fundingList = fundingService.findFundingByStoreId(storeId);
		model.addAttribute("fundingList", fundingList);
		return "seller_fundings";
	}

	// 펀딩 상세 조회
	@GetMapping("/{fundingId:[0-9]+}")
	public String fundingDetail(@PathVariable int fundingId, Model model) {
		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		model.addAttribute("fundingDTO", funding);
		return "seller_funding_detail";
	}

	// 펀딩 수정 폼
	@GetMapping("/{fundingId}/edit")
	public String editFundingForm(@PathVariable int fundingId, Model model) {
		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		model.addAttribute("fundingDTO", funding);
		return "seller_funding_edit";
	}

	// 펀딩 수정 처리
	@PutMapping("/{fundingId}")
	public String updateFunding(@ModelAttribute FundingDTO fundingDTO, @PathVariable int fundingId,
			RedirectAttributes redirectAttributes) {
		fundingDTO.setFundingId(fundingId); // ID 세팅
		int result = fundingService.updateFunding(fundingDTO);
		if (result > 0) {
			redirectAttributes.addFlashAttribute("resultMessage", "펀딩이 수정되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("resultMessage", "수정 실패");
		}
		return "redirect:/seller/fundings";
	}

	// 펀딩 삭제
	@DeleteMapping("/{fundingId}")
	public String deleteFunding(@PathVariable int fundingId, RedirectAttributes redirectAttributes) {
		int result = fundingService.deleteFunding(fundingId);
		if (result > 0) {
			redirectAttributes.addFlashAttribute("resultMessage", "펀딩이 삭제되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("resultMessage", "삭제 실패");
		}
		return "redirect:/seller/fundings";
	}

	public List<String> extractTags(String rawInput) {
		return Arrays.stream(rawInput.replaceAll("[\\[\\]#]", "") // 대괄호, # 제거
				.split("[,\\s]+") // 쉼표 or 공백 기준 split
		).map(String::trim).filter(s -> !s.isBlank()).distinct().collect(Collectors.toList());
	}
}
