package com.automart.order.repository;

import com.automart.order.domain.Order;
import com.automart.order.dto.BestProductResponseDto;
import com.automart.order.dto.TotalDailyMarginResponseDto;
import com.automart.order.dto.TotalDailySalesResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 주문 고유번호로 주문 조회하기(단건)
    public Optional<Order> findByNo(Integer no);

    /**
     * 일별 매출 (전체)
     *
     * date : 주문이 존재하는 날짜
     * price : 해당 날짜의 총 판매액
     */
    @Query(value="SELECT DATE(o.order_date) AS date, SUM(od.price) AS price" +
                " FROM automart.order o" +
                    " LEFT OUTER JOIN (" +
                        " SELECT order_no, SUM(order_detail_price) AS price" +
                        " FROM automart.order_detail" +
                        " WHERE order_detail_status = 1" +
                        " GROUP BY order_no" +
                    " ) od" +
                    " ON o.order_no = od.order_no" +
                " WHERE o.order_state = 1" +
                " GROUP BY o.order_date" +
                " ORDER BY o.order_date;", nativeQuery = true)
    public List<TotalDailySalesResponseDto> findTotalDailySales();

    /**
     * 일별 매출 (카테고리)
     *
     * date : 주문이 존재하는 날짜
     * price : 해당 날짜에 특정 카테고리 상품들의 총 판매액
     */
    @Query(value="SELECT DATE(o.order_date) AS date, SUM(od.price) AS price" +
                " FROM automart.order o" +
                    " INNER JOIN (" +
                        " SELECT d.order_no, SUM(d.order_detail_price) AS price" +
                        " FROM automart.order_detail d, automart.product p" +
                        " WHERE d.product_no = p.product_no" +
                            " AND d.order_detail_status = 1" +
                            " AND p.category_no = ?1" +
                        " GROUP BY order_no" +
                    " ) od" +
                " ON o.order_no = od.order_no" +
                " WHERE o.order_state = 1" +
                " GROUP BY o.order_date" +
                " ORDER BY o.order_date;", nativeQuery = true)
    public List<TotalDailySalesResponseDto> findTotalDailySalesByCategory(int categoryNo);

    /**
     * 일별 순수익 (전체)
     *
     * date : 주문이 존재하는 날짜
     * margin : 해당 날짜의 총 순수익
     */
    @Query(value="SELECT DATE(o.order_date) AS date, SUM(od.margin) AS margin" +
                " FROM automart.order o" +
                    " LEFT OUTER JOIN (" +
                        " SELECT order_no," +
                            " SUM(d.order_detail_price - p.product_cost * d.order_detail_count) AS margin" +
                        " FROM automart.order_detail d, automart.product p" +
                        " WHERE d.product_no = p.product_no" +
                            " AND d.order_detail_status = 1" +
                        " GROUP BY d.order_no" +
                    " ) od" +
                    " ON o.order_no = od.order_no" +
                " WHERE o.order_state = 1" +
                " GROUP BY o.order_date" +
                " ORDER BY o.order_date;", nativeQuery = true)
    public List<TotalDailyMarginResponseDto> findTotalDailyMargin();

    /**
     * 일별 순수익 (카테고리별)
     *
     * date : 주문이 존재하는 날짜
     * margin : 해당 날짜에 특정 카테고리 상품들의 총 순수익
     */
    @Query(value="SELECT DATE(o.order_date) AS date, SUM(od.margin) AS margin" +
            " FROM automart.order o" +
                " INNER JOIN (" +
                    " SELECT order_no," +
                        " SUM(d.order_detail_price - p.product_cost * d.order_detail_count) AS margin" +
                    " FROM automart.order_detail d, automart.product p" +
                    " WHERE d.product_no = p.product_no" +
                        " AND d.order_detail_status = 1" +
                        " AND p.category_no = ?1" +
                    " GROUP BY d.order_no" +
                " ) od" +
                " ON o.order_no = od.order_no" +
            " WHERE o.order_state = 1" +
            " GROUP BY o.order_date" +
            " ORDER BY o.order_date;", nativeQuery = true)
    public List<TotalDailyMarginResponseDto> findTotalDailyMarginByCategory(int categoryNo);


    /**
     * 당일 베스트 상품 5개 (카테고리별)
     *
     * no : 베스트 상품의 고유 번호
     * subdiv : 상품의 소분류 이름
     * name : 상품 이름
     * price : 상품의 당일 매출
     * beforePrice : 상품의 전일 매출
     */
    @Query(value="SELECT p.product_no AS no, s.subdiv_name AS subdiv, " +
            " p.product_name AS name, SUM(d.order_detail_price) AS price, " +
                " (SELECT SUM(order_detail_price)" +
                    " FROM automart.order_detail, automart.order" +
                    " WHERE no = product_no" +
                        " AND DATE(order_date) = CURDATE() - INTERVAL 1 DAY" +
                        " AND order.order_no = order_detail.order_no" +
                        " AND order_state = 1 " +
                        " AND order_detail_status = 1) AS beforePrice" +
            " FROM ( SELECT order_no" +
                " FROM automart.order" +
                " WHERE DATE(order_date) = CURDATE()" +
                    " AND order_state = 1 ) o," +
                " automart.order_detail d, automart.product p, automart.subdivision s" +
            " WHERE o.order_no = d.order_no" +
                " AND p.product_no = d.product_no" +
                " AND d.order_detail_status = 1" +
                " AND p.category_no = ?1" +
                " AND p.subdiv_no = s.subdiv_no" +
            " GROUP BY p.product_no" +
            " ORDER BY price desc limit 5;", nativeQuery = true)
    public List<BestProductResponseDto> findDailyBestProductByCategory(int categoryNo);

    /**
     * 주간(해당 주의 월요일~일요일) 베스트 상품 5개 (카테고리별)
     *
     * no : 베스트 상품의 고유 번호
     * subdiv : 상품의 소분류 이름
     * name : 상품 이름
     * price : 상품의 당일 매출
     * beforePrice : 상품의 전주 매출
     */
    @Query(value="SELECT p.product_no AS no, s.subdiv_name AS subdiv, " +
            " p.product_name AS name, SUM(d.order_detail_price) AS price, " +
                " (SELECT SUM(order_detail_price)" +
                    " FROM automart.order_detail, automart.order" +
                    " WHERE no = product_no" +
                        " AND DATE(order_date)" +
                            " BETWEEN ADDDATE(CURDATE(), -WEEKDAY(CURDATE()) - 7)" +
                                " AND ADDDATE(CURDATE(), -WEEKDAY(CURDATE()) - 1)" +
                        " AND order.order_no = order_detail.order_no" +
                        " AND order_state = 1" +
                        " AND order_detail_status = 1) AS beforePrice" +
            " FROM ( SELECT order_no" +
                " FROM automart.order" +
                " WHERE DATE(order_date)" +
                    " BETWEEN ADDDATE(CURDATE(), -WEEKDAY(CURDATE()) + 0)" +
                        " AND ADDDATE(CURDATE(), -WEEKDAY(CURDATE()) + 6)" +
                    " AND order_state = 1 ) o," +
                " automart.order_detail d, automart.product p, automart.subdivision s" +
            " WHERE o.order_no = d.order_no" +
                " AND p.product_no = d.product_no" +
                " AND d.order_detail_status = 1" +
                " AND p.category_no = ?1" +
                " AND p.subdiv_no = s.subdiv_no" +
            " GROUP BY p.product_no" +
            " ORDER BY price desc limit 5;", nativeQuery = true)
    public List<BestProductResponseDto> findWeeklyBestProductByCategory(int categoryNo);

    /**
     * 월간(이번달 1일부터 현재까지) 베스트 상품 5개 (카테고리별)
     *
     * no : 베스트 상품의 고유 번호
     * subdiv : 상품의 소분류 이름
     * name : 상품 이름
     * price : 상품의 당월 매출
     * beforePrice : 상품의 전월 매출
     */
    @Query(value="SELECT p.product_no AS no, s.subdiv_name AS subdiv, " +
            " p.product_name AS name, SUM(d.order_detail_price) AS price, " +
            " (SELECT SUM(order_detail_price)" +
            " FROM automart.order_detail, automart.order" +
            " WHERE no = product_no" +
            " AND DATE(order_date) > LAST_DAY(NOW() - INTERVAL 2 MONTH)" +
            " AND DATE(order_date) <= LAST_DAY(NOW() - INTERVAL 1 MONTH)" +
            " AND order.order_no = order_detail.order_no" +
            " AND order_state = 1" +
            " AND order_detail_status = 1) AS beforePrice" +
            " FROM ( SELECT order_no" +
            " FROM automart.order" +
            " WHERE DATE(order_date) > LAST_DAY(NOW() - INTERVAL 1 MONTH)" +
            " AND DATE(order_date) <= NOW()" +
            " AND order_state = 1 ) o," +
            " automart.order_detail d, automart.product p, automart.subdivision s" +
            " WHERE o.order_no = d.order_no" +
            " AND p.product_no = d.product_no" +
            " AND d.order_detail_status = 1" +
            " AND p.category_no = ?1" +
            " AND p.subdiv_no = s.subdiv_no" +
            " GROUP BY p.product_no" +
            " ORDER BY price desc limit 5;", nativeQuery = true)
    public List<BestProductResponseDto> findMonthlyBestProductByCategory(int categoryNo);
}
