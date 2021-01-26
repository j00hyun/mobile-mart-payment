package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ChangePasswordRequestDto {

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$", message = "영문,숫자,특수문자를 사용하여 8 ~ 15자리의 패스워드를 입력해주세요.")
    private String password; // 새 비밀번호

    public ChangePasswordRequestDto() { };

    @Builder
    public ChangePasswordRequestDto(String password) {
        this.password = password;
    }
}
