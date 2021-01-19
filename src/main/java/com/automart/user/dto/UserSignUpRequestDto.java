package com.automart.user.dto;

import com.automart.user.domain.AuthProvider;
import lombok.Builder;
import lombok.Data;

@Data
public class UserSignUpRequestDto {

    private String email; // 사용자 이메일
    private String password; // 사용자 패스워드
    private String tel; // 사용자 전화번호
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
