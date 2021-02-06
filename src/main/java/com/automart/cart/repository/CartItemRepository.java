package com.automart.cart.repository;

import com.automart.cart.domain.Cart;
import com.automart.cart.domain.CartItem;
import com.automart.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // 장바구니 고유번호로 장바구니 조회하기(단건)
    public Optional<CartItem> findByNo(Integer no);

    // 특정 유저의 장바구니에서 해당 제품 찾기
    public Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // 특정 유저의 장바구니 전체 목록 조회하기
    public List<CartItem> findAllByCart(Cart cart);
}
