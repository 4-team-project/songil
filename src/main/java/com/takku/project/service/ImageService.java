package com.takku.project.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.takku.project.domain.ImageDTO;
import com.takku.project.mapper.ImageMapper;

@Service
public class ImageService implements ImageMapper {

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.ImageMapper.";

	@Value("${file.upload.path}")
	private String uploadDir;

	public ImageDTO storeImage(MultipartFile file, Integer productId, Integer fundingId, Integer reviewId) {
		if (file.isEmpty())
			return null;

		try {
			String uuid = UUID.randomUUID().toString();
			String fileName = uuid + "_" + file.getOriginalFilename();
			File dest = new File(uploadDir + File.separator + fileName);
			file.transferTo(dest);

			return ImageDTO.builder().productId(productId).fundingId(fundingId).reviewId(reviewId)
					.imageUrl("/image/" + fileName).build();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int insertImageUrl(ImageDTO image) {
		int result = sqlSession.insert(namespace + "insertImageUrl", image);
		return result;
	}

	@Override
	public int deleteImageUrl(String imageUrl) {
		int result = sqlSession.delete(namespace + "deleteImageUrl", imageUrl);
		return result;
	}

	@Override
	public List<ImageDTO> selectImagesByFundingId(int fundingId) {
		List<ImageDTO> imagelist = sqlSession.selectList(namespace + "selectImagesByFundingId", fundingId);
		return imagelist;
	}

	@Override
	public List<ImageDTO> selectImagesByReviewId(int reviewId) {
		List<ImageDTO> imagelist = sqlSession.selectList(namespace + "selectImagesByReviewId", reviewId);
		return imagelist;
	}

	@Override
	public List<ImageDTO> selectImagesByProductId(int productId) {
		List<ImageDTO> imagelist = sqlSession.selectList(namespace + "selectImagesByProductId", productId);
		return imagelist;
	}
}
