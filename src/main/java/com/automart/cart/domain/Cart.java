package com.automart.cart.domain;


import com.automart.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_no")
    private int no; // 장바구니 고유번호

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user; // 고객 고유번호

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    @CreatedDate
    @Column(name = "cart_date")
    private LocalDateTime cartDate; // 장바구니 생성 날짜


    public void setUser(User user) {
        this.user = user;
        user.setCart(this);
    }

    // 장바구니 생성
    public static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);

        return cart;
    }

    // 해당 유저의 장바구니중 특정 제품에 대한 연관관계를 제거
    public void removeCartItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
    }

    // 장바구니 삭제
    public void clear() {
        this.user = null;
    }

    /**
     * 장바구니 총 금액 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getPrice();
        }
        return totalPrice;
    }
}
