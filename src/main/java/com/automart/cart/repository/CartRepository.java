package com.automart.cart.repository;

import com.automart.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    // 장바구니 고유번호로 장바구니 조회하기(단건)
    @Query(value = "select c from Cart c where c.no = :no")
    public Cart findByNo(@Param("no") Integer no);
}
