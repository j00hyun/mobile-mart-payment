package com.automart.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CategoryUpdateRequestDto {

    private String name; // 카테고리 이름

    public CategoryUpdateRequestDto() { }

    @Builder
    public CategoryUpdateRequestDto(String name) {
        this.name = name;
    }
}