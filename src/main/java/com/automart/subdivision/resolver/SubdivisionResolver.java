package com.automart.subdivision.resolver;

import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import com.automart.subdivision.dto.SubdivisionResponseDto;
import com.automart.subdivision.repository.SubdivisionRepository;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@GraphQLApi
@RequiredArgsConstructor
public class SubdivisionResolver {

    private final CategoryRepository categoryRepository;
    private final SubdivisionRepository subdivisionRepository;

    /**
     * 카테고리 고유번호에 포함된 소분류 목록 조회
     * @param categoryNo : 조회할 카테고리 고유 번호
     * @return 해당 소분류 정보를 담은 Dto를 반환
     */
    @GraphQLQuery(name = "showSubdivByCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SubdivisionResponseDto> showSubdivByCategory(int categoryNo) {
        Category category = categoryRepository.findByNo(categoryNo)
                .orElseThrow(()->new IllegalStateException("해당 카테고리가 존재하지 않습니다."));

        return SubdivisionResponseDto.listOf(subdivisionRepository.findByCategory(category));
    }

}
