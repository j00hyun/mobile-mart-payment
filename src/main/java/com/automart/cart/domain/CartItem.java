package com.automart.cart.domain;

import com.automart.product.domain.Product;
import com.automart.user.domain.User;
import com.automart.advice.exception.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cart_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_no")
    private int no; // 상세 장바구니 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_no", nullable = false)
    private Cart cart; // 장바구니 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no", nullable = false)
    private Product product; // 상품 고유번호

    @Column(name = "cart_item_count", columnDefinition = "integer default 1")
    private int count; // 담은 상품 수량

    @Column(name = "cart_item_price")
    private int price; // 수량 포함 제품 가격

    public void setCart(Cart cart) {
        this.cart = cart;
        cart.getCartItems().add(this);
    }

    public void setProduct(Product product) {
        this.product = product;
        product.getCartItems().add(this);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * 장바구니에 새로운 상품 담기
     */
    public static CartItem createCartItem(User user, Product product) throws NotEnoughStockException {
        CartItem cartItem = new CartItem();

        // 재고가 존재해야만 가능
        if(product.getStock() < 1) {
            throw new NotEnoughStockException("재고 부족으로 인해 상품을 담을 수 없습니다.");
        }

        cartItem.setCart(user.getCart());
        cartItem.setProduct(product);
        cartItem.setCount(1);
        cartItem.setPrice(product.getPrice());

        return cartItem;
    }

    /**
     * 카트에 담긴 물건 수량 증가
     */
    public void addCartItem() throws NotEnoughStockException {
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
    public void subtractCartItem() {
        // 현재 카트에 담긴 물건의 수량이 2개 이상일 경우에만 감소 가능
        if(this.getCount() > 1) {
            this.count --;
            this.price -= this.getProduct().getPrice();
        }
    }

    /**
     * 카트에 담긴 물건 삭제
     */
    public void removeCartItem() {
        this.cart.removeCartItem(this);
        this.product.removeCartItem(this);
        this.cart = null;
        this.product = null;
    }

}
