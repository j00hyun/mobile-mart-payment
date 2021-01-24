package com.automart.order.controller;

import com.automart.order.service.OrderService;
import com.automart.order.dto.OrderRequestDto;
import com.automart.order.dto.OrderResponseDto;
import com.automart.user.dto.AuthResponseDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"6. User Payment"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/purchase")
public class OrderController {
    private final OrderService orderService;

    @ApiOperation(value = "주문 하기", notes = "결제한 상품들을 주문 한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 주문되었습니다."),
            @ApiResponse(code = 400, message = "장바구니에 상품을 담아주세요."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<OrderResponseDto> order(@ApiIgnore @RequestHeader("Authorization") String token,
                                                  @ApiParam("주문하는 고객 번호와 주문할 카트 번호") @RequestBody @Valid OrderRequestDto requestDto) throws Exception {
        OrderResponseDto orderResponseDto = orderService.order(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @ApiOperation(value = "주문 취소", notes = "해당 주문건을 취소한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "orderNo", value = "취소할 주문 번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 주문되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
            @ApiResponse(code = 404, message = "취소할 주문이 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{orderNo}/cancel")
    public ResponseEntity<String> cancel(@ApiIgnore @RequestHeader("Authorization") String token,
                                         @PathVariable int orderNo) {
        orderService.cancel(orderNo);
        return ResponseEntity.status(HttpStatus.CREATED).body("해당 상품에 대한 주문이 취소되었습니다.");
    }

    @ApiOperation(value = "주문 상세 조회", notes = "해당 주문건을 조회한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "orderNo", value = "조회할 주문의 주문 번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 해당 주문건이 조회되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
            @ApiResponse(code = 404, message = "해당 주문이 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<OrderResponseDto> showOrder(@ApiIgnore @RequestHeader("Authorization") String token,
                                                      @RequestParam int orderNo) {
        OrderResponseDto orderResponseDto = orderService.showOrder(orderNo);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    @ApiOperation(value = "주문 목록 조회", notes = "모든 주문건을 조회한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 모든 주문건이 조회되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/purchase/list")
    public ResponseEntity<List<OrderResponseDto>> showOrders(@ApiIgnore @RequestHeader("Authorization") String token) {
        List<OrderResponseDto> orderResponseDtos = orderService.showOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDtos);
    }

}
