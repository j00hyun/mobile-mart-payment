package com.automart.cart.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CartAddProductRequestDto {

    @NotBlank(message = "상품 고유 번호를 입력해주세요.")
    private int productCode;

    public CartAddProductRequestDto() { }

    @Builder
    public CartAddProductRequestDto(int productCode) {
        this.productCode = productCode;
    }
}

