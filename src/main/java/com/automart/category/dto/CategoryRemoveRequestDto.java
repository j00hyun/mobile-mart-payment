package com.automart.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CategoryRemoveRequestDto {
    private String code; // 카테고리 고유 코드

    public CategoryRemoveRequestDto() { }

    @Builder
    public CategoryRemoveRequestDto(String code) {
        this.code = code;
    }
}
