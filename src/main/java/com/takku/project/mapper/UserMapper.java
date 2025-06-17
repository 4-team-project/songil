package com.takku.project.mapper;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.OrderDTO;
import com.takku.project.domain.ReviewDTO;
import com.takku.project.domain.UserDTO;

import java.util.List;

public interface UserMapper {

    // 회원가입
    void insertUser(UserDTO user);

    // 로그인용 사용자 조회
    UserDTO selectByPhone(String phone);

    // 내 정보 보기
    UserDTO selectByUserId(Integer userId);

    // 내 정보 수정
    void updateUser(UserDTO user);
}
