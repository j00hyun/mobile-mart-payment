package com.automart.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryRemoveRequestDto {

    @NotBlank(message = "카테고리 고유 코드를 입력해주세요.")
    private String code; // 카테고리 고유 코드

    public CategoryRemoveRequestDto() { }

    @Builder
    public CategoryRemoveRequestDto(String code) {
        this.code = code;
    }
}
