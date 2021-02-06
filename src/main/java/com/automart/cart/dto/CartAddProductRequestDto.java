package com.automart.cart.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class CartAddProductRequestDto {

    @Positive(message = "바코드번호를 입력해주세요.")
    private int productCode; // 제품 바코드 번호

    public CartAddProductRequestDto() { }

    @Builder
    public CartAddProductRequestDto(int productCode) {
        this.productCode = productCode;
    }
}

