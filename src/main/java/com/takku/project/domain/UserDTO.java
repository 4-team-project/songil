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
	private Integer user_id;
    private String user_type;
    private String phone;
    private String password;
    private String name;
    private String gender;
    private Date birth;
    private String nickname;
    private String sido;
    private String sigungu;
    private Character is_partner;
    private Date created_at;
}
