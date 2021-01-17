package com.automart.product.service;

import com.automart.category.domain.Category;
import com.automart.advice.exception.ForbiddenMakeProductException;
import com.automart.product.domain.Product;
import com.automart.category.repository.CategoryRepository;
import com.automart.product.dto.ProductSaveRequestDto;
import com.automart.product.repository.ProductRepository;
import com.automart.product.dto.ProductResponseDto;
import com.automart.product.dto.ProductUpdateRequestDto;
import graphql.ExecutionResult;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

@Service
@GraphQLApi
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 상품 등록하기
     * @param requestDto : 등록할 상품에 대한 정보를 갖고있는 Dto
     * @return 등록한 상품 식별자
     */
    @GraphQLMutation(name = "saveProduct")
    @Transactional
    public ProductResponseDto saveProduct(@GraphQLContext ProductSaveRequestDto requestDto) throws ParseException {
        Category category = categoryRepository.findByNo(requestDto.getCategoryNo())
                .orElseThrow(() -> new ForbiddenMakeProductException("해당 카테고리가 존재하지 않습니다."));
        Product product = Product.createProduct(category, requestDto.getName(),
                requestDto.getPrice(),requestDto.getCost(),requestDto.getStock(),
                requestDto.getMinStock(), requestDto.getReceivingDate(), requestDto.getCode(),requestDto.getLocation());
        productRepository.save(product);
        return ProductResponseDto.of(product);
    }

    /**
     * 상품 수정하기
     * @param requestDto : 상품 수정 정보를 갖고있는 Dto
     * @return 수정된 상품에 대한 Dto
     */

    @Transactional
    public ProductResponseDto updateProduct(ProductUpdateRequestDto requestDto) {
        Category category = categoryRepository.findByNo(requestDto.getCategoryNo()).get();
        Product product = productRepository.findByNo(requestDto.getProductNo())
                .orElseThrow(()->new IllegalStateException("상품이 존재하지 않습니다."));
        product = product.update(category, requestDto.getName(), requestDto.getPrice(),
                requestDto.getCost(), requestDto.getStock(), requestDto.getCode(), requestDto.getImgUrl(),
                requestDto.getLocation());
        return ProductResponseDto.of(product);
    }

    /**
     * 상품 제거하기
     * @param productNo : 제거할 상품에 대한 상품 고유번호
     */

    @Transactional
    public void removeProduct(Integer productNo) {
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(()->new IllegalStateException("상품이 존재하지 않습니다."));
        productRepository.delete(product);
    }


    /**
     * 상품 단건 조회하기
     * @param productNo : 조회할 상품의 번호
     * @return 주문 단건에 대한 정보를 담은 Dto를 반환
     */
    @GraphQLQuery(name = "showProduct")
    public ProductResponseDto showProduct(Integer productNo) {
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(()->new IllegalStateException("상품이 존재하지 않습니다."));
        return ProductResponseDto.of(product);
    }

    /**
     * 해당 카테고리의 상품 조회하기
     * @param categoryNo : 조회할 카테고리 고유번호
     * @return 전체 상품에 대한 정보를 담은 Dto를 반환
     */
    @GraphQLQuery(name = "showProducts")
    public List<ProductResponseDto> showProducts(int categoryNo) {
        Category category = categoryRepository.findByNo(categoryNo)
                .orElseThrow(() -> new ForbiddenMakeProductException("해당 카테고리가 존재하지 않습니다."));
        List<Product> products = productRepository.findAllByCategory(category);
        return ProductResponseDto.listOf(products);
    }

}
