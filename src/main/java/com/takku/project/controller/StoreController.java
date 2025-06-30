package com.takku.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.StoreService;
import com.takku.project.service.UserService;

@Controller
@RequestMapping("/seller/store")
public class StoreController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private UserService userService;

	@GetMapping("/new")
	public String showStoreForm() {
		return "store_form";
	}

	// 상점 등록
	@PostMapping
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
	@GetMapping("/{storeId}/edit")
	public String showEditForm(@PathVariable("storeId") Integer storeId, Model model) {
		StoreDTO store = storeService.selectStoreById(storeId);
		model.addAttribute("storeDTO", store);
		return "store_edit";
	}

	// 상점 수정 처리
	@PostMapping("/{storeId}/edit")
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
	@PostMapping("/{storeId}/delete")
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
	public String selectStoreNameByUserId(Model model) {
		int userId = 3; // 임시 사용자 ID
		StoreDTO store = storeService.selectStoreNameByUserId(userId);
		model.addAttribute("store", store);
		return "pages/seller/createFunding";
	}

	// 상품 정보
	@GetMapping("/create-step2")
    public String createStep2(@RequestParam("type") String type, Model model) {
        if ("general".equals(type)) {
            // 일반 펀딩 선택 시
            return "pages/seller/create_normalFunding"; 
        } else if ("limited".equals(type)) {
            // 한정 상품 펀딩 선택 시 (필요하면)
            return "pages/seller/create_existMenu"; 
        } else {
            // 기본 페이지 혹은 에러 처리
            return "pages/seller/createFunding"; // 선택 페이지로 다시 보내거나
        }
    }
	
	//기간 및 이미지
	@GetMapping("/create-step3")
	public String selectDateAndImage() {
		return "pages/seller/create_insertDetail";
	}
		
	
	
}
