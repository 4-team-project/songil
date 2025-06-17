package com.takku.project.mapper;

import com.takku.project.domain.CouponDTO;
import com.takku.project.domain.OrderDTO;
import com.takku.project.domain.ReviewDTO;
import com.takku.project.domain.UserDTO;

import java.util.List;

public interface UserMapper {

    // ȸ������
    int insertUser(UserDTO user);

    // �α��ο� ����� ��ȸ
    UserDTO selectByPhone(String phone);

    // �� ���� ����
    UserDTO selectByUserId(Integer user_id);

    // �� ���� ����
    int updateUser(UserDTO user);
}
