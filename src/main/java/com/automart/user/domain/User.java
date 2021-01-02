package com.automart.user.domain;

import com.automart.cart.domain.Cart;
import com.automart.order.domain.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private int no; // 사용자 고유번호

    @Column(name = "user_email", length = 45, nullable = false)
    private String email; // 사용자 이메일

    @Column(name = "user_pw", length = 70)
    private String password; // 사용자 비밀번호

    @Column(name = "user_tel", length = 45)
    private String tel; // 사용자 전화번호

    @Column(name = "user_name", length = 45)
    private String name; // 사용자 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "user_sns_type", length = 45, nullable = false)
    private AuthProvider snsType; // 사용자 SNS 연동 타입 [local, naver, google, kakao]

    @Column(name = "user_sns_key", length = 45, unique = true)
    private String snsKey; // 사용자 SNS 고유 key

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER) // = @OneToMany, but @ElementCollection은 비엔티티를 매핑하는데 사용
    private List<String> roles = new ArrayList<>();

    @Builder
    public User(String email, String password, String tel, String name, AuthProvider snsType, String snsKey, List roles) {
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.name = name;
        this.snsType = snsType;
        this.snsKey = snsKey;
        this.roles = roles;
    }

    // 비밀번호 변경
    public void setPassword(String password) {
        this.password = password;
    }

    // 소셜로그인 유저 이메일 변겅
    public void setEmail(String email) { this.email = email; }

    // 해당 유저의 장바구니중 특정 제품 제거
    public void removeCart(Cart cart) {
        this.carts.remove(cart);
    }

    // 해당 유저에 해당하는 장바구니 모두 제거
    public void removeAllCart() {
        this.carts.forEach(cart -> cart.removeCart());
        this.carts.clear();
    }
}
