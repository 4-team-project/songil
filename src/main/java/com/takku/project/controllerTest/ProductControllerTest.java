package com.takku.project.controllerTest;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.takku.project.controller.ProductManagementController;
import com.takku.project.domain.ProductDTO;
import com.takku.project.service.ProductService;

public class ProductControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private ProductService productService;
	
	@InjectMocks
	private ProductManagementController productController;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		InternalResourceViewResolver rr = new InternalResourceViewResolver();
		rr.setPrefix("/WEB-INF/views/");
		rr.setSuffix(".jsp");
		
		mockMvc = MockMvcBuilders
				.standaloneSetup(productController)
				.setViewResolvers(rr)
				.build();
	}
	
	@Test
	void productList_test() throws Exception {
		ProductDTO product = new ProductDTO();
		product.setProductId(1);
		product.setProductName("테스트");

		when(productService.selectProductByStoreId(1)).thenReturn(Arrays.asList(product));

		mockMvc.perform(get("/seller/product").param("storeId", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("seller_product"))
				.andExpect(model().attributeExists("productList"));
	}
	
	@Test
	void showForm_test() throws Exception {
		mockMvc.perform(get("/seller/product/new"))
		.andExpect(status().isOk())
		.andExpect(view().name("seller_product_add"));
		
	}
	
	 @Test
	    void insertForm_test() throws Exception {
	        when(productService.insertProduct(any(ProductDTO.class))).thenReturn(1);

	        mockMvc.perform(post("/seller/product"))
	                .andExpect(status().is3xxRedirection())
	                .andExpect(redirectedUrl("/seller/product"))
	                .andExpect(flash().attribute("resultMessage", "등록을 성공하였습니다."));
	    }
	 
	 @Test
	 void showEditForm_test() throws Exception {
	     ProductDTO product = new ProductDTO();
	     product.setProductId(1);
	     product.setProductName("테스트 상품");

	     when(productService.selectByProductId(1)).thenReturn(product);

	     mockMvc.perform(get("/seller/product/1/edit"))
	         .andExpect(status().isOk())
	         .andExpect(view().name("seller_product_edit"))
	         .andExpect(model().attributeExists("productDTO"));
	 }
	 
	 @Test
	 void updateProduct_test() throws Exception {
		 ProductDTO product = new ProductDTO();
	     product.setProductId(1);
	     product.setProductName("테스트 상품");
	     
	     when(productService.updateProduct(any(ProductDTO.class))).thenReturn(1);
	     
	     mockMvc.perform(put("/seller/product/1")
	     .param("productName", "테스트상품"))
	     .andExpect(redirectedUrl("/seller/product"));
	 }
	 
	 @Test
	 void deleteProduct_test() throws Exception {
		 when(productService.deleteProduct(1)).thenReturn(1);
		 
		 mockMvc.perform(delete("/seller/product/1"))
		 .andExpect(redirectedUrl("/seller/product"));
	 }
}
