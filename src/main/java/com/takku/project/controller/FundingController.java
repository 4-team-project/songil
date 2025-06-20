package com.takku.project.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.ImageService;
import com.takku.project.service.ProductService;
import com.takku.project.service.StoreService;

@Controller
@RequestMapping("/fundings")
public class FundingController {

	@Autowired
	FundingService fundingService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	StoreService storeService;

	@GetMapping
	public String getFundings(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String sido,
			@RequestParam(required = false) String sigungu, HttpSession session, Model model) {
		List<FundingDTO> fundinglist;

		if (keyword != null || categoryId != null || sido != null || sigungu != null) {
			fundinglist = fundingService.selectFundingByCondition(keyword, categoryId, sido, sigungu);
		} else {
			fundinglist = fundingService.selectAllFunding();
		}
		model.addAttribute("fundinglist", fundinglist);
		return "pages/user/home";
	}

	@GetMapping("/{fundingId}")
	public String getFundingDetail(@PathVariable("fundingId") int fundingId, Model model) {
		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		if (funding == null) {
			return "common/error";
		}

		ProductDTO product = productService.selectByProductId(funding.getProductId());
		List<ImageDTO> productImages = imageService.selectImagesByProductId(funding.getProductId());
		StoreDTO store = storeService.selectStoreById(funding.getStoreId());
		
		model.addAttribute("funding", funding); 
		model.addAttribute("store", store); 
		model.addAttribute("product", product); 
		model.addAttribute("productImages", productImages);
		return "pages/user/funding_detail";
	}
}
