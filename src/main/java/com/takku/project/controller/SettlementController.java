package com.takku.project.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.SettlementDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.SettlementService;

@Controller
@RequestMapping("/seller/settlements")
public class SettlementController {
	
	@Autowired
	private SettlementService settlementService;
	
	@Autowired
	private FundingService fundingService;

	@GetMapping()
	public String getSettlement(HttpSession session, Model model, Integer storeId) {
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
	    //if (loginUser == null) return "redirect:/auth/login";

	    StoreDTO store = (StoreDTO) session.getAttribute("store"); // 로그인 시 선택한 상점 정보
	    //if (store == null) return "redirect:/seller/store/select"; // 상점 선택 안 된 경우
	    
	    List<SettlementDTO> settlements = settlementService.selectSettlementByStoreId(2);
	    
	    for (SettlementDTO settlement : settlements) {
	        FundingDTO funding = fundingService.selectFundingByFundingId(settlement.getFundingId());
	        settlement.setFunding(funding); // ← 정산에 펀딩정보 주입
	    }
	    
	    model.addAttribute("settlementlist", settlements);
		return "seller.settlement";
	}
}
