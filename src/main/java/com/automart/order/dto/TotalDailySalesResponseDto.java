package com.automart.order.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface TotalDailySalesResponseDto {
    String getDate();
    Integer getPrice();
}
