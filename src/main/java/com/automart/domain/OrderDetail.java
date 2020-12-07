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

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_detail_no")
    private int no; // 주문 세부정보 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no", nullable = false)
    private Order order; // 주문 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no", nullable = false)
    private Product product; // 구매 제품 고유번호

    @Column(name = "ord_detail_count", columnDefinition = "integer default 1")
    private int count; // 구매 수량

    @Column(name = "ord_detail_price")
    private int price; // 수량 포함 제품 가격

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalPrice() {
        return price*count;
    }

    public void cancel() {
        product.addStock(count);
    }

    /**
     * 주문 상품에 대한 정보 생성
     */
    public static OrderDetail createOrderDetail(Order order, Product product, int count, int price) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setCount(count);
        orderDetail.setPrice(price);

        product.removeStock(count); // 주문상품의 개수만큼 상품현황에서 감소시킴
        return orderDetail;
    }

}
