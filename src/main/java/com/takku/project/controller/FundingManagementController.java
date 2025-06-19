package com.takku.project.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import java.util.List;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.ImageService;
import com.takku.project.service.ProductService;
import com.takku.project.service.StoreService;

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

	// 펀딩 입력 폼
	@GetMapping("/new")
	public String insertForm(@ModelAttribute("loginUser") UserDTO loginUser, Model model) {
		Integer storeId = storeService.findStoreIdByUserId(loginUser.getUserId());

		List<ProductDTO> productList = productService.selectProductByStoreId(storeId);
		model.addAttribute("productList", productList);
		return "seller_funding_add";
	}

	// 펀딩 등록 처리
	@PostMapping
	public String registerFunding(@ModelAttribute FundingDTO fundingDTO, @ModelAttribute("loginUser") UserDTO loginUser,
			@RequestParam("images") List<MultipartFile> images, RedirectAttributes redirectAttributes) {

		Integer storeId = storeService.findStoreIdByUserId(loginUser.getUserId());
		fundingDTO.setStoreId(storeId);

		int result = fundingService.insertFunding(fundingDTO);
		
		String uploadDir = "C:\\sinhan5\\install\\springFramework\\workSpace\\songil\\src\\main\\webapp\\resources\\images";
		
		if (result > 0) {
			for (MultipartFile file : images) {
				if(!file.isEmpty()) {
					try {
	                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	                    String fullPath = uploadDir + File.separator + fileName;
	                    file.transferTo(new File(fullPath));

	                    ImageDTO imageDTO = ImageDTO.builder()
	                        .fundingId(fundingDTO.getFundingId())
	                        .imageUrl("/resources/images/" + fileName) // 웹 기준 경로
	                        .build();

	                    imageService.insertImageUrl(imageDTO); // insert into image ...
	                } catch (IOException e) {
	                    e.printStackTrace(); // 로그 출력
	                    redirectAttributes.addFlashAttribute("resultMessage", "펀딩은 등록됐지만 이미지 업로드 중 오류 발생");
	                    return "redirect:/seller/fundings";
	                }
				}
			}
		}
		return "redirect:/seller/fundings";
	}

	// 펀딩 상세 조회
	@GetMapping("/{fundingId}")
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
}
