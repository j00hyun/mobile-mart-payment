package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class SignInRequestDto {
    private String email; // 사용자 이메일
    private String password; // 사용자 패스워드

    @Builder
    public SignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}