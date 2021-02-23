package com.automart.order.resolver;

import com.automart.order.dto.BestProductResponseDto;
import com.automart.order.dto.TotalDailyMarginResponseDto;
import com.automart.order.dto.TotalDailySalesResponseDto;
import com.automart.order.repository.OrderRepository;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@GraphQLApi
@RequiredArgsConstructor
public class OrderResolver {

    private final OrderRepository orderRepository;

    /**
     * 일별 총 매출액 계산
     * @return 일별 매출액 반환
     */
    @GraphQLQuery(name = "showTotalDailySales")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TotalDailySalesResponseDto> showTotalDailySales() {
        return orderRepository.findTotalDailySales();
    }

    /**
     * 일별 특정 카테고리 매출액 계산
     * @return 해당 카테고리의 일별 매출액 반환
     */
    @GraphQLQuery(name = "showTotalDailySalesByCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TotalDailySalesResponseDto> showTotalDailySalesByCategory(int categoryNo) {
        return orderRepository.findTotalDailySalesByCategory(categoryNo);
    }

    /**
     * 일별 총 순수익 계산
     * @return 일별 순수익 반환
     */
    @GraphQLQuery(name = "showTotalDailyMargin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TotalDailyMarginResponseDto> showTotalDailyMargin() {
        return orderRepository.findTotalDailyMargin();
    }

    /**
     * 일별 특정 카테고리 순수익 계산
     * @return 해당 카테고리의 일별 순수익 반환
     */
    @GraphQLQuery(name = "showTotalDailyMarginByCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TotalDailyMarginResponseDto> showTotalDailyMarginByCategory(int categoryNo) {
        return orderRepository.findTotalDailyMarginByCategory(categoryNo);
    }

    /**
     * 당일 특정 카테고리의 매출 상위 5개 상품 계산
     * @return 해당 카테고리의 당일 상위 매출 5개의 상품 목록과 매출액 반환
     */
    @GraphQLQuery(name = "showDailyBestProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BestProductResponseDto> showDailyBestProduct(int categoryNo) {
        return orderRepository.findDailyBestProductByCategory(categoryNo);
    }

    /**
     * 주간 특정 카테고리의 매출 상위 5개 상품 계산
     * @return 해당 카테고리의 주간 상위 매출 5개의 상품 목록과 매출액 반환
     */
    @GraphQLQuery(name = "showWeeklyBestProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BestProductResponseDto> showWeeklyBestProduct(int categoryNo) {
        return orderRepository.findWeeklyBestProductByCategory(categoryNo);
    }

    /**
     * 월간 특정 카테고리의 매출 상위 5개 상품 계산
     * @return 해당 카테고리의 월간 상위 매출 5개의 상품 목록과 매출액 반환
     */
    @GraphQLQuery(name = "showMonthlyBestProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BestProductResponseDto> showMonthlyBestProduct(int categoryNo) {
        return orderRepository.findMonthlyBestProductByCategory(categoryNo);
    }

    /**
     * 연간 특정 카테고리의 매출 상위 5개 상품 계산
     * @return 해당 카테고리의 연간 상위 매출 5개의 상품 목록과 매출액 반환
     */
    @GraphQLQuery(name = "showAnnualBestProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BestProductResponseDto> showAnnualBestProduct(int categoryNo) {
        return orderRepository.findAnnualBestProductByCategory(categoryNo);
    }
}
