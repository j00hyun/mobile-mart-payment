package com.automart.cart.repository;

import com.automart.cart.domain.Cart;
import com.automart.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    // 장바구니 고유번호로 장바구니 조회하기(단건)
    public Optional<Cart> findByNo(Integer no);

    // 유저정보로 장바구니 조회하기
    public Optional<Cart> findByUser(User user);

}
