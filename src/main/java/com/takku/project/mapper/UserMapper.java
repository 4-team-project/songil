package com.takku.project.mapper;

import com.takku.project.domain.UserDTO;


public interface UserMapper {

	int insertUser(UserDTO user);

    UserDTO selectByPhone(String phone);

    UserDTO selectByUserId(Integer userId);

    int updateUser(UserDTO user);
}
