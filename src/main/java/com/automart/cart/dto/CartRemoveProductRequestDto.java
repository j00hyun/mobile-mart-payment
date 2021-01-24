package com.automart.cart.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CartRemoveProductRequestDto {

    @NotBlank(message = "상품 고유 번호를 입력해주세요.")
    private int productNo;

    public CartRemoveProductRequestDto() { }

    @Builder
    public CartRemoveProductRequestDto(int productNo) {
        this.productNo = productNo;
    }
}

