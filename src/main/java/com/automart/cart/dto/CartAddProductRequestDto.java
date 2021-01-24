package com.automart.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CartAddProductRequestDto {

    private int productCode;

    public CartAddProductRequestDto() { }

    @Builder
    public CartAddProductRequestDto(int productCode) {
        this.productCode = productCode;
    }
}

