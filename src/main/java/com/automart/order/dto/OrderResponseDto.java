package com.automart.order.dto;

import com.automart.order.domain.Order;
import com.automart.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {

    private int orderNo; // 주문 고유번호
    private LocalDateTime orderDate; // 주문 날짜
    private String state; // 주문 상태 [ORDER, CANCLE]}
    private List<OrderDetailDto> orderDetails = new ArrayList<>();
    private int totalPrice; // 결제금액

    @Builder
    public OrderResponseDto(int orderNo, LocalDateTime orderDate, String state,List<OrderDetailDto> orderDetails, int totalPrice) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.state = state;
        this.orderDetails = orderDetails;
        this.totalPrice = totalPrice;
    }

    public static OrderResponseDto of(Order order) {
        return OrderResponseDto.builder()
                .orderNo(order.getNo())
                .orderDate(order.getOrderDate())
                .state(order.getState())
                .orderDetails(order.getOrderDetails().stream().map(orderDetail -> new OrderDetailDto(orderDetail)).collect(Collectors.toList()))
                .totalPrice(order.getTotalPrice())
                .build();
    }

    public static List<OrderResponseDto> listOf(List<Order> orders) {
        return orders.stream().map(OrderResponseDto::of)
                .collect(Collectors.toList());
    }

    @Data
    static class OrderDetailDto {
        // private int productNo; // 구매 제품 고유번호
        private String productName; // 구매 상품명
        private int count; // 구매 수량
        private int price; // 수량 포함 제품 가격

        public OrderDetailDto(OrderDetail orderDetail) {
            // this.productNo = orderDetail.getProduct().getNo();
            this.productName = orderDetail.getProduct().getName();
            this.count = orderDetail.getCount();
            this.price = orderDetail.getPrice();
        }
    }
}