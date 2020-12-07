package com.automart.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

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

    public void addCart(Cart cart) {
        carts.add(cart);
        cart.setUser(this);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }

    @Builder
    public User(String email, String password, String tel, String name, String snsType, String snsKey, List<Cart> carts, List<Order> orders) {
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.name = name;
        this.snsType = snsType;
        this.snsKey = snsKey;
        this.carts = carts;
        this.orders = orders;
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
                .snsType("NULL").build();

        return user;
    }

    /**
     * SNS를 통해 회원가입한 유저 생성(이메일 존재)
     */
    public static User createUserBySns(String email, String snsType, String snsKey) {
        User user = User.builder()
                .email(email)
                .snsType(snsType)
                .snsKey(snsKey).build();

        return user;
    }

    /**
     * SNS를 통해 회원가입한 유저 생성(이메일 미존재 - 카카오톡)
     */
    public static User createUserBySns(String snsType, String snsKey) {
        User user = User.builder()
                .snsType(snsType)
                .snsKey(snsKey).build();

        return user;
    }
}
