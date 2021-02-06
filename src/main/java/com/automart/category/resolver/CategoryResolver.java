package com.automart.category.resolver;

import com.automart.category.domain.Category;
import com.automart.category.dto.CategoryResponseDto;
import com.automart.category.repository.CategoryRepository;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@GraphQLApi
@RequiredArgsConstructor
public class CategoryResolver {

    private final CategoryRepository categoryRepository;

    /**
     * 코드로 카테고리 조회
     * @param categoryCode : 조회할 카테고리 고유 코드
     * @return 해당 카테고리 정보를 담은 Dto를 반환
     */
    @GraphQLQuery(name = "showCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDto showCategory(String categoryCode) {
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(()->new IllegalStateException("상품이 존재하지 않습니다."));
        return CategoryResponseDto.of(category);
    }

    /**
     * 전체 카테고리 목록 조회하기
     * @return 전체 카테고리에 대한 정보를 담은 Dto를 반환
     */
    @GraphQLQuery(name = "showCategories")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponseDto> showCategories() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryResponseDto.listOf(categories);
    }
}
