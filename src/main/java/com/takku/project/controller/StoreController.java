package com.takku.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.StoreDTO;
import com.takku.project.service.StoreService;

@Controller
@RequestMapping("/seller/store")
public class StoreController {
	
	@Autowired
	private StoreService storeService;

	@GetMapping("/new")
	public String showStoreForm() {
		return "store_form";
	}
	
	//상점 등록
	@PostMapping
	public String insertStore(StoreDTO storeDTO, RedirectAttributes ra) {
		int result = storeService.insertStore(storeDTO);
		if(result > 0) {
			ra.addFlashAttribute("resultMessage", "상점이 등록되었습니다.");
		}else {
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
}
