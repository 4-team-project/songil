package com.takku.project.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ImageDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.StoreDTO;
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

	// 펀딩 등록 처리(참고용임 지워야함)
	@PostMapping
	public String registerFunding(@ModelAttribute FundingDTO fundingDTO, @ModelAttribute("loginUser") UserDTO loginUser,
			@RequestParam("images") List<MultipartFile> images, HttpSession session,
			RedirectAttributes redirectAttributes) {

		// 1. 로그인 유저로부터 storeId 조회
		Integer storeId = storeService.findStoreIdByUserId(loginUser.getUserId());
		fundingDTO.setStoreId(storeId);

		// 2. 기본값 설정 (status: 준비중, currentQty: 0)
		fundingDTO.setStatus("준비중");
		fundingDTO.setCurrentQty(0);

		// 3. 펀딩 등록
		int result = fundingService.insertFunding(fundingDTO); // keyProperty로 fundingId 생성

		// 4. 파일 업로드 처리
		String uploadPath = session.getServletContext().getRealPath("/resources/images");

		if (result > 0) {
			List<ImageDTO> imageDTOList = new ArrayList<>();

			for (MultipartFile file : images) {
				if (!file.isEmpty()) {
					try {
						// 고유 파일 이름 생성
						String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
						String fullPath = uploadPath + File.separator + fileName;

						// 서버에 저장
						file.transferTo(new File(fullPath));

						// 이미지 DTO 생성
						ImageDTO imageDTO = new ImageDTO();
						imageDTO.setFundingId(fundingDTO.getFundingId()); // FK 연결
						imageDTO.setImageUrl("/resources/images/" + fileName);

						// DB 저장
						imageService.insertImageUrl(imageDTO);

						imageDTOList.add(imageDTO);

					} catch (IOException e) {
						e.printStackTrace();
						redirectAttributes.addFlashAttribute("resultMessage", "펀딩은 등록됐지만 이미지 업로드 중 오류 발생");
						return "redirect:/seller/fundings";
					}
				}
			}

			// 5. DTO에 이미지 목록 추가 (필요시 뷰에서 활용)
			fundingDTO.setImages(imageDTOList);
			redirectAttributes.addFlashAttribute("resultMessage", "펀딩 등록 성공!");
		} else {
			redirectAttributes.addFlashAttribute("resultMessage", "펀딩 등록 실패");
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

	// 펀딩 만들기 -> 한정 상품 펀딩 or 일반 펀딩
	@GetMapping("/create-step1")
	public String selectStoreNameByUserId(Model model, HttpSession session) {
		int userId = 3; // 임시 사용자 ID

		StoreDTO store = storeService.selectStoreNameByUserId(userId);
		model.addAttribute("store", store);

		FundingDTO fundingDTO = new FundingDTO();

		session.setAttribute("fundingDTO", fundingDTO);
		session.setAttribute("store", store);
		return "seller.createFunding";
	}

	// 상품 정보
	@GetMapping("/create-step2")
	public String createStep2(@RequestParam("type") String type, Model model, HttpSession session) {
		// 상점이름
		StoreDTO store = (StoreDTO) session.getAttribute("store");
		model.addAttribute("store", store);

		if ("general".equals(type)) {
			return "pages/seller/create_normalFunding";
		} else if ("limited".equals(type)) {
			return "pages/seller/create_existMenu";
		} else {
			return "seller.createFunding";
		}
	}

	// 펀딩이름 판매가 최소 판매개수 최대판대매수 인당구매
	@PostMapping("/create-step3")
	public String insertFundingMenuDetail(@ModelAttribute FundingDTO funding, HttpSession session, Model model) {

		FundingDTO fundingDTO = (FundingDTO) session.getAttribute("fundingDTO");

		StoreDTO store = (StoreDTO) session.getAttribute("store");
		model.addAttribute("store", store);

		fundingDTO.setProductId(funding.getProductId()); // 상품ID
		fundingDTO.setSalePrice(funding.getSalePrice()); // 판매가
		fundingDTO.setTargetQty(funding.getTargetQty()); // 최소 판매 개수
		fundingDTO.setMaxQty(funding.getMaxQty()); // 최대 판매 개수
		fundingDTO.setPerQty(funding.getPerQty()); // 인당 구매 가능 개수

		session.setAttribute("fundingDTO", fundingDTO);

		return "pages/seller/create_insertDetail";
	}

	// ai, 직접입력 선택 창
	@PostMapping("/create-step4")
	public String writeType(@ModelAttribute FundingDTO funding, HttpSession session, Model model) {
		StoreDTO store = (StoreDTO) session.getAttribute("store");

		FundingDTO fundingDTO = (FundingDTO) session.getAttribute("fundingDTO");

		fundingDTO.setStartDate(funding.getStartDate()); // 시작일
		fundingDTO.setEndDate(funding.getEndDate()); // 마감일

		session.setAttribute("fundingDTO", fundingDTO);
		model.addAttribute("store", store);
		return "pages/seller/select_writetype";
	}

	// 선택 후 제목, 내용 입력 창
	@PostMapping("/create-step5")
	public String handleWriteType(@RequestParam("type") String type, HttpSession session, Model model) {

		StoreDTO store = (StoreDTO) session.getAttribute("store");
		model.addAttribute("store", store);

		if ("directly".equals(type)) {
			return "pages/seller/funding_direct_input";
		} else if ("ai".equals(type)) {
			return "pages/seller/funding_ai_input";
		}

		// 잘못된 type 처리
		return "redirect:/error";
	}

	@PostMapping("/submit-funding")
	public String handleFundingBasicInfo(@ModelAttribute FundingDTO funding, HttpSession session) {
		// 세션에서 기존 fundingDTO 가져오기
		FundingDTO sessionDTO = (FundingDTO) session.getAttribute("fundingDTO");

		// 만약 세션에 없으면 새로 생성 (예외 처리 목적)
		if (sessionDTO == null) {
			sessionDTO = new FundingDTO();
		}

		// 입력된 값만 덮어쓰기
		sessionDTO.setFundingName(funding.getFundingName());
		sessionDTO.setFundingDesc(funding.getFundingDesc());

		fundingService.insertFunding(sessionDTO);

		return "redirect:/seller/complete";
	}

	@GetMapping("/complete")
	public String fundingComplete(HttpSession session, Model model) {
		FundingDTO funding = (FundingDTO) session.getAttribute("fundingDTO");

		session.removeAttribute("fundingDTO");

		// 예외 처리 (없을 경우 홈으로)
		if (funding == null) {
			return "redirect:/seller/create-step1";
		}

		model.addAttribute("fundingName", funding.getFundingName());
		model.addAttribute("startDate", funding.getStartDate());
		// Date로 저장돼 있다면 포맷 필요
		return "pages/seller/funding_complete";
	}

	// 기간 및 이미지
	@GetMapping("/create-step3")
	public String selectDateAndImage() {
		return "pages/seller/create_insertDetail";
	}
}
