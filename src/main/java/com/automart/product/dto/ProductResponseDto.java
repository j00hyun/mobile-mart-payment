package com.automart.product.dto;

import com.automart.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponseDto {

    private int productNo; // 제품 고유번호
    private String name; // 제품 이름
    private int stock; // 제품 남은 수량
    private int minStock; // 자동주문 겟수
    private String receivingDate; // 제품 마지막 입고 날짜
    private String location; // 제품 진열 위치
    private int cost; // 제품 원가 (구매가)
    private int price; // 제품 판매가


    @Builder
    public ProductResponseDto(int productNo, String name, int stock, int minStock, String receivingDate, String location, int cost, int price) {
        this.productNo = productNo;
        this.name = name;
        this.stock = stock;
        this.minStock = minStock;
        this.receivingDate = receivingDate;
        this.location = location;
        this.cost = cost;
        this.price = price;
    }


    public static ProductResponseDto of(Product product) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String stringDate = dateFormat.format(product.getReceivingDate());

        return ProductResponseDto.builder()
                .productNo(product.getNo())
                .name(product.getName())
                .stock(product.getStock())
                .minStock(product.getMinStock())
                .receivingDate(stringDate)
                .location(product.getLocation())
                .cost(product.getCost())
                .price(product.getPrice())
                .build();
    }


    public static List<ProductResponseDto> listOf(List<Product> products) {
        return products.stream().map(ProductResponseDto::of)
                .collect(Collectors.toList());
    }
}
