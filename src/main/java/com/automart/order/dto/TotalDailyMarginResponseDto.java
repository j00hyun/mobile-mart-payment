package com.automart.order.dto;

// 일별 총 순수익
public interface TotalDailyMarginResponseDto {
    String getDate();
    Integer getMargin();
}
