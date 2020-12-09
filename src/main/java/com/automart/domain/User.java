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
public class User implements UserDetails {

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

    /**
     * 사용자의 권한을 콜렉션 형태로 반환
     * 단, 클래스 자료형은 GrantedAuthority를 구현해야함
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        return roles;
    }

    /**
     * 사용자의 id를 반환 (unique한 값)
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 사용자의 password를 반환
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 계정 만료 여부 반환
     */
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    /**
     * 계정 잠금 여부 반환
     */
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    /**
     * 패스워드의 만료 여부 반환
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    /**
     * 계정 사용 가능 여부 반환
     */
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
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
}
