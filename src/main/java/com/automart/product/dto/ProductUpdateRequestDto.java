package com.automart.product.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductUpdateRequestDto {

    private int no; // 상품 고유 번호
    private String name; // 상품 이름
    private int stock; // 남은 수량
    private int minStock; // 자동주문 수량
    private String receivingDate; // 마지막 입고 날짜
    private String location; // 상품 위치
    private int price; // 판매가
    private int cost; // 구매가
    private int code; // 제품 바코드 번호
    private MultipartFile img; // 제품 이미지 - null값 허용


    @Builder
    public ProductUpdateRequestDto(int no, String name, int stock, int minStock, String receivingDate, String location, int price, int cost, int code, MultipartFile img) {
        this.no = no;
        this.name = name;
        this.stock = stock;
        this.minStock = minStock;
        this.receivingDate = receivingDate;
        this.location = location;
        this.price = price;
        this.cost = cost;
        this.code = code;
        this.img = img;
    }
}
