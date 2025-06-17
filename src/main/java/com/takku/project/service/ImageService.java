package com.takku.project.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.ImageDTO;
import com.takku.project.mapper.ImageMapper;

@Service
public class ImageService implements ImageMapper{

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.ImageMapper.";
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
	
}
