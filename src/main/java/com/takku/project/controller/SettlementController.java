package com.takku.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.takku.project.domain.SettlementDTO;
import com.takku.project.service.SettlementService;

@Controller
@RequestMapping("/seller")
public class SettlementController {
	
	@Autowired
	private SettlementService settlementService;

	@GetMapping("/settlement")
	public String getSettlement(Model model, Integer storeId) {
		List<SettlementDTO> settlementList = settlementService.getSettlementByStoreId(storeId);
		model.addAttribute("settlementList", settlementList);
		return "seller_settlement";
	}
}
