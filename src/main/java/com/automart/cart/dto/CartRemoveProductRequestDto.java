package com.automart.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CartRemoveProductRequestDto {

    private int productNo;

    public CartRemoveProductRequestDto() { }

    @Builder
    public CartRemoveProductRequestDto(int productNo) {
        this.productNo = productNo;
    }
}

