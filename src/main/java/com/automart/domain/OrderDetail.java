package com.automart.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail {

    @Id @GeneratedValue
    @Column(name = "order_detail_no")
    private int no; // 주문 세부정보 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no")
    private Order order; // 주문 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    private Product product; // 구매 제품 고유번호

    @Column(name = "ord_detail_count")
    private int count; // 구매 수량

    @Column(name = "ord_detail_price")
    private int price; // 수량 포함 제품 가격

}
