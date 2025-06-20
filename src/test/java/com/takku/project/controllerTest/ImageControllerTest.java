package com.takku.project.controllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.takku.project.controller.ImageController;
import com.takku.project.domain.ImageDTO;
import com.takku.project.service.ImageService;

public class ImageControllerTest {
	
	private MockMvc mockMvc;

	@Mock
	private ImageService imageService;
	
	@InjectMocks
	private ImageController imageController;
	
	 @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);

	        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
	        viewResolver.setPrefix("/WEB-INF/views/");
	        viewResolver.setSuffix(".jsp");

	        mockMvc = MockMvcBuilders
	                .standaloneSetup(imageController)
	                .setViewResolvers(viewResolver)
	                .build();
	    }
	 
	 @Test
	 @DisplayName("이미지 등록")
	 void insertImage() throws Exception {
	        when(imageService.insertImageUrl(any(ImageDTO.class))).thenReturn(1);

	        mockMvc.perform(post("/image")
	                .param("imageUrl", "test.jpg"))
	                .andExpect(redirectedUrl("/image"));
	    }
	 
	 @Test
	 @DisplayName("이미지 삭제")
	 void deleteImage() throws Exception {
	        when(imageService.deleteImageUrl("test.jpg")).thenReturn(0);

	        mockMvc.perform(delete("/image")
	                .param("imageUrl", "test.jpg"))
	                .andExpect(redirectedUrl("/image"));
	    }
 }

