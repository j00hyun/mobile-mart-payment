package com.automart.category.service;

import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import com.automart.exception.ForbiddenMakeCategoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 생성
     * @param name : 생성할 카테고리 이름
     * @return : 생성된 카테고리
     */
    public Category saveCategory(String name) throws ForbiddenMakeCategoryException {
        log.info("카테고리 생성");

        if(categoryRepository.findByName(name).isPresent()) {
            throw new ForbiddenMakeCategoryException("동일한 카테고리명이 존재합니다.");
        }
        int no = categoryRepository.save(Category.builder().name(name).build()).getNo();
        return categoryRepository.findByNo(no).get();
    }

    /**
     * 카테고리 삭제
     * @param no : 삭제할 카테고리 고유번호
     */
    public void deleteCategory(int no) throws IllegalArgumentException {
        log.info("카테고리 삭제");

        categoryRepository.deleteById(no);
    }

}
