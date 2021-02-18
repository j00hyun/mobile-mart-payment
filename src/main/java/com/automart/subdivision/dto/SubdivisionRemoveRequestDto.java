package com.automart.subdivision.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SubdivisionRemoveRequestDto {

    @NotBlank(message = "소분류 고유 번호를 입력해주세요.")
    private int no; // 소분류 고유 번호

    public SubdivisionRemoveRequestDto() { }

    @Builder
    public SubdivisionRemoveRequestDto(int no) {
        this.no = no;
    }
}
