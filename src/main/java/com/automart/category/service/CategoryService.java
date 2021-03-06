package com.automart.category.service;

import com.automart.advice.exception.DuplicateDataException;
import com.automart.advice.exception.NotFoundDataException;
import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 생성
     * @param name : 생성할 카테고리 이름
     * @return : 생성된 카테고리
     */
    public Category saveCategory(String name) throws DuplicateDataException {

        if(categoryRepository.findByName(name).isPresent()) {
            throw new DuplicateDataException("동일한 카테고리명이 존재합니다.");
        }

        categoryRepository.save(
                Category.builder()
                .name(name)
                .build())
        ;

        return categoryRepository.findByName(name).get();
    }

    /**
     *
     * @param no : 수정하려는 카테고리 고유 번호
     * @param name : 변경하려는 이름
     * @return : 변경된 카테고리
     */
    public Category updateCategory(int no, String name) throws NotFoundDataException{

        Category category = categoryRepository.findByNo(no)
                .orElseThrow(() -> new NotFoundDataException("해당 카테고리가 존재하지 않습니다."));

        category.setName(name);
        categoryRepository.save(category);
        return category;
    }

    /**
     * 카테고리 삭제
     * @param no : 삭제할 카테고리 고유 번호
     */
    public void deleteCategory(int no) throws NotFoundDataException{
        Category category = categoryRepository.findByNo(no)
                .orElseThrow(() -> new NotFoundDataException("해당 카테고리가 존재하지 않습니다."));
        categoryRepository.deleteByNo(no);
    }

}
