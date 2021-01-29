package com.automart.order.repository;

import com.automart.order.domain.Order;
import com.automart.order.dto.TotalDailySalesResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 주문 고유번호로 주문 조회하기(단건)
    public Optional<Order> findByNo(Integer no);

    // 일별 매출 (전체)
    @Query(value="SELECT DATE(o.order_date) AS date, SUM(od.price) AS price" +
            " FROM automart.order o" +
                " LEFT OUTER JOIN (" +
                    " SELECT order_no, SUM(ord_detail_price) AS price" +
                    " FROM automart.order_detail" +
                    " GROUP BY order_no" +
                " ) od" +
                " ON o.order_no = od.order_no" +
            " WHERE o.order_state = 1" +
            " GROUP BY o.order_date" +
            " ORDER BY o.order_date;", nativeQuery = true)
    public List<TotalDailySalesResponseDto> findTotalDailySales();

}
