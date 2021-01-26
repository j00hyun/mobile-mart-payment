package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserSignInRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 양식을 지켜주세요.")
    private String email; // 사용자 이메일

    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password; // 사용자 패스워드

    public UserSignInRequestDto() { };

    @Builder
    public UserSignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
