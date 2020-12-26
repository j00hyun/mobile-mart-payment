package com.automart.cart.repository;

import com.automart.cart.domain.Cart;
import com.automart.product.domain.Product;
import com.automart.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    // 장바구니 고유번호로 장바구니 조회하기(단건)
    public Optional<Cart> findByNo(Integer no);

    public Optional<Cart> findByUserAndProduct(User user, Product product);

    public List<Cart> findAllByUser(User user);
}
