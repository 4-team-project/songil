package com.takku.project.mapper;

import com.takku.project.domain.UserDTO;


public interface UserMapper {

	int insertUser(UserDTO user);

	UserDTO selectByPhone(String phone, String password);

    UserDTO selectByUserId(Integer userId);

    int updateUser(UserDTO user);
    
    //이메일 중복 검사, validationcontroller에서 중복검사
    int countByEmail(String email);
}
