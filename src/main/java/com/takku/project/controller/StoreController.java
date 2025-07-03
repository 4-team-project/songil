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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.FundingDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.FundingService;
import com.takku.project.service.ProductService;
import com.takku.project.service.StoreService;
import com.takku.project.service.StoreStatsService;
import com.takku.project.service.UserService;

@Controller
@RequestMapping("/seller/store")
public class StoreController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private UserService userService;

	@Autowired
	private FundingService fundingService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private StoreStatsService storeStatsService;

	@GetMapping()
	public String homePage() {
		return "seller.storeManagement";
	}

	@GetMapping("/new")
	public String showStoreForm() {
		return "store_form";
	}

	// 상점 등록
	@PostMapping()
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

	// 펀딩 상세 정보 세션
	@ModelAttribute("tempFunding")
	public FundingDTO createTempFunding() {
		return new FundingDTO();
	}

	// 유저 id (로그인 세션으로 받을 듯?)
	@ModelAttribute("currentProcessingUserId")
	public Integer createCurrentProcessingUserId(HttpSession session) {
		// 새 펀딩 생성 시작 시, 로그인된 사용자의 userId를 가져와 초기 설정합니다.
		// 수정 시작 시에는 startFundingEdit에서 해당 펀딩의 userId로 덮어씌울 것입니다.
		return (Integer) session.getAttribute("loggedInUserId"); // 로그인 세션에서 ID 가져오기
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

		UserDTO user = userService.selectByUserId(userId);

		// 6. 모델에 현재 상점과 펀딩 리스트 추가
		model.addAttribute("userStores", userStore);
		model.addAttribute("store", currentStore); // 현재 상점 정보 (JSP의 ${store.storeName} 등에 사용)
		model.addAttribute("funding", funding); // 현재 상점의 펀딩 리스트
		model.addAttribute("userId", userId);
		model.addAttribute("user", user);
		return "seller/list";
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

// 사용자 펀딩 현황
	@GetMapping("/stats")
	public String showFundingStats(@RequestParam("fundingId") Integer fundingId, Model model) {
		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		StoreDTO ownerStore = storeService.selectStoreById(funding.getStoreId());
		Integer fundingUserId = ownerStore.getUserId();

		Date endDate = funding.getEndDate();
		java.util.Date today = new java.util.Date();
		if (funding.getEndDate() != null) {
			// java.util.Date와 java.sql.Date는 getTime()으로 밀리초를 반환합니다.
			// 날짜만 비교하기 위해 '오늘'의 자정 시간 (00:00:00)을 만듭니다.
			// java.util.Date로 변환하여 시분초를 잘라내는 것이 좋습니다.
			// (java.sql.Date도 시분초가 00:00:00으로 설정되는 경향이 있으나, 명시적으로 처리)
			java.util.Date todayUtilDate = new java.util.Date(today.getYear(), today.getMonth(), today.getDate());
			java.util.Date endDateUtilDate = new java.util.Date(funding.getEndDate().getYear(),
					funding.getEndDate().getMonth(), funding.getEndDate().getDate());

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

		int todayFundingAmount = storeStatsService.getTodayFundingAmount(fundingId);
		int completeOrders = storeStatsService.getFundingCompleteOrderCount(fundingId);
		int refundOrders = storeStatsService.getFundingRefundOrderCount(fundingId);

		model.addAttribute("userId", fundingUserId);
		model.addAttribute("fundingId", fundingId);
		model.addAttribute("todayFundingAmount", todayFundingAmount);
		model.addAttribute("completeOrders", completeOrders);
		model.addAttribute("refundOrders", refundOrders);
		model.addAttribute("fundingGenderStats", storeStatsService.getFundingGenderRatio(fundingId));
		model.addAttribute("fundingAgeStats", storeStatsService.getFundingAgeDistribution(fundingId));

		model.addAttribute("funding", funding);
		model.addAttribute("today", today);

		return "seller/stats"; // sellerFundingStats.jsp로 이동
	}

// 사용자 펀딩 상세보기
	@GetMapping("/detail")
	public String showFundingDetail(@RequestParam("fundingId") Integer fundingId, Model model) {
		FundingDTO funding = fundingService.selectFundingByFundingId(fundingId);
		ProductDTO product = productService.selectByProductId(funding.getProductId());
		StoreDTO ownerStore = storeService.selectStoreById(funding.getStoreId());
		Integer fundingUserId = ownerStore.getUserId();
		boolean isEditable = "준비중".equals(funding.getStatus());

		model.addAttribute("isEditable", isEditable);
		model.addAttribute("funding", funding);
		model.addAttribute("product", product);
		model.addAttribute("userId", fundingUserId);

		return "seller/detail";
	}

// 펀딩 수정 시작
	@GetMapping("/funding/edit/{fundingId}")
	public String startFundingEdit(@PathVariable Integer fundingId, Model model) {
		FundingDTO fundingToEdit = fundingService.selectFundingByFundingId(fundingId);
		// DB에서 가져온 펀딩 객체를 "tempFunding" 이라는 이름으로 모델에 추가
		// @SessionAttributes("tempFunding") 설정 덕분에 이 객체가 세션에 저장됩니다.
		model.addAttribute("tempFunding", fundingToEdit);

		StoreDTO ownerStore = storeService.selectStoreById(fundingToEdit.getStoreId());
		Integer fundingUserId = ownerStore.getUserId();
		model.addAttribute("currentProcessingUserId", fundingUserId);
		System.out.println(fundingToEdit);

		// 첫 번째 수정 폼 페이지로 리다이렉트
		return "forward:/seller/edit/step1";
	}

// 1단계 폼 페이지 요청
	@GetMapping("/edit/step1")
	public String showCreateFundingStep1(@ModelAttribute("tempFunding") FundingDTO tempFunding,
			@ModelAttribute("currentProcessingUserId") Integer userId, Model model, HttpSession session) {
		// 이미 세션에 데이터가 있다면 불러와서 폼에 채워줄 수 있습니다.
		// @SessionAttributes("tempFunding") 설정을 사용하면, Model에 tempFunding이 있으면 자동 로드됨
		System.out.println("showCreateFundingStep1: userId= " + userId);

		System.out.println("--- showCreateFundingStep1 메서드 진입 ---");
		System.out.println("1. @ModelAttribute userId = " + userId); // 이전에 출력된 로그

		// ⭐ 세션에서 직접 "currentProcessingUserId" 값 확인
		Object sessionUserIdRaw = session.getAttribute("currentProcessingUserId");
		if (sessionUserIdRaw != null) {
			System.out.println("2. session.getAttribute(\"currentProcessingUserId\") 값: " + sessionUserIdRaw);
			System.out.println("3. session.getAttribute(\"currentProcessingUserId\") 타입: "
					+ sessionUserIdRaw.getClass().getName());
		} else {
			System.out.println("2. session.getAttribute(\"currentProcessingUserId\") 없음 또는 null");
		}

		// ⭐ tempFunding 객체 내부 값도 상세히 확인
		if (tempFunding != null) {
			System.out.println("4. tempFunding.getFundingId() = " + tempFunding.getFundingId());
			System.out.println("5. tempFunding.getFundingName() = " + tempFunding.getFundingName());
			// 필요한 경우 tempFunding의 다른 Integer 타입 필드도 모두 출력
			// 예: System.out.println("7. tempFunding.getProductId() = " +
			// tempFunding.getProductId());
		} else {
			System.out.println("4. @ModelAttribute tempFunding이 null입니다.");
		}
		System.out.println("--- showCreateFundingStep1 메서드 종료 전 ---");
		return "pages/seller/fundingFormStep1";
	}

// 1단계 데이터 제출 및 세션 저장
	@PostMapping("/edit/step1")
	public String processCreateFundingStep1(@ModelAttribute("tempFunding") FundingDTO funding,
			@RequestParam(value = "menuPhotoFile", required = false) MultipartFile menuPhotoFile, // 파일 처리
			HttpSession session, // 세션 직접 접근하여 파일 URL 저장
			Model model, @ModelAttribute("currentProcessingUserId") Integer userId) {
		// 실제로는 파일 업로드 로직을 여기에 추가하여 메뉴 사진을 저장하고 URL을 funding 객체에 설정해야 합니다.
		// 예시: 파일 저장 후 URL을 세션에 저장된 tempFunding 객체에 추가
		if (menuPhotoFile != null && !menuPhotoFile.isEmpty()) {
			try {
				String fileUrl = "path/to/uploaded/images/" + menuPhotoFile.getOriginalFilename();
				// 실제 파일 저장 로직 (서비스 계층으로 분리 권장)
				// funding.setMenuPhotoUrl(fileUrl); // 세션 객체에 URL 저장
			} catch (Exception e) {
				// 파일 업로드 실패 처리
			}
		}

		// @SessionAttributes("tempFunding") 덕분에 funding 객체는 자동으로 세션에 업데이트됨
		return "redirect:/seller/edit/step2"; // 2단계 페이지로 리다이렉트
	}

// 2단계 폼 페이지 요청
	@GetMapping("/edit/step2")
	public String showCreateFundingStep2(@ModelAttribute("tempFunding") FundingDTO funding, Model model) {
		// 1단계에서 저장된 funding 객체가 Model에 자동으로 주입됩니다.
		// 여기서 isEditable 등 추가적인 모델 속성을 필요에 따라 설정할 수 있습니다.
		return "pages/seller/fundingFormStep2";
	}

// 2단계 데이터 제출 및 최종 저장
	@PostMapping("/edit/step2")
	public String processCreateFundingStep2(@ModelAttribute("tempFunding") FundingDTO funding,
			@ModelAttribute("currentProcessingUserId") Integer userId, SessionStatus sessionStatus) { // 세션 완료 처리
		// 1단계와 2단계의 모든 데이터가 합쳐진 funding 객체가 여기로 넘어옵니다.
		// 이 funding 객체를 DB에 최종 저장하는 로직을 구현합니다.
		fundingService.updateFunding(funding);

		sessionStatus.setComplete(); // 세션에 저장된 "tempFunding" 객체를 비웁니다.
		return "redirect:/seller/list?userId=" + userId; // 최종 펀딩 목록으로 리다이렉트
	}

}
