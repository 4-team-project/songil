package com.takku.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.takku.project.domain.ImageDTO;
import com.takku.project.service.ImageService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // 이미지 URL 등록
    @PostMapping
    public String insertImage(ImageDTO imageDTO, RedirectAttributes ra) {
        int result = imageService.insertImageUrl(imageDTO);
        if (result > 0) {
            ra.addFlashAttribute("resultMessage", "이미지 등록 성공");
        } else {
            ra.addFlashAttribute("resultMessage", "이미지 등록 실패");
        }
        return "redirect:/image";  
    }

    // 이미지 URL 삭제
    @DeleteMapping
    public String deleteImage(@RequestParam("imageUrl") String imageUrl, RedirectAttributes ra) {
        int result = imageService.deleteImageUrl(imageUrl);
        if (result > 0) {
            ra.addFlashAttribute("resultMessage", "이미지 삭제 성공");
        } else {
            ra.addFlashAttribute("resultMessage", "이미지 삭제 실패");
        }
        return "redirect:/image"; 
    }


    @GetMapping
    public String showImageForm() {
        return "image_form"; 
    }
}