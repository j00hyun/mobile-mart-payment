package com.automart.order.dto;

// 기간별 특정 카테고리의 상위 매출 5개 제품 반환
public interface BestProductResponseDto {
    Integer getNo();
    String getSubdiv();
    String getName();
    Integer getPrice();
    Integer getBeforePrice();
}
