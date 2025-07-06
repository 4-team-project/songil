package com.takku.project.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.takku.project.domain.ImageDTO;
import com.takku.project.domain.ProductDTO;
import com.takku.project.domain.StoreDTO;
import com.takku.project.domain.UserDTO;
import com.takku.project.service.ImageService;
import com.takku.project.service.ProductService;
import com.takku.project.service.StoreService;

@Controller
@RequestMapping("/seller/product")
public class ProductManagementController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private StoreService storeService;

	@Value("${file.upload.path}")
	private String uploadDir;

	@GetMapping
	public String productList(Integer storeId, Model model) {
		List<ProductDTO> list = productService.selectProductByStoreId(storeId);
		model.addAttribute("productList", list);
		return "seller_product";
	}

	// 상품 등록 폼
	@GetMapping("/new")
	public String showForm(@RequestParam("storeId") int storeId, Model model) {
	    model.addAttribute("storeId", storeId);
	    return "seller.product"; 
	}

	private String getFileExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
			return "";
		}
		return filename.substring(filename.lastIndexOf("."));
	}

	// 상품 등록 & 이미지 저장
	@PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<String> insertProductWithImages(@RequestParam("product") String productJson,
			@RequestPart(value = "images", required = false) MultipartFile[] files) {

		try {
			ObjectMapper mapper = new ObjectMapper();
			ProductDTO productDTO = mapper.readValue(productJson, ProductDTO.class);

			// 상품 등록
			int result = productService.insertProduct(productDTO);

			// 이미지 저장
			if (result > 0) {
				imageService.storeImages(files, productDTO.getProductId(), null, null);
			}

			return ResponseEntity.ok("상품 등록 성공");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
		}
	}

	// 상품 수정 폼
	@GetMapping("/edit/{productId}")
	public String showEditForm(@PathVariable("productId") Integer productId,
			@RequestParam(value = "redirect", required = false) String redirect, Model model) {
		ProductDTO productDTO = productService.selectByProductId(productId);
		model.addAttribute("productDTO", productDTO);

		if (redirect != null) {
			model.addAttribute("redirectUrl", redirect);
		}

		return "seller.product"; 
	}

	// 상품 수정 처리
	@PostMapping(value = "/update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public String updateProductWithImages(@PathVariable("productId") Integer productId,
			@RequestParam("product") String productJson,
			@RequestPart(value = "images", required = false) MultipartFile[] files) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ProductDTO productDTO = mapper.readValue(productJson, ProductDTO.class);
			productDTO.setProductId(productId);

			int result = productService.updateProduct(productDTO);

			List<ImageDTO> existingImages = imageService.selectImagesByProductId(productId);
			List<String> existingUrls = existingImages.stream().map(ImageDTO::getImageUrl).collect(Collectors.toList());

			List<String> newUrls = productDTO.getImages() != null
					? productDTO.getImages().stream().map(ImageDTO::getImageUrl).collect(Collectors.toList())
					: new ArrayList<>();

			for (String oldUrl : existingUrls) {
				if (!newUrls.contains(oldUrl)) {
					imageService.deleteImageUrl(oldUrl);
				}
			}

			if (files != null) {
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						String ext = getFileExtension(file.getOriginalFilename());
						String fileName = UUID.randomUUID().toString() + ext;

						File uploadPath = new File(uploadDir);
						if (!uploadPath.exists())
							uploadPath.mkdirs();

						File dest = new File(uploadDir + File.separator + fileName);
						file.transferTo(dest);

						ImageDTO image = ImageDTO.builder().productId(productId).imageUrl("/image/" + fileName).build();

						imageService.insertImageUrl(image);
					}
				}
			}

			for (String newUrl : newUrls) {
				if (newUrl == null)
					continue;
				if (!existingUrls.contains(newUrl)) {
					String correctedUrl = newUrl.startsWith("/image/") ? newUrl : "/image/" + newUrl;

					ImageDTO image = ImageDTO.builder().productId(productId).imageUrl(correctedUrl).build();

					imageService.insertImageUrl(image);
				}
			}

			return result > 0 ? "상품이 수정되었습니다." : "상품 수정에 실패하였습니다.";

		} catch (Exception e) {
			e.printStackTrace();
			return "오류 발생: " + e.getMessage();
		}
	}

	// 상품 삭제
	@PostMapping("/delete/{productId}")
	@ResponseBody
	public String deleteProduct(@PathVariable("productId") Integer productId) {
	    if (productId == null) return "삭제 실패 (ID 없음)";

	    try {

	        List<ImageDTO> imageList = imageService.selectImagesByProductId(productId);

	        for (ImageDTO image : imageList) {
	            String fileName = image.getImageUrl().replace("/image/", "");
	            File file = new File(uploadDir + File.separator + fileName);
	            if (file.exists()) {
	                file.delete(); 
	            }

	            imageService.deleteImageUrl(image.getImageUrl());
	        }

	        int result = productService.deleteProduct(productId);

	        return result > 0 ? "삭제 성공" : "삭제 실패 (DB 처리 실패)";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "삭제 실패 (서버 오류)";
	    }
	}

	
	//상품 목록 보기 
	@GetMapping("/productList")
	public String showProductList(@RequestParam("storeId") int storeId, HttpSession session, Model model) {
	    UserDTO userDTO = (UserDTO) session.getAttribute("loginUser");

	    StoreDTO storeDTO = storeService.selectStoreById(storeId);
	    
	    List<ProductDTO> productDTO = productService.selectProductByStoreId(storeId);

	    model.addAttribute("userDTO", userDTO);
	    model.addAttribute("storeDTO", storeDTO);
	    model.addAttribute("productDTO", productDTO);

	    return "seller.productList";
	}
	
	@GetMapping("/list/byStoreId")
	@ResponseBody
	public Map<String, Object> getProductsByStoreId(
		@RequestParam("storeId") int storeId,
		@RequestParam(defaultValue = "1") int page) {

		int pageSize = 5;

		List<ProductDTO> allProducts = productService.selectProductByStoreId(storeId);

		for (ProductDTO product : allProducts) {
			List<ImageDTO> images = product.getImages();
			if (images != null && !images.isEmpty()) {
				product.setThumbnailImageUrl(images.get(0).getImageUrl()); // 첫 번째 이미지를 썸네일로
			}
		}

		int total = allProducts.size();
		int totalPages = (int) Math.ceil((double) total / pageSize);

		int fromIndex = (page - 1) * pageSize;
		int toIndex = Math.min(fromIndex + pageSize, total);
		List<ProductDTO> pagedProducts = allProducts.subList(fromIndex, toIndex);

		Map<String, Object> result = new HashMap<>();
		result.put("productList", pagedProducts);
		result.put("currentPage", page);
		result.put("totalPages", totalPages);
		result.put("storeId", storeId);

		return result;
	}



	// 상품가져오기
	@GetMapping(value = "/list", produces = "application/json")
	@ResponseBody
	public List<ProductDTO> getProductListJson(@RequestParam int storeId) {
		return productService.selectProductByStoreId(storeId);
	}

	@GetMapping(value = "/info", produces = "application/json")
	@ResponseBody
	public ProductDTO getProductInfo(@RequestParam int productId) {
		return productService.selectByProductId(productId);
	}

	// productId로 상품 정보 조회
	@GetMapping(value = "/info/{productId}", produces = "application/json")
	@ResponseBody
	public ProductDTO getProductInfoByProductId(@PathVariable int productId, HttpServletRequest request) {
		ProductDTO product = productService.selectByProductId(productId);
		List<ImageDTO> imageList = imageService.selectImagesByProductId(productId);

		String cpath = request.getContextPath();

		for (ImageDTO image : imageList) {
			image.setImageUrl(cpath + image.getImageUrl());
		}

		product.setImages(imageList);
		return product;
	}

}
