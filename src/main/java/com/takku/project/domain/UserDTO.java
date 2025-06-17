package com.takku.project.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Integer userId;
    private String userType;
    private String phone;
    private String password;
    private String name;
    private String gender;
    private Date birth;
    private String nickname;
    private String sido;
    private String sigungu;
    private Character isPartner;
    private Date createdAt;
}
