package com.automart.product.dto;

import lombok.Builder;
import lombok.Data;


@Data
public class ProductRemoveRequestDto {

    private int productNo; // 제품 고유 번호

    @Builder
    public ProductRemoveRequestDto(int productNo) {
        this.productNo = productNo;
    }

}
