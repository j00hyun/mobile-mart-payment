package com.automart.repository;

import com.automart.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 주문 고유번호로 주문 조회하기(단건)
    @Query("select o from Order o where o.no =:no")
    public Optional<Order> findByNo(@Param("no") Integer no);
}
