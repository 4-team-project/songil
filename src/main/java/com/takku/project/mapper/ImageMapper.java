package com.takku.project.mapper;

import org.springframework.web.multipart.MultipartFile;

import com.takku.project.domain.ImageDTO;

public interface ImageMapper {

	// 등록
	int insertImageUrl(ImageDTO image);
	
	// 삭제
	int deleteImageUrl(String imageUrl);

}
