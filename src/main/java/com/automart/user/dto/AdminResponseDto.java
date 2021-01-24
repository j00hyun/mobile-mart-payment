package com.automart.user.dto;

import com.automart.user.domain.Admin;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AdminResponseDto {

    private String id;

    @Builder
    public AdminResponseDto(String id) {
        this.id = id;
    }

    public static AdminResponseDto of(Admin admin) {
        return AdminResponseDto.builder()
                .id(admin.getId())
                .build();
    }
}
