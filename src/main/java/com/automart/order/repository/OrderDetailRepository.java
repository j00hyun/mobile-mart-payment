package com.automart.order.repository;

import com.automart.order.domain.Order;
import com.automart.order.domain.OrderDetail;
import com.automart.order.dto.TotalDailySalesResponseDto;
import com.automart.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    // 주문에서 특정 상품을 가진 주문정보 조회
    public Optional<OrderDetail> findByOrderAndProduct(Order order, Product product);
}
