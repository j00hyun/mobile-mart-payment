package com.automart.user.dto;

import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponseDto {

    private String email;
    private String tel;
    private String name;


    @Builder
    public UserResponseDto(String email, String password, String tel, String name, AuthProvider snsType, String snsKey) {
        this.email = email;
        this.tel = tel;
        this.name = name;
    }

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .email(user.getEmail())
                .tel(user.getTel())
                .name(user.getName())
                .build();
    }

    public static List<UserResponseDto> listOf(List<User> users) {
        return users.stream().map(UserResponseDto::of)
                .collect(Collectors.toList());
    }
}
