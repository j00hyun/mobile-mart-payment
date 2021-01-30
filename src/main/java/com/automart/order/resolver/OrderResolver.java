package com.automart.order.resolver;

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

}
