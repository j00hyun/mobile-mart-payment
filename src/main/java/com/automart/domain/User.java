package com.automart.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "user_pw", length = 45, nullable = false)
    private String password; // 사용자 비밀번호

    @Column(name = "user_tel", length = 45)
    private String tel; // 사용자 전화번호

    @Column(name = "user_sns_type", length = 45)
    private String snsType; // 사용자 SNS 연동 타입

    @Column(name = "user_sns_id", length = 45)
    private String snsId; // 사용자 SNS 고유 아이디

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

}
