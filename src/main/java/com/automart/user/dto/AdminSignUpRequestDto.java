package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AdminSignUpRequestDto {

    @NotBlank(message = "매장 번호를 입력해주세요")
    @Pattern(regexp = "^[A-Z0-9].{2,15}$", message = "매장번호 형식이 옳바르지 않습니다.")
    private String id; // 관리자 아이디

    @NotBlank(message = "패스워드를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$", message = "영문,숫자,특수문자를 사용하여 8 ~ 15자리의 패스워드를 입력해주세요.")
    private String password; // 관리자 패스워드

    public AdminSignUpRequestDto() { };

    @Builder
    public AdminSignUpRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
