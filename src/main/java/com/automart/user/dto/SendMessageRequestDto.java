package com.automart.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class SendMessageRequestDto {

    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNo; // 문자를 전송할 핸드폰 번호

    public SendMessageRequestDto() { };

    @Builder
    public SendMessageRequestDto(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
