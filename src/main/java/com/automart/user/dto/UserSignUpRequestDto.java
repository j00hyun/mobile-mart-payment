package com.automart.user.dto;

import com.automart.user.domain.AuthProvider;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserSignUpRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 양식을 지켜주세요.")
    private String email; // 사용자 이메일

    @NotBlank(message = "패스워드를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$", message = "영문,숫자,특수문자를 사용하여 8 ~ 15자리의 패스워드를 입력해주세요.")
    private String password; // 사용자 패스워드

    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String tel; // 사용자 전화번호
    @NotBlank(message = "이름을 입력해주세요")
    private String name; // 사용자 이름

    public UserSignUpRequestDto() { };

    @Builder
    public UserSignUpRequestDto(String email, String password, String tel, String name) {
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.name = name;
    }
}
