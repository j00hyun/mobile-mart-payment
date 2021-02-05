package com.automart.security;

import com.automart.user.domain.Admin;
import com.automart.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

/**
 * 인증된 로그인 유저 정보
 */
public class UserPrincipal implements OAuth2User, UserDetails {

    private static final long serialVersionUID = 1L;

    private int no;
    private String principal;
    private String password;
    private String name;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(int no, String principal, String password, String name, Collection<? extends GrantedAuthority> authorities) {
        this.no = no;
        this.principal = principal;
        this.password = password;
        this.name = name;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getNo(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                authorities
        );
    }

    public static UserPrincipal create(Admin admin) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new UserPrincipal(
                admin.getNo(),
                admin.getId(),
                admin.getPassword(),
                "admin",
                authorities
        );
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public int getNo() { return no; }

    public String getPrincipal() {
        return principal;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return principal;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(principal);
    }
}
