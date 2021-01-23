package com.automart.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CategorySaveRequestDto {

    private String code; // 카테고리 고유 코드
    private String name; // 카테고리 이름

    public CategorySaveRequestDto() { }

    @Builder
    public CategorySaveRequestDto(String code, String name) {
        this.code = code;
        this.name = name;
    }
}