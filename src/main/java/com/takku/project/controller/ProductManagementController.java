package com.takku.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.takku.project.domain.ProductDTO;
import com.takku.project.service.ProductService;

@Controller
@RequestMapping("/seller/product")
public class ProductManagementController {
	
	@Autowired
	private ProductService productService;

	@GetMapping
	public String productList(Integer storeId, Model model) {
	List<ProductDTO> list = productService.selectProductByStoreId(storeId);
		model.addAttribute("productList", list);
		return "seller_product";
	}
	
	//상품 등록 폼
	@GetMapping("/new")
	public String showForm() {
		return "seller_product_add";
	}
	
	//삼품 등록 처리
	@PostMapping
	public String InsertForm(ProductDTO productDTO, RedirectAttributes ra) {
		int success = productService.insertProduct(productDTO);
		if(success > 0) {
			ra.addFlashAttribute("resultMessage", "등록을 성공하였습니다.");
		}else {
			ra.addFlashAttribute("resultMessage", "등록에 실패하였습니다.");
		}
		return "redirect:/seller/product";
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
	
}
