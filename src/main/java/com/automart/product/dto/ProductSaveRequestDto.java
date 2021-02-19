package com.automart.product.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Data
public class ProductSaveRequestDto {

    @PositiveOrZero(message = "카테고리의 고유번호를 입력해주세요.")
    private int categoryNo; // 카테고리 고유 번호

    @PositiveOrZero(message = "소분류의 고유번호를 입력해주세요.")
    private int subdivNo; // 소분류 고유 번호

    @NotBlank(message = "상품명을 입력해주세요.")
    private String name; // 상품명

    @PositiveOrZero(message = "상품의 개수는 0개부터입니다.")
    private int stock; // 남은 수량

    @Min(value = 1, message = "최소 수량은 1개까지 가능합니다.")
    @Max(value = 99, message = "최대 수량은 99개까지 가능합니다.")
    private int minStock; // 자동주문 수량

    private String receivingDate; // 마지막 입고 날짜
    private String location; // 상품 위치
    private int price; // 판매가

    @PositiveOrZero(message = "구매가를 입력해야합니다.")
    private int cost; // 구매가

    @Positive(message = "바코드번호를 입력해주세요.")
    private int code; // 제품 바코드 번호
    private MultipartFile img; // 제품 이미지

    @Builder
    public ProductSaveRequestDto(int categoryNo, int subdivNo, String name, int stock, int minStock, String receivingDate, String location, int price, int cost, int code, MultipartFile img) {
        this.categoryNo = categoryNo;
        this.subdivNo = subdivNo;
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
