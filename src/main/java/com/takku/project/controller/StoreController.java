package com.takku.project.controller;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.ProductService;
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
	
	@Autowired
	private ProductService productService;

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

		fundingDTO.setFundingName(funding.getFundingName()); // 펀딩명
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
		if (funding == null) { return "redirect:/seller/create-step1"; }
		
		model.addAttribute("fundingName", funding.getFundingName());
		model.addAttribute("startDate", funding.getStartDate());
		// Date로 저장돼 있다면 포맷 필요
	    return "pages/seller/funding_complete";
	}
	
	//기간 및 이미지
	@GetMapping("/create-step3")
	public String selectDateAndImage() {
		return "pages/seller/create_insertDetail";
	}
	
	// 상점 펀딩 현황 (초기 페이지 로드 시)
		@GetMapping("/list")
		public String findFundingByStoreId(@RequestParam(value = "userId") Integer userId, Model model) {
			// 실제 서비스에서는 @RequestParam으로 받은 userId를 사용하여 동적으로 처리하는 것이 좋습니다.
			// 현재 userId=3은 기본값 또는 테스트 용도.

			List<StoreDTO> userStore = storeService.selectStoreListByUserId(userId);

			StoreDTO currentStore = null;
			List<FundingDTO> funding = Collections.emptyList();

			// 4. (선택적) 사용자가 소유한 상점 중 첫 번째 상점을 '기본' 상점으로 설정
			if (userStore != null && !userStore.isEmpty()) {
				currentStore = userStore.get(0); // 첫 번째 상점을 선택

				// 5. 선택된 상점의 펀딩 리스트를 가져옴
				funding = fundingService.selectFudingListByStoreId(currentStore.getStoreId());
			} else {
				// 사용자가 상점을 하나도 소유하고 있지 않은 경우 처리
				// 예를 들어, 메시지를 추가하거나 상점 등록 페이지로 유도
				model.addAttribute("message", "등록된 상점이 없습니다. 새로운 상점을 등록해주세요.");
			}

			// 6. 모델에 현재 상점과 펀딩 리스트 추가
			model.addAttribute("userStores", userStore);
			model.addAttribute("store", currentStore); // 현재 상점 정보 (JSP의 ${store.storeName} 등에 사용)
			model.addAttribute("funding", funding); // 현재 상점의 펀딩 리스트

			return "/pages/seller/seller_funding_list";
		}

		// store 리스트 반환 (AJAX 호출용)
		@GetMapping("/stores/byUser")
		@ResponseBody // JSON 형태로 응답하도록 지정
		public List<StoreDTO> getStoresByUser(@RequestParam int userId) { // @Param 대신 @RequestParam 사용 권장
			return storeService.selectStoreListByUserId(userId);
		}

		// Store에 속한 펀딩 리스트 반환 (AJAX 호출용)
		@GetMapping("/fundings/byStore")
		@ResponseBody // JSON 형태로 응답하도록 지정
		public List<FundingDTO> getFundingsByStore(@RequestParam int storeId) {
			return fundingService.selectFudingListByStoreId(storeId);
		}
		
		//사용자 펀딩 현황
		@GetMapping("/stats")
		public String showFundingStats(@RequestParam("fundingId") Integer fundingId, Model model) {
			// fundingId를 사용하여 해당 펀딩의 상세 통계 데이터를 조회
			// FundingDTO funding = fundingService.selectFundingById(fundingId);
			// model.addAttribute("funding", funding);
			// ... 통계 데이터 추가 로직 ...
			FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
			 Date endDate = funding.getEndDate();
			 java.util.Date today = new java.util.Date();
			 if (funding.getEndDate() != null) {
		            // java.util.Date와 java.sql.Date는 getTime()으로 밀리초를 반환합니다.
		            // 날짜만 비교하기 위해 '오늘'의 자정 시간 (00:00:00)을 만듭니다.
		            // java.util.Date로 변환하여 시분초를 잘라내는 것이 좋습니다.
		            // (java.sql.Date도 시분초가 00:00:00으로 설정되는 경향이 있으나, 명시적으로 처리)
		            java.util.Date todayUtilDate = new java.util.Date(today.getYear(), today.getMonth(), today.getDate());
		            java.util.Date endDateUtilDate = new java.util.Date(funding.getEndDate().getYear(), funding.getEndDate().getMonth(), funding.getEndDate().getDate());


		            // 두 날짜 간의 밀리초 차이 계산
		            long diffInMillis = endDateUtilDate.getTime() - todayUtilDate.getTime();

		            // 밀리초를 일 단위로 변환
		            long remainingDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

		            if (remainingDays > 0) {
		                model.addAttribute("remainingDays", remainingDays);
		                model.addAttribute("fundingStatusMessage", "펀딩 종료까지 " + remainingDays + "일 남았습니다.");
		            } else if (remainingDays == 0) {
		                model.addAttribute("remainingDays", 0L);
		                model.addAttribute("fundingStatusMessage", "펀딩이 오늘 종료됩니다!");
		            } else { // remainingDays < 0 (종료일이 이미 지남)
		                model.addAttribute("remainingDays", remainingDays); // 음수 값 그대로 전달
		                model.addAttribute("fundingStatusMessage", "펀딩이 이미 종료되었습니다. (" + Math.abs(remainingDays) + "일 전)");
		            }
		        } else {
		            model.addAttribute("remainingDays", null);
		            model.addAttribute("fundingStatusMessage", "종료일 정보가 없습니다.");
		        }
			model.addAttribute("funding", funding);
			 model.addAttribute("today", today);
			return "/pages/seller/sellerFundingStats"; // sellerFundingStats.jsp로 이동
		}
		
		//사용자 펀딩 상세보기
		@GetMapping("/detail")
		public String showFundingDetail(@RequestParam("fundingId") Integer fundingId, Model model) {
			FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
			ProductDTO product = productService.selectByProductId(funding.getProductId());
			boolean isEditable = "준비중".equals(funding.getStatus());
	        model.addAttribute("isEditable", isEditable);
			model.addAttribute("funding", funding);
			model.addAttribute("product", product);
			return "/pages/seller/sellerFundingDetail";
		}


}
