package com.automart.order.resolver;

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
    public List<TotalDailySalesResponseDto> showTotalDailySalesByCategory(String categoryCode) {
        return orderRepository.findTotalDailySalesByCategory(categoryCode);
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
    public List<TotalDailyMarginResponseDto> showTotalDailyMarginByCategory(String categoryCode) {
        return orderRepository.findTotalDailyMarginByCategory(categoryCode);
    }
}
