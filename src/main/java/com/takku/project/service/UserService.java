package com.takku.project.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takku.project.domain.UserDTO;
import com.takku.project.mapper.UserMapper;

@Service
public class UserService implements UserMapper{

	@Autowired
	SqlSession sqlSession;
	String namespace = "com.takku.project.mapper.UserMapper.";
	
	@Override
	public int insertUser(UserDTO user) {
			int count = countByPhone(user.getPhone(), user.getUserType());
			if(count > 0) {
				throw new RuntimeException("이미 존재하는 사용자 번호입니다.");
			}

		int result = sqlSession.insert(namespace + "insertUser", user);
		return result;
	}

	@Override
	public UserDTO selectByPhone(String phone, String password) {
		UserDTO user = sqlSession.selectOne(namespace + "selectByPhone", phone);
		boolean isUser = user.getPassword().equals(password);
		if(isUser == false || user == null) {
			return null;
		} else {
		return user;
		}
	}

	@Override
	public UserDTO selectByUserId(Integer userId) {
		UserDTO user = sqlSession.selectOne(namespace + "selectByUserId", userId);
		return user;
	}

	@Override
	public int updateUser(UserDTO user) {
		int result = sqlSession.update(namespace + "updateUser", user);
		return result;
	}

	@Override
	public int countByEmail(String email) {
		int result = sqlSession.selectOne(namespace+"countByEmail", email);
		return result;
	}

	@Override
	public int countByPhone(String phone, String userType) {
		Map<String, Object> map = new HashMap<>();
		map.put("phone", phone);
		map.put("userType", userType);
		int result = sqlSession.selectOne(namespace + "countByPhone", map);
		return result;
	}

	

	
}
