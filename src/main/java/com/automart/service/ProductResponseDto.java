package com.automart.service;

import com.automart.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponseDto {

    private int productNo; // 제품 고유번호
    private int categoryNo; // 카테고리 고유번호
    private String name; // 제품 이름
    private int price; // 제품 판매가
    private int cost; // 제품 원가
    private int stock; // 제품 재고
    private int code; // 제품 바코드 번호
    private String imgUrl; // 제품 이미지 저장 주소
    private String location; // 제품 진열 위치

    @Builder
    public ProductResponseDto(int productNo, int categoryNo, String name,
                                    int price, int cost, int stock, int code,
                                    String imgUrl, String location) {
        this.productNo = productNo;
        this.categoryNo = categoryNo;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.stock = stock;
        this.code = code;
        this.imgUrl = imgUrl;
        this.location = location;
    }

    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .productNo(product.getNo())
                .name(product.getName())
                .price(product.getPrice())
                .cost(product.getCost())
                .stock(product.getStock())
                .code(product.getCode())
                .imgUrl(product.getImgUrl())
                .location(product.getLocation())
                .build();
    }

    public static List<ProductResponseDto> listOf(List<Product> products) {
        return products.stream().map(ProductResponseDto::of)
                .collect(Collectors.toList());
    }
}
