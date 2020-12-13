package com.automart.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_no")
    private int no; // 사용자 고유번호

    @Column(name = "user_email", length = 45, nullable = false)
    private String email; // 사용자 이메일

    @Column(name = "user_pw", length = 45)
    private String password; // 사용자 비밀번호

    @Column(name = "user_tel", length = 45)
    private String tel; // 사용자 전화번호

    @Column(name = "user_name", length = 45)
    private String name; // 사용자 이름

    @Convert(converter = SnsTypeAttributeConverter.class)
    @Column(name = "user_sns_type", length = 45, nullable = false)
    private String snsType; // 사용자 SNS 연동 타입 [NULL, NAVER, KAKAO, GOOGLE]

    @Column(name = "user_sns_key", length = 45, unique = true)
    private String snsKey; // 사용자 SNS 고유 key

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @Builder
    public User(String email, String password, String tel, String name, String snsType, String snsKey) {
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.name = name;
        this.snsType = snsType;
        this.snsKey = snsKey;
    }

    /**
     * 앱에서 회원가입한 유저 생성
     */
    public static User createUserByApp(String email, String password, String tel, String name) {
        User user = User.builder()
                .email(email)
                .password(password)
                .tel(tel)
                .name(name)
                .snsType("LOCAL").build();

        return user;
    }

    /**
     * SNS를 통해 회원가입한 유저 생성(이메일 존재)
     */
    public static User createUserBySns(String email, String snsType) {
        User user = User.builder()
                .email(email)
                .snsType(snsType)
                .build();

        return user;
    }

    /**
     * 회원정보 수정
     */
}
