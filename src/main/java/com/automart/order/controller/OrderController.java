package com.automart.order.controller;

import com.automart.advice.exception.NotFoundDataException;
import com.automart.cart.domain.Cart;
import com.automart.cart.repository.CartRepository;
import com.automart.order.dto.ResponseDataDto;
import com.automart.order.service.OrderService;
import com.automart.order.dto.OrderRequestDto;
import com.automart.order.dto.OrderResponseDto;
import com.automart.security.UserPrincipal;
import com.automart.user.dto.AuthResponseDto;
import com.automart.utility.HttpService;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Api(tags = {"6. User Payment"})
@RestController
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class OrderController {
    private final OrderService orderService;
    private final CartRepository cartRepository;

    private final HttpService httpService;

    @Value("${spring.iamport.key}")
    private String iamPortKey;

    @Value("${spring.iamport.secret}")
    private String iamPortSecret;

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
    public ResponseEntity<OrderResponseDto> order(@ApiParam("주문하는 고객 번호와 주문할 카트 번호") @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @RequestBody @Valid OrderRequestDto requestDto) throws Exception {
        OrderResponseDto orderResponseDto = orderService.order(userPrincipal.getPrincipal(),requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @ApiOperation(value = "주문 취소", notes = "해당 주문건을 취소한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "orderNo", value = "취소할 주문 번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 201, message = "해당 주문이 취소되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
            @ApiResponse(code = 404, message = "취소할 주문이 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/purchase/{orderNo}/cancel")
    public ResponseEntity<String> cancel(@PathVariable int orderNo) {
        orderService.cancelAll(orderNo);
        return ResponseEntity.status(HttpStatus.CREATED).body("해당 주문이 취소되었습니다.");
    }

    @ApiOperation(value = "주문에서 특정 상품 취소", notes = "주문에서 특정 상품에 대한 주문만 취소한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "취소할 주문 번호", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "productNo", value = "취소할 상품 번호", required = true, dataType = "int", defaultValue = "1"),
    })
    @ApiResponses({
            @ApiResponse(code = 201, message = "주문 단건이 취소되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                    "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
            @ApiResponse(code = 404, message = "취소할 주문 상품이 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/purchase/{orderNo}/cancel/{productNo}")
    public ResponseEntity<String> cancel(@PathVariable int orderNo, @PathVariable int productNo) {
        orderService.cancelOne(orderNo, productNo);
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

    @ApiOperation(value = "결제정보 일치여부 확인", notes = "조회된 결제정보(status,amount)가 올바른지 체크한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회된 결제정보가 일치하는지 확인된 결과를 true 또는 false로 응답합니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                    "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/payment/certification")
    public ResponseEntity<Boolean> confirmPaymentInfo(@RequestParam String imp_uid, @RequestParam String merchant_uid,
                                                      @RequestParam boolean imp_success, @RequestParam int cartNo) throws Exception {

        String postUrl = "https://api.iamport.kr/users/getToken";

        UriComponents postBuilder = UriComponentsBuilder.fromHttpUrl(postUrl)
                .build(false);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        params.add("imp_key", iamPortKey);
        params.add("imp_secret", iamPortSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, header);

        ResponseDataDto postResult = httpService.post(postBuilder, request, ResponseDataDto.class);

        String token = null;
        for (Field field : postResult.getResponse().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String value = field.get(postResult.getResponse()).toString();
            if (value.startsWith("access_token")) {
                int idx = value.indexOf("=");
                token = value.substring(idx + 1);
                break;
            }
        }

        // encode 하면 제대로 요청이 되지 않는다. (build에 encode설정)
        String getUrl = "https://api.iamport.kr/payments/find/{merchant_uid}/paid";
        UriComponents getBuilder = UriComponentsBuilder.fromHttpUrl(getUrl)
                .build(false)
                .expand(merchant_uid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.add("X-ImpTokenHeader", token);

        ResponseDataDto<Map<String, Object>> getResult = httpService.get(getBuilder, headers).getBody();
        JSONObject jsonObject = httpService.MapConverterToJson(getResult.getResponse());

        String status = (String) jsonObject.get("status");

        if (status.equals("paid")) {
            Integer amount = (Integer) jsonObject.get("amount");
            Cart cart = cartRepository.findByNo(cartNo).
                    orElseThrow(() -> new NotFoundDataException("카트를 불러올 수 없습니다."));

            if (amount == cart.getTotalPrice()) {
                ResponseEntity.status(HttpStatus.OK).body(true);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);
    }


    @ApiOperation(value = "가맹점 UID 조회", notes = "가맹점 UID를 조회한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 모든 주문건이 조회되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                    "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/payment/merchant/me")
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
