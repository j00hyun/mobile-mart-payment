package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class VerifyPasswordRequestDto {

    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password; // 사용자 패스워드

    public VerifyPasswordRequestDto() { };

    @Builder
    public VerifyPasswordRequestDto(String email, String password) {
        this.password = password;
    }
}
