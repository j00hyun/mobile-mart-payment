package com.automart.order.controller;

import com.automart.order.Service.OrderService;
import com.automart.order.dto.OrderRequestDto;
import com.automart.order.dto.OrderResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ApiOperation("주문 하기")
    @PostMapping("/order/done")
    public ResponseEntity<OrderResponseDto> order(@RequestBody @Valid OrderRequestDto requestDto) throws Exception {
        OrderResponseDto orderResponseDto = orderService.order(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    @ApiOperation("주문 취소")
    @PostMapping("/{orderNo}/cancel")
    public ResponseEntity<OrderResponseDto> cancel(@PathVariable int orderNo) {
        orderService.cancel(orderNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("주문 상세 조회")
    @GetMapping("/purchase/detail/{orderNo}")
    public ResponseEntity<OrderResponseDto> showOrder(@PathVariable int orderNo) {
        OrderResponseDto orderResponseDto = orderService.showOrder(orderNo);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    @ApiOperation("주문 목록 조회")
    @GetMapping("/purchase/list")
    public ResponseEntity<List<OrderResponseDto>> showOrders() {
        List<OrderResponseDto> orderResponseDtos = orderService.showOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDtos);
    }

}
