package com.automart.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategorySaveRequestDto {

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String name; // 카테고리 이름

    public CategorySaveRequestDto() { }

    @Builder
    public CategorySaveRequestDto(String name) {
        this.name = name;
    }
}