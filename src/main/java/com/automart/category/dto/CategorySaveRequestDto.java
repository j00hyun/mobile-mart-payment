package com.automart.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CategorySaveRequestDto {

    @NotBlank(message = "카테고리 고유 코드를 입력해주세요.")
    private String code; // 카테고리 고유 코드
    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String name; // 카테고리 이름

    public CategorySaveRequestDto() { }

    @Builder
    public CategorySaveRequestDto(String code, String name) {
        this.code = code;
        this.name = name;
    }
}