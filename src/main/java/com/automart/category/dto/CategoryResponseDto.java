package com.automart.category.dto;

import com.automart.category.domain.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryResponseDto {

    private String code; // 카테고리 고유 코드
    private String name; // 제품 이름


    @Builder
    public CategoryResponseDto(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public static CategoryResponseDto of(Category category) {
        return CategoryResponseDto.builder()
                .code(category.getCode())
                .name(category.getName())
                .build();
    }


    public static List<CategoryResponseDto> listOf(List<Category> categories) {
        return categories.stream().map(CategoryResponseDto::of)
                .collect(Collectors.toList());
    }
}
