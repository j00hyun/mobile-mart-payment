package com.automart.order.dto;

import lombok.Data;
import javax.validation.constraints.PositiveOrZero;


@Data
public class OrderRequestDto {

//    @PositiveOrZero(message = "주문하는 고객의 고유번호를 입력해주세요.")
//    private int userNo; // 고객 고유번호
    @PositiveOrZero(message = "주문할 카트의 고유번호를 입력해주세요.")
    private int cartNo; // 카트 고유번호

}