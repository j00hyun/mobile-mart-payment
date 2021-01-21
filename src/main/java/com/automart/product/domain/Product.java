package com.automart.product.domain;

import com.automart.cart.domain.Cart;
import com.automart.category.domain.Category;
import com.automart.advice.exception.NotEnoughStockException;
import com.automart.order.domain.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_no")
    private int no; // 제품 고유번호

    // 제품이 품절되거나 사라지면 카트에 들어있던 동일 제품도 사라져야한다
    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Cart> carts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_no")
    private Category category; // 카테고리 고유번호

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(name = "product_name", length = 45, unique = true)
    private String name; // 제품 이름

    @Column(name = "product_price")
    private int price; // 제품 판매가

    @Column(name = "product_cost")
    private int cost; // 제품 원가

    @Column(name = "product_stock")
    private int stock; // 제품 재고

    @Column(name = "product_min_stock")
    private int minStock; // 자동주문을 진행할 남은 수량

    @Temporal(TemporalType.DATE)
    @Column(name = "product_receive_date")
    private Date receivingDate; // 마지막 입고 날짜

    @Column(name = "product_code")
    private int code; // 제품 바코드 번호

    @Column(name = "product_img_url", length = 100)
    private String imgUrl; // 제품 이미지 저장 주소

    @Column(name = "product_location", length = 45)
    private String location; // 제품 진열 위치

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
    public Product(Category category, String name, int price, int cost, int stock, int minStock, Date receivingDate, int code, String imgUrl, String location) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.stock = stock;
        this.minStock = minStock;
        this.receivingDate = receivingDate;
        this.code = code;
        this.imgUrl = imgUrl;
        this.location = location;

        category.getProducts().add(this); // 양방향 연관관계 설정
    }

    /**
     * 제품 생성
     */
    public static Product createProduct(Category category, String name, int price, int cost, int stock, int minStock, String stringDate, int code, String location) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date receivingDate = dateFormat.parse(stringDate);

        Product product = Product.builder()
                .category(category)
                .name(name)
                .price(price)
                .cost(cost)
                .stock(stock)
                .minStock(minStock)
                .receivingDate(receivingDate)
                .code(code)
                .location(location)
                .build();

        return product;
    }

    /**
     * 제품 수정
     */
    public Product update(String name, int price, int cost, int stock, int minStock, String stringDate, int code, String location) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date receivingDate = dateFormat.parse(stringDate);

        if (this.category != null) { // 양방향 연관관계를 다시 생성하기위해 기존의 관계를 제거
            this.category.getProducts().remove(this);
        }

        this.name = name;
        this.price = price;
        this.cost = cost;
        this.stock = stock;
        this.minStock = minStock;
        this.receivingDate = receivingDate;
        this.code = code;
        this.location = location;

        category.getProducts().add(this); // 양방향 연관관계 설정

        return this;
    }

    /**
     * 해당 제품의 카트 삭제
     */
    public void removeCart(Cart cart) {
        this.carts.remove(cart);
    }

    /**
     * 해당 제품의 imgUrl 수정
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
