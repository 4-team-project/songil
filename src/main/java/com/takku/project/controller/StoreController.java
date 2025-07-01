package com.takku.project.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.StoreService;
import com.takku.project.service.UserService;

@Controller
@RequestMapping("/seller")
public class StoreController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private FundingService fundingService;

	@GetMapping("/store/new")
	public String showStoreForm() {
		return "store_form";
	}

	// 상점 등록
	@PostMapping("/store")
	public String insertStore(StoreDTO storeDTO, RedirectAttributes ra) {
		int result = storeService.insertStore(storeDTO);
		if (result > 0) {
			ra.addFlashAttribute("resultMessage", "상점이 등록되었습니다.");
		} else {
			ra.addFlashAttribute("resultMessage", "상점 등록에 실패하였습니다.");
		}
		return "redirect:/seller/store/list";
	}

	// 상점 수정 폼
	@GetMapping("/store/{storeId}/edit")
	public String showEditForm(@PathVariable("storeId") Integer storeId, Model model) {
		StoreDTO store = storeService.selectStoreById(storeId);
		model.addAttribute("storeDTO", store);
		return "store_edit";
	}

	// 상점 수정 처리
	@PostMapping("/store/{storeId}/edit")
	public String updateStore(@PathVariable("storeId") Integer storeId, StoreDTO storeDTO, RedirectAttributes ra) {
		storeDTO.setStoreId(storeId);
		int result = storeService.updateStore(storeDTO);
		if (result > 0) {
			ra.addFlashAttribute("resultMessage", "상점이 수정되었습니다.");
		} else {
			ra.addFlashAttribute("resultMessage", "상점 수정에 실패했습니다.");
		}
		return "redirect:/seller/store/list?storeId=" + storeId;
	}

	// 상점 삭제 처리
	@PostMapping("/store/{storeId}/delete")
	public String deleteStore(@PathVariable("storeId") Integer storeId, RedirectAttributes ra) {
		int result = storeService.deleteStore(storeId);
		if (result > 0) {
			ra.addFlashAttribute("resultMessage", "상점이 삭제되었습니다.");
		} else {
			ra.addFlashAttribute("resultMessage", "상점 삭제에 실패했습니다.");
		}
		return "redirect:/seller/store/list";
	}

	// seller -> 한정 상품 펀딩 or 일반 펀딩
	@GetMapping("/create-step1")
	public String selectStoreNameByUserId(Model model, HttpSession session) {
		int userId = 3; // 임시 사용자 ID
		StoreDTO store = storeService.selectStoreNameByUserId(userId);
		model.addAttribute("store", store);
		
		session.setAttribute("userId", userId);
		session.setAttribute("store", store);
		return "pages/seller/createFunding";
	}

	// 상품 정보
	@GetMapping("/create-step2")
    public String createStep2(@RequestParam("type") String type, Model model, HttpSession session) {
		//상점이름
		StoreDTO store = (StoreDTO) session.getAttribute("store");
		model.addAttribute("store", store);
		
		session.setAttribute("fundingtype", type);
		
        if ("general".equals(type)) {
            return "pages/seller/create_normalFunding"; 
        } else if ("limited".equals(type)) {
            return "pages/seller/create_existMenu"; 
        } else {
            return "pages/seller/createFunding";
        }
    }

	public String selectFundingMenuType() {
		return "pages/seller/create_existMenu";
	}

	/*
	 * // 기존 메뉴 선택을 눌렀을 때 create_existMenu로
	 * 
	 * @GetMapping("/create_existMenu") public String showExistingMenuPage() {
	 * return "pages/seller/create_existMenu"; }
	 * 
	 * // 새로운 메뉴 등록을 눌렀을 때 create_newMenu로
	 * 
	 * @GetMapping("/create_newMenu") public String showNewMenuPage() { return
	 * "pages/seller/create_newMenu"; }
	 */
	
	//기간 및 이미지
	@GetMapping("/create-step3")
	public String selectDateAndImage(HttpSession session, Model model) {

		
		String type = (String) session.getAttribute("fundingType");
		StoreDTO store = (StoreDTO) session.getAttribute("store");
		
		model.addAttribute("store", store);
		model.addAttribute("type", type);
		
		return "pages/seller/create_insertDetail";
	}

	@PostMapping("/create-step3")
	public String insertFundingMenuDetail(HttpSession session, Model model) {
		//상점이름
		StoreDTO store = (StoreDTO) session.getAttribute("store");
		model.addAttribute("store", store);
		
		int userId = (int) session.getAttribute("userId");
		String type = (String) session.getAttribute("fundingType");
		
		model.addAttribute("userId", userId);
		model.addAttribute("type", type);
		
		return "pages/seller/create_insertDetail";
	}

	//ai, 직접입력 선택 창
	@GetMapping("/create-step4")
	public String writeType(HttpSession session, Model model) {
		StoreDTO store = (StoreDTO) session.getAttribute("store");
		model.addAttribute("store", store);
		return "pages/seller/select_writetype";
	}

	//선택 후 제목, 내용 입력 창
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
	public String handleFundingBasicInfo(
	        @ModelAttribute FundingDTO funding,
	        HttpSession session) {
	    // 세션에서 기존 fundingDTO 가져오기
	    FundingDTO sessionDTO = (FundingDTO) session.getAttribute("fundingDTO");

	    // 만약 세션에 없으면 새로 생성 (예외 처리 목적)
	    if (sessionDTO == null) {
	        sessionDTO = new FundingDTO();
	    }

	    // 입력된 값만 덮어쓰기
	    sessionDTO.setFundingName(funding.getFundingName());
	    sessionDTO.setFundingDesc(funding.getFundingDesc());

		/*
		 * fundingService.insertFunding(sessionDTO);
		 * 
		 */

	    return "redirect:/seller/complete";
	}
	
	@GetMapping("/complete")
	public String fundingComplete(HttpSession session, Model model) {
	    FundingDTO funding = (FundingDTO) session.getAttribute("fundingDTO");
	    
	    session.removeAttribute("fundingDTO");
	    // 예외 처리 (없을 경우 홈으로)
		/*
		 * if (funding == null) { return "redirect:/"; }
		 */

		/*
		 * model.addAttribute("fundingName", funding.getFundingName());
		 * model.addAttribute("startDate", funding.getStartDate());
		 */// Date로 저장돼 있다면 포맷 필요
	    return "pages/seller/funding_complete";
	}
	
	
}
