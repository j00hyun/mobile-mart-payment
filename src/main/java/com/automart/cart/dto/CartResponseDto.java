package com.automart.cart.dto;

import com.automart.cart.domain.CartItem;
import com.automart.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CartResponseDto {

    private int productNo; // 제품 고유번호
    private int productCode; // 제품 코드
    private String productName; // 제품 이름
    private int productPrice; // 제품 판매가격
    private String categoryName; // 카테고리 이름
    private int count; // 장바구니에 담은 제픔 수량

    @Builder
    public CartResponseDto(int productNo, int productCode, String productName, int productPrice, String categoryName, int count) {
        this.productNo = productNo;
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.categoryName = categoryName;
        this.count = count;
    }

    public static CartResponseDto of(CartItem cartItem) {
        Product product = cartItem.getProduct();

        return CartResponseDto.builder()
                .productNo(product.getNo())
                .productCode(product.getCode())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .categoryName(product.getCategory().getName())
                .count(cartItem.getCount())
                .build();
    }

    public static List<CartResponseDto> listOf(List<CartItem> carts) {
        return carts.stream().map(CartResponseDto::of)
                .collect(Collectors.toList());
    }
}
