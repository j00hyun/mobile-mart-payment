package com.automart.order.controller;

import com.automart.order.dto.ConfirmResponseDto;
import com.automart.order.service.OrderService;
import com.automart.order.dto.OrderRequestDto;
import com.automart.order.dto.OrderResponseDto;
import com.automart.security.UserPrincipal;
import com.automart.user.dto.AuthResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

@Api(tags = {"6. User Payment"})
@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    // private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "주문 하기", notes = "결제한 상품들을 주문 한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 주문되었습니다."),
            @ApiResponse(code = 400, message = "장바구니에 상품을 담아주세요."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/purchase")
    public ResponseEntity<OrderResponseDto> order(@ApiParam("주문하는 고객 번호와 주문할 카트 번호") @RequestBody @Valid OrderRequestDto requestDto) throws Exception {
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
    @PutMapping("/purchase/{orderNo}/cancel")
    public ResponseEntity<String> cancel(@PathVariable int orderNo) {
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
    @GetMapping("/purchase")
    public ResponseEntity<OrderResponseDto> showOrder(@RequestParam int orderNo) {
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
    public ResponseEntity<List<OrderResponseDto>> showOrders() {
        List<OrderResponseDto> orderResponseDtos = orderService.showOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDtos);
    }

//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/payment/confirm")
//    public ResponseEntity<ConfirmResponseDto> confirm(@RequestHeader("Authorization") String token, @RequestParam String imp_uid,
//                                                      @RequestParam String merchant_uid, @RequestParam boolean imp_success) {
//        RestTemplate restTemplate = new RestTemplate(); // 아임포트 서버에 REST API 요청
//        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//
//        String url = "https://api.iamport.kr/payments/" + imp_uid;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
//        headers.add("Authorization",token);
//
//        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
//                .build(false);
//
//        ResponseEntity<ConfirmResponseDto> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(headers), ConfirmResponseDto.class);
//        log.info("아임포트 요청 결과 : " + response.getStatusCode());
//
//        return null;
//        //return response;
//    }


    @ApiOperation(value = "가맹점 UID 조회", notes = "가맹점 UID를 조회한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 모든 주문건이 조회되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                    "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/payment/v1/merchant/me")
    public ResponseEntity<String> getUniqueMerchantUid(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                       @RequestBody MerchantRequestDto merchantRequestDto){
        String email = userPrincipal.getPrincipal();
        String merchantUid = merchantRequestDto.getMerchantUid();

        int idx = merchantUid.indexOf("_");

        String pre = merchantUid.substring(0,idx);
        String mid = Base64.getEncoder().encodeToString(email.getBytes());
        String end = merchantUid.substring(idx+1);

        String merchant_uid = pre + mid + end;

        return ResponseEntity.status(HttpStatus.OK).body(merchant_uid);

    }


    @Data
    static class MerchantRequestDto {
        @JsonProperty("merchant_uid")
        private String merchantUid;
    }
}
