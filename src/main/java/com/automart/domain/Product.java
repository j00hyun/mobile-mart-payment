package com.automart.domain;

import com.automart.exception.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_no")
    private int no; // 제품 고유번호

    // 제품이 품절되거나 사라지면 카트에 들어있던 동일 제품도 사라져야한다
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Cart> carts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_no")
    private Category category; // 카테고리 고유번호

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(name = "product_name", length = 45)
    private String name; // 제품 이름

    @Column(name = "product_price")
    private int price; // 제품 판매가

    @Column(name = "product_cost")
    private int cost; // 제품 원가

    @Column(name = "product_stock")
    private int stock; // 제품 재고

    @Column(name = "product_code")
    private int code; // 제품 바코드 번호

    @Column(name = "product_img_url", length = 100)
    private String imgUrl; // 제품 이미지 저장 주소

    @Column(name = "product_location", length = 45)
    private String location; // 제품 진열 위치

    public void setCategory(Category category) {
        this.category = category;
    }

    public void removeStock(int count) {
        int restStock = this.stock - count;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stock = restStock;
    }

    public void addStock(int count) {
        this.stock += count;
    }

    @Builder
    public Product(List<Cart> carts, Category category, List<OrderDetail> orderDetails, String name, int price, int cost, int stock, int code, String imgUrl, String location) {
        this.carts = carts;
        this.category = category;
        this.orderDetails = orderDetails;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.stock = stock;
        this.code = code;
        this.imgUrl = imgUrl;
        this.location = location;
    }

    /**
     * 제품 생성
     */
    public Product createProduct(Category category, String name, int price, int cost, int stock, int code, String imgUrl, String location) {
        Product product = Product.builder()
                .category(category)
                .name(name)
                .price(price)
                .cost(cost)
                .stock(stock)
                .code(code)
                .imgUrl(imgUrl)
                .location(location).build();

        category.addProduct(product);

        return product;
    }
}
