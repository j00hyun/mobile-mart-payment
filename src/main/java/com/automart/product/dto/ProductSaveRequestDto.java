package com.automart.product.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductSaveRequestDto {

    private int categoryNo; // 카테고리 고유번호
    private String name; // 상품명
    private int stock; // 남은 수량
    private int minStock; // 자동주문 수량
    private String receivingDate; // 마지막 입고 날짜
    private String location; // 상품 위치
    private int price; // 판매가
    private int cost; // 구매가
    private int code; // 제품 바코드 번호
    private MultipartFile img; // 제품 이미지

    @Builder
    public ProductSaveRequestDto(int categoryNo, String name, int stock, int minStock, String receivingDate, String location, int price, int cost, int code, MultipartFile img) {
        this.categoryNo = categoryNo;
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
    // To do : @NotEmpty등으로 @Valid 설정가능 (Entity에서 적용된 부분과 동일한 부분을 설정할것
}
