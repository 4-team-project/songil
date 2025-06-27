package com.takku.project.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.mapper.TagMapper;

@Service
public class TagService implements TagMapper{

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.TagMapper.";
	
	@Override
	public List<String> selectTagNamesByFundingId(Integer fundingId) {
		return sqlSession.selectList(namespace + "selectTagNamesByFundingId", fundingId);
	}
}
