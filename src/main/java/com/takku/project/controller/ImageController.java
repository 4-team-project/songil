package com.takku.project.controller;

import com.takku.project.domain.ImageDTO;
import com.takku.project.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/image")
public class ImageController {

	@Autowired
	private ImageService imageService;

	@Value("${file.upload.path}")
	private String uploadPath;

	@PostMapping("/upload")
	public String uploadImage(@RequestParam("file") MultipartFile file) {
		ImageDTO image = imageService.storeImage(file, null, null, null);
		if (image != null) {
			String fileName = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1);
			return "/image/view/" + fileName;
		}
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed");
	}

	@GetMapping("/{fileName:.+}")
	public void viewImage(@PathVariable String fileName, HttpServletResponse response) throws IOException {
		File file = new File(uploadPath + fileName);
		if (file.exists()) {
			String contentType = Files.probeContentType(file.toPath());
			response.setContentType(contentType != null ? contentType : "application/octet-stream");
			Files.copy(file.toPath(), response.getOutputStream());
			response.getOutputStream().flush();
		} else {
			response.sendError(404, "File not found");
		}
	}

	@PostMapping
	public String insertImage(ImageDTO imageDTO, RedirectAttributes ra) {
		int result = imageService.insertImageUrl(imageDTO);
		ra.addFlashAttribute("resultMessage", result > 0 ? "이미지 등록 성공" : "이미지 등록 실패");
		return "redirect:/image";
	}

	@DeleteMapping
	public String deleteImage(@RequestParam("imageUrl") String imageUrl, RedirectAttributes ra) {
		int result = imageService.deleteImageUrl(imageUrl);
		ra.addFlashAttribute("resultMessage", result > 0 ? "이미지 삭제 성공" : "이미지 삭제 실패");
		return "redirect:/image";
	}

	@GetMapping
	public String showImageForm() {
		return "image_form";
	}
}
