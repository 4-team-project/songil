package com.takku.project.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseBody;
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

	@GetMapping
	public String productList(Integer storeId, Model model) {
		List<ProductDTO> list = productService.selectProductByStoreId(storeId);
		model.addAttribute("productList", list);
		return "seller_product";
	}
	
	//상품 등록 폼
	@GetMapping("/new")
	public String showForm() {
		return "seller.product";
	}
	
	//상품 등록 처리
	@PostMapping(value = "/insert", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<String> insertProductJson(@RequestBody ProductDTO productDTO) {
	    try {
	        if (productDTO.getStoreId() == null) {
	            productDTO.setStoreId(1); // 임시 Store ID
	        }
	    	ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	        System.out.println("전달된 JSON:\n" + mapper.writeValueAsString(productDTO));
	     
	        int result = productService.insertProduct(productDTO);
	        if (result > 0 && productDTO.getImages() != null) {
	        	for (ImageDTO imageDTO : productDTO.getImages()) {
	        	    String url = imageDTO.getImageUrl(); 
	        	    String ext = url.substring(url.lastIndexOf("."));
	        	    String timestamp = String.valueOf(System.currentTimeMillis());
	        	    String newFilename = timestamp + ext;

	        	    ImageDTO image = ImageDTO.builder()
	        	        .productId(productDTO.getProductId())
	        	        .imageUrl(newFilename)
	        	        .build();

	        	    imageService.insertImageUrl(image);
	        	}
			}
	        return ResponseEntity.ok("상품 등록 성공");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("오류 발생: " + e.getMessage());
	    }
	}

	//상품 수정 폼
	@GetMapping("/{productId}/edit")
	public String showEditForm(@PathVariable("productId") Integer productId, Model model) {
		ProductDTO productDTO = productService.selectByProductId(productId);
		model.addAttribute("productDTO", productDTO);
		return "seller_product_edit";
	}
	
	//상품 수정 처리
	@PutMapping("/{productId}")
	public String updateProduct(@PathVariable ("productId") Integer productId, ProductDTO productDTO, RedirectAttributes ra) {
		productDTO.setProductId(productId);
		
		int result = productService.updateProduct(productDTO);
		if(result > 0) {
			ra.addFlashAttribute("resultMessage", "상품이 수정되었습니다.");
		}else {
			ra.addFlashAttribute("resultMessage", "상품 수정에 실패하였습니다.");
		}
		return "redirect:/seller/product";
	}
	
	//상품 삭제
	@DeleteMapping("/{productId}")
	public String deleteProduct(@PathVariable ("productId") Integer productId, RedirectAttributes ra) {
		int result = productService.deleteProduct(productId);
		if(result > 0) {
			ra.addFlashAttribute("resultMessage", "상품이 삭제되었습니다.");
		}else {
			ra.addFlashAttribute("resultMessage", "상품 삭제에 실패하였습니다.");
		}
		return "redirect:/seller/product";
	}
	
	//상품가져오기
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
}
