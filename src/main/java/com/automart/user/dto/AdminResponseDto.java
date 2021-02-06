package com.automart.user.dto;

import com.automart.user.domain.Admin;
import lombok.Builder;
import lombok.Getter;

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
