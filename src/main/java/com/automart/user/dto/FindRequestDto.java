package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class FindRequestDto {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name; // 사용자 이름

    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력해주세요.")
    private String phone; // 사용자 휴대폰 번호

    public FindRequestDto() { };

    @Builder
    public FindRequestDto(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
