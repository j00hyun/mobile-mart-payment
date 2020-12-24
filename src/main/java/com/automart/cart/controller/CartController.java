package com.automart.cart.controller;

import com.automart.cart.dto.CartResponseDto;
import com.automart.cart.service.CartService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @ApiOperation("장바구니 조회")
    @GetMapping("/list")
    public ResponseEntity<List<CartResponseDto>> showUserCart(@RequestParam(value = "userNo") int userNo) {
        List<CartResponseDto> cartResponseDtos = cartService.showUserCarts(userNo);
        return ResponseEntity.status(HttpStatus.OK).body(cartResponseDtos);
    }

    @ApiOperation("장바구니 상품 개수 증가")
    @PutMapping("/add/{userNo}/{productNo}")
    public ResponseEntity<Void> addProductCount(@PathVariable int userNo, @PathVariable int productNo) {
        cartService.addProductToCart(userNo, productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("장바구니 상품 개수 감소")
    @PutMapping("/sub/{userNo}/{productNo}")
    public ResponseEntity<Void> subProductCount(@PathVariable int userNo, @PathVariable int productNo) {
        cartService.subtractProduct(userNo, productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("장바구니 상품 삭제")
    @DeleteMapping("/delete/{userNo}/{productNo}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int userNo, @PathVariable int productNo) {
        cartService.takeProductOutOfCart(userNo, productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
