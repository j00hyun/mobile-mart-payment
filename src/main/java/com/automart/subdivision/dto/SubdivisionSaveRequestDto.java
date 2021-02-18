package com.automart.subdivision.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SubdivisionSaveRequestDto {

    @NotBlank(message = "소분류 이름을 입력해주세요.")
    private String name; // 소분류 이름

    @NotBlank(message = "소분류를 위치시킬 카테고리 고유 번호를 입력해주세요.")
    private int categoryNo; // 속한 카테고리의 고유 번호

    public SubdivisionSaveRequestDto() { }

    @Builder
    public SubdivisionSaveRequestDto(String name, int categoryNo) {
        this.name = name;
        this.categoryNo = categoryNo;
    }
}