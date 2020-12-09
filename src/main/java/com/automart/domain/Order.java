package com.automart.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`order`") // 예약어 충돌 문제에 의해 백틱 삽입
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_no")
    private int no; // 주문 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user; // 고객 고유번호

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @CreatedDate
    @Column(name = "order_date")
    private LocalDateTime orderDate; // 주문 날짜

    @Convert(converter = OrderStateAttributeConverter.class)
    @Column(name = "order_state", length = 45, nullable = false)
    private String state; // 주문 상태 [ORDER, CANCLE]

    public void setUser(User user) {
        this.user = user;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    /**
     * 주문 생성
     */
    public static Order createOrder(User user, List<OrderDetail> orderDetails){
        Order order = new Order(); // 주문생성
        order.setUser(user); // 주문유저
        for (OrderDetail orderDetail : orderDetails) { // 주문정보들
            order.addOrderDetail(orderDetail);
        }
        order.setState("ORDER"); // 주문상태

        return order;
    }

    /**
     * 주문 취소
     */
    public void cancel(){
        setState("CANCEL"); // 주문취소

        for (OrderDetail orderDetail : orderDetails) { // Details에서도 취소
            orderDetail.cancel();
        }
        // 환불에 대한 코드가 필요(?)
    }

    /**
     * 주문 금액 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderDetail orderDetail : orderDetails) {
            totalPrice += orderDetail.getPrice();
        }
        return totalPrice;
    }
}
