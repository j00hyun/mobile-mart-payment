package com.automart.order.dto;

import com.automart.order.domain.Order;
import com.automart.order.domain.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class OrderRequestDto {

    private int userNo; // 고객 고유번호
    private List<Integer> cartNos = new ArrayList<>(); // 카트 고유번호

}