package com.automart.product.resolver;

import com.automart.advice.exception.NotFoundDataException;
import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import com.automart.product.domain.Product;
import com.automart.product.dto.ProductResponseDto;
import com.automart.product.repository.ProductRepository;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@GraphQLApi
@RequiredArgsConstructor
public class ProductResolver {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 상품 단건 조회하기
     * @param productNo : 조회할 상품의 번호
     * @return 주문 단건에 대한 정보를 담은 Dto를 반환
     */
    @GraphQLQuery(name = "showProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDto showProduct(Integer productNo) {
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(()->new NotFoundDataException("상품이 존재하지 않습니다."));
        return ProductResponseDto.of(product);
    }

    /**
     * 해당 카테고리의 상품 조회하기
     * @param categoryCode : 조회할 카테고리 고유 코드
     * @return 전체 상품에 대한 정보를 담은 Dto를 반환
     */
    @GraphQLQuery(name = "showProducts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductResponseDto> showProducts(String categoryCode) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new NotFoundDataException("해당 카테고리가 존재하지 않습니다."));
        List<Product> products = productRepository.findAllByCategory(category);
        return ProductResponseDto.listOf(products);
    }
}
