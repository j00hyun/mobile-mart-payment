package com.automart.product.dto;

import com.automart.product.domain.Product;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponseDto {

    @GraphQLQuery(description = "고유번호")
    private int no; // 제품 고유번호

    @GraphQLQuery(description = "품명")
    private String name; // 제품 이름

    @GraphQLQuery(description = "남은 수량")
    private int stock; // 제품 남은 수량

    @GraphQLQuery(description = "자동주문 갯수")
    private int minStock; // 자동주문 겟수

    @GraphQLQuery(description = "마지막 입고")
    private Date receivingDate; // 제품 마지막 입고 날짜

    @GraphQLQuery(description = "위치")
    private String location; // 제품 진열 위치

    @GraphQLQuery(description = "구매가")
    private int cost; // 제품 원가 (구매가)

    @GraphQLQuery(description = "판매가")
    private int price; // 제품 판매가


    @Builder
    public ProductResponseDto(int no, String name, int stock, int minStock, Date receivingDate, String location, int cost, int price) {
        this.no = no;
        this.name = name;
        this.stock = stock;
        this.minStock = minStock;
        this.receivingDate = receivingDate;
        this.location = location;
        this.cost = cost;
        this.price = price;
    }


    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .no(product.getNo())
                .name(product.getName())
                .stock(product.getStock())
                .minStock(product.getMinStock())
                .receivingDate(product.getReceivingDate())
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
