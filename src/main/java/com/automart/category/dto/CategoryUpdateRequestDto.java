package com.automart.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CategoryUpdateRequestDto {

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String name; // 카테고리 이름

    public CategoryUpdateRequestDto() { }

    @Builder
    public CategoryUpdateRequestDto(String name) {
        this.name = name;
    }
}