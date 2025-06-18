package com.takku.project.mapper;

import com.takku.project.domain.UserDTO;


public interface UserMapper {

    // 회원가입
	int insertUser(UserDTO user);

    // 濡洹몄몄 ъ⑹ 議고
    UserDTO selectByPhone(String phone);

    // 내 정보 보기
    UserDTO selectByUserId(Integer userId);

    // 내 정보 수정
    int updateUser(UserDTO user);
    
    //이메일 중복 검사, validationcontroller에서 중복검사
    int countByEmail(String email);
}
