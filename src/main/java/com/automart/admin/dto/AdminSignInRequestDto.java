package com.automart.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminSignInRequestDto {
    private String id; // 관리자 아이디
    private String password; // 관리자 패스워드

    public AdminSignInRequestDto() { };

    @Builder
    public AdminSignInRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
