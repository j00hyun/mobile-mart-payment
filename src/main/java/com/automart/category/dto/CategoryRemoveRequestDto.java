package com.automart.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryRemoveRequestDto {

    @NotBlank(message = "카테고리 고유 번호를 입력해주세요.")
    private int no; // 카테고리 고유 번호

    public CategoryRemoveRequestDto() { }

    @Builder
    public CategoryRemoveRequestDto(int no) {
        this.no = no;
    }
}
