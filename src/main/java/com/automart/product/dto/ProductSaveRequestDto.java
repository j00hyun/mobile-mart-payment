package com.automart.product.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.AssociationOverride;

@Data
public class ProductSaveRequestDto {

    private int ProductNo; // 제품 고유번호
    private int categoryNo; // 카테고리 고유번호
    private String name; // 제품 이름
    private int price; // 제품 판매가
    private int cost; // 제품 원가
    private int stock; // 제품 재고
    private int code; // 제품 바코드 번호
    private String imgUrl; // 제품 이미지 저장 주소
    private String location; // 제품 진열 위치

    @Builder
    public ProductSaveRequestDto(int productNo, int categoryNo, String name, int price, int cost, int stock, int code, String imgUrl, String location) {
        ProductNo = productNo;
        this.categoryNo = categoryNo;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.stock = stock;
        this.code = code;
        this.imgUrl = imgUrl;
        this.location = location;
    }
    // To do : @NotEmpty등으로 @Valid 설정가능 (Entity에서 적용된 부분과 동일한 부분을 설정할것
}
