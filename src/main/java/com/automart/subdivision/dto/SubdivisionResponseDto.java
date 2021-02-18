package com.automart.subdivision.dto;

import com.automart.subdivision.domain.Subdivision;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SubdivisionResponseDto {

    private int no; // 소분류 고유 번호
    private String name; // 소분류 이름


    @Builder
    public SubdivisionResponseDto(int no, String name) {
        this.no = no;
        this.name = name;
    }


    public static SubdivisionResponseDto of(Subdivision subdivision) {
        return SubdivisionResponseDto.builder()
                .no(subdivision.getNo())
                .name(subdivision.getName())
                .build();
    }


    public static List<SubdivisionResponseDto> listOf(List<Subdivision> subdivisions) {
        return subdivisions.stream().map(SubdivisionResponseDto::of)
                .collect(Collectors.toList());
    }
}
