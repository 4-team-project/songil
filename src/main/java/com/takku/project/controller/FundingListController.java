package com.takku.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.takku.project.domain.FundingDTO;
import com.takku.project.service.FundingListService;

@Controller
@RequestMapping("fundings")
public class FundingListController {
	
	@Autowired
	private FundingListService fundingListService;

	@PostMapping
	public String selectFundingList(String sort, Model model) {
		 // sort 값이 null이면 기본값 설정
        if (sort == null || sort.isEmpty()) {
            sort = "latest"; 
        }

        List<FundingDTO> fundingList = fundingListService.selectFundingListBySort(sort);
        model.addAttribute("fundingList", fundingList);

        return "funding_list";
	}
	
	@GetMapping("/sort")
    @ResponseBody
    public List<FundingDTO> sortFundingList(@RequestParam(defaultValue = "recent") String sort) {
        return fundingListService.selectFundingListBySort(sort);
    }
}
