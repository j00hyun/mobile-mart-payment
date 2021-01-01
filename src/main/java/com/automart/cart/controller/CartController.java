package com.automart.cart.controller;

import com.automart.cart.dto.CartResponseDto;
import com.automart.cart.service.CartService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"1. Cart"})
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "장바구니 조회", notes = "바코드촬영 화면에서 위로 슬라이드하면 장바구니 목록이 반환됩니다.")
    @ApiImplicitParam(name = "userNo", value = "장바구니 조회를 요청하는 회원의 고유번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponse(code = 200, message = "정상적으로 장바구니 목록이 반환되었습니다.")
    @GetMapping("/carts/list")
    public ResponseEntity<List<CartResponseDto>> showUserCart(@RequestParam(value = "userNo") int userNo) {
        List<CartResponseDto> cartResponseDtos = cartService.showUserCarts(userNo);
        return ResponseEntity.status(HttpStatus.OK).body(cartResponseDtos);
    }

    @ApiOperation(value = "장바구니 상품 추가", notes = "상품의 바코드를 인식하면 장바구니에 상품이 담깁니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "해당 회원의 고유번호", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "productCode", value = "개수를 증가하려는 제품의 바코드 번호", required = true, dataType = "int", defaultValue = "12345")
    })
    @ApiResponse(code = 200, message = "정상적으로 장바구니의 상품 개수가 증가되었습니다.")
    @PutMapping("/carts/add/{userNo}/{productCode}")
    public ResponseEntity<Void> addProductByCode(@PathVariable int userNo, @PathVariable int productCode) {
        cartService.addProductToCart(userNo, productCode);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "장바구니 상품 개수 증가", notes = "장바구니 화면에서 해당 상품의 개수 '+' 버튼을 누르면 장바구니의 상품 개수가 1개 증가됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "해당 회원의 고유번호", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "productNo", value = "개수를 증가하려는 제품의 고유번호", required = true, dataType = "int", defaultValue = "1")
    })
    @ApiResponse(code = 200, message = "정상적으로 장바구니의 상품 개수가 증가되었습니다.")
    @PutMapping("/carts/add/{userNo}/{productNo}")
    public ResponseEntity<Void> addProductCount(@PathVariable int userNo, @PathVariable int productNo) {
        cartService.addProduct(userNo, productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "장바구니 상품 개수 감소", notes = "장바구니 화면에서 해당 상품의 개수 '-' 버튼을 누르면 장바구니의 상품 개수가 1개 감소됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "해당 회원의 고유번호", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "productNo", value = "개수를 감하려는 제품의 고유번호", required = true, dataType = "int", defaultValue = "1")
    })
    @ApiResponse(code = 200, message = "정상적으로 장바구니의 상품 개수가 감소되었습니다.")
    @PutMapping("/carts/sub/{userNo}/{productNo}")
    public ResponseEntity<Void> subProductCount(@PathVariable int userNo, @PathVariable int productNo) {
        cartService.subtractProduct(userNo, productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "장바구니 상품 삭제", notes = "장바구니 화면에서 해당 상품의 '삭제하기' 버튼을 누르면 장바구니에서 해당 상품이 삭제됩니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userNo", value = "해당 회원의 고유번호", required = true, dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "productNo", value = "삭제하려는 제품의 고유번호", required = true, dataType = "int", defaultValue = "1")
    })
    @ApiResponse(code = 200, message = "정상적으로 장바구니의 상품이 삭제되었습니다.")
    @DeleteMapping("/carts/delete/{userNo}/{productNo}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int userNo, @PathVariable int productNo) {
        cartService.takeProductOutOfCart(userNo, productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
