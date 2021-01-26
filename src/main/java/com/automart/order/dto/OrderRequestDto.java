package com.automart.order.dto;

import com.automart.order.domain.Order;
import com.automart.order.domain.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class OrderRequestDto {

    @PositiveOrZero(message = "주문하는 고객의 고유번호를 입력해주세요.")
    private int userNo; // 고객 고유번호
    @PositiveOrZero(message = "주문할 카트의 고유번호를 입력해주세요.")
    private List<Integer> cartNo; // 카트 고유번호

}