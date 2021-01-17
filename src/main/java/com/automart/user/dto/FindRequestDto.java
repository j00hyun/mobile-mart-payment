package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FindRequestDto {
    private String name; // 사용자 이름
    private String phone; // 사용자 휴대폰 번호

    public FindRequestDto() { };

    @Builder
    public FindRequestDto(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
