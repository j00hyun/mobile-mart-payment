package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FindRequestDto {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name; // 사용자 이름
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    private String phone; // 사용자 휴대폰 번호

    public FindRequestDto() { };

    @Builder
    public FindRequestDto(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
