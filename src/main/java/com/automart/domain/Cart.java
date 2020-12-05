package com.automart.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_no")
    private int no; // 장바구니 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user; // 고객 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no", nullable = false)
    private Product product; // 상품 고유번호

    @Column(name = "cart_count")
    private int count; // 담은 상품 수량

    @Column(name = "cart_price")
    private int price; // 수량 포함 제품 가격

    //test
    public void setCart(Product product) {
        this.product = product;
    }
}
