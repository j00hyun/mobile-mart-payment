package com.automart.cart.dto;

import com.automart.cart.domain.Cart;
import com.automart.cart.domain.CartItem;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CartResponseDto {

    private int cartNo; // 장바구니 번호
    private int totalPrice; // 장바구니 총 금액
    private List<CartItemDto> cartItems; // 장바구니에 담은 아이템

    @Builder
    public CartResponseDto(int cartNo, int totalPrice, List<CartItemDto> cartItems) {
        this.cartNo = cartNo;
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
    }

    public static CartResponseDto of(Cart cart) {

        return CartResponseDto.builder()
                .cartNo(cart.getNo())
                .totalPrice(cart.getTotalPrice())
                .cartItems(cart.getCartItems().stream().map(cartItem -> new CartItemDto(cartItem)).collect(Collectors.toList()))
                .build();
    }

    @Data
    static class CartItemDto {
        private int productNo; // 제품 고유번호
        private int productCode; // 제품 코드
        private String productName; // 제품 이름
        private int productPrice; // 제품 판매가격
        private String categoryName; // 카테고리 이름
        private int count; // 장바구니에 담은 제픔 수량

        public CartItemDto(CartItem cartItem) {
            this.productNo = cartItem.getProduct().getNo();
            this.productCode = cartItem.getProduct().getCode();
            this.productName = cartItem.getProduct().getName();
            this.productPrice = cartItem.getProduct().getPrice();
            this.categoryName = cartItem.getProduct().getCategory().getName();
            this.count = cartItem.getCount();
        }
    }
}
