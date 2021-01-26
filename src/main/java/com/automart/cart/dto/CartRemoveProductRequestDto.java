package com.automart.cart.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class CartRemoveProductRequestDto {

    @PositiveOrZero(message = "상품 고유번호를 입력해주세요.")
    private int productNo;

    public CartRemoveProductRequestDto() { }

    @Builder
    public CartRemoveProductRequestDto(int productNo) {
        this.productNo = productNo;
    }
}

