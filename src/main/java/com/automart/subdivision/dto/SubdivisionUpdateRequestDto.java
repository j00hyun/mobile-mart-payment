package com.automart.subdivision.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SubdivisionUpdateRequestDto {

    @NotBlank(message = "소분류 이름을 입력해주세요.")
    private String name; // 소분류 이름

    public SubdivisionUpdateRequestDto() { }

    @Builder
    public SubdivisionUpdateRequestDto(String name) {
        this.name = name;
    }
}