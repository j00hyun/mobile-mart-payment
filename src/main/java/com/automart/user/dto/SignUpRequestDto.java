package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class SignUpRequestDto {

    private String email; // 사용자 이메일
    private String password; // 사용자 패스워드
    private String tel; // 사용자 전화번호
    private String name; // 사용자 이름
    private String snsType; // 사용자 SNS 연동 타입 [LOCAL, NAVER, KAKAO, GOOGLE]
    private String snsKey; // 사용자 SNS 고유 Key

    @Builder
    public SignUpRequestDto(String email, String password, String tel, String name, String snsType, String snsKey) {
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.name = name;
        this.snsType = snsType;
        this.snsKey = snsKey;
    }
}
