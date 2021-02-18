package com.automart.category.dto;

import com.automart.category.domain.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryResponseDto {

    private int no; // 카테고리 고유 번호
    private String name; // 제품 이름


    @Builder
    public CategoryResponseDto(int no, String name) {
        this.no = no;
        this.name = name;
    }


    public static CategoryResponseDto of(Category category) {
        return CategoryResponseDto.builder()
                .no(category.getNo())
                .name(category.getName())
                .build();
    }


    public static List<CategoryResponseDto> listOf(List<Category> categories) {
        return categories.stream().map(CategoryResponseDto::of)
                .collect(Collectors.toList());
    }
}
