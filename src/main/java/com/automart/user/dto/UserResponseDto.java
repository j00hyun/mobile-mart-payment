package com.automart.user.dto;

import com.automart.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponseDto {

    private int no;
    private String email;
    private String password;
    private String tel;
    private String name;
    private String snsType;
    private String snsKey;


    @Builder
    public UserResponseDto(int no, String email, String password, String tel, String name, String snsType, String snsKey) {
        this.no = no;
        this.email = email;
        this.password = password;
        this.tel = tel;
        this.name = name;
        this.snsType = snsType;
        this.snsKey = snsKey;
    }

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .no(user.getNo())
                .email(user.getEmail())
                .password(user.getPassword())
                .tel(user.getTel())
                .name(user.getName())
                .snsType(user.getSnsType())
                .snsKey(user.getSnsKey())
                .build();
    }

    public static List<UserResponseDto> listOf(List<User> users) {
        return users.stream().map(UserResponseDto::of)
                .collect(Collectors.toList());
    }
}