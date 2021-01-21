package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminSignUpRequestDto {

    private String id; // 관리자 아이디
    private String password; // 관리자 패스워드

    public AdminSignUpRequestDto() { };

    @Builder
    public AdminSignUpRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
