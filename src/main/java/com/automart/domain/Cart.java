package com.automart.domain;

import com.automart.exception.NotEnoughStockException;
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

    @Column(name = "cart_count", columnDefinition = "integer default 1")
    private int count; // 담은 상품 수량

    @Column(name = "cart_price")
    private int price; // 수량 포함 제품 가격

    public void setUser(User user) {
        this.user = user;
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

    /**
     * 카트에 물건 담기
     */
    public Cart createCart(User user, Product product) {
        Cart cart = new Cart();
        cart.setUser(user);

        // 재고가 존재해야만 가능
        if(product.getStock() < 1) {
            throw new NotEnoughStockException("재고 부족으로 인해 상품을 담을 수 없습니다.");
        }

        cart.setProduct(product);
        cart.setCount(1);
        cart.setPrice(product.getPrice());

        return cart;
    }

    /**
     * 카트에 담긴 물건 수량 증가
     */
    public void addCart() {
        // 재고가 존재해야만 가능
        if(this.getProduct().getStock() < 1) {
            throw new NotEnoughStockException("재고 부족으로 인해 상품을 담을 수 없습니다.");
        }
        this.count ++;
        this.price += this.getProduct().getPrice();
    }

    /**
     * 카트에 담긴 물건 수량 감소
     */
    public void subtractCart() {
        // 현재 카트에 담긴 물건의 수량이 2개 이상일 경우에만 감소 가능
        if(this.getCount() > 1) {
            this.count --;
            this.price -= this.getProduct().getPrice();
        }
    }

}
