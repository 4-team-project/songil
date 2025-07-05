package com.takku.project.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
import com.takku.project.service.ImageService;
import com.takku.project.service.ProductService;

@Controller
@RequestMapping("/seller/product")
public class ProductManagementController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ImageService imageService;

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
	public String showForm() {
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

			if (productDTO.getStoreId() == null) {
				productDTO.setStoreId(1);
			}

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
	@DeleteMapping("/{productId}")
	public String deleteProduct(@PathVariable("productId") Integer productId, RedirectAttributes ra) {
		int result = productService.deleteProduct(productId);
		if (result > 0) {
			ra.addFlashAttribute("resultMessage", "상품이 삭제되었습니다.");
		} else {
			ra.addFlashAttribute("resultMessage", "상품 삭제에 실패하였습니다.");
		}
		return "redirect:/seller/product";
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
