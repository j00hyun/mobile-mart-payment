package com.automart.order.controller;

import com.automart.order.service.OrderService;
import com.automart.order.dto.OrderRequestDto;
import com.automart.order.dto.OrderResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchase")
public class OrderController {
    private final OrderService orderService;

    @ApiOperation("주문 하기")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<OrderResponseDto> order(@RequestBody @Valid OrderRequestDto requestDto) throws Exception {
        OrderResponseDto orderResponseDto = orderService.order(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @ApiOperation("주문 취소")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{orderNo}/cancel")
    public ResponseEntity<String> cancel(@PathVariable int orderNo) {
        orderService.cancel(orderNo);
        return ResponseEntity.status(HttpStatus.CREATED).body("해당 상품에 대한 주문이 취소되었습니다.");
    }

    @ApiOperation("주문 상세 조회")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<OrderResponseDto> showOrder(@RequestParam int orderNo) {
        OrderResponseDto orderResponseDto = orderService.showOrder(orderNo);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    @ApiOperation("주문 목록 조회")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/purchase/list")
    public ResponseEntity<List<OrderResponseDto>> showOrders() {
        List<OrderResponseDto> orderResponseDtos = orderService.showOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDtos);
    }

}
