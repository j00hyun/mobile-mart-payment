package com.automart.service;

import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import com.automart.category.service.CategoryService;
import javassist.NotFoundException;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void 카테코리생성() {
        // when
        Category category1 = categoryService.saveCategory("과일");
        Category category2 = categoryService.saveCategory("채소");

        // then
        assertEquals("카테고리 고유코드가 올바로 저장되어야 한다.", 1, category1.getNo());
        assertEquals("카테고리 고유코드가 올바로 저장되어야 한다.", 2, category2.getNo());
    }

    @Test
    public void 카테고리이름변경() {
        // given
        categoryService.saveCategory("과일");

        // when
        categoryService.updateCategory(1, "채소");

        // then
        assertEquals("카테고리 이름이 채소여야한다.", "채소", categoryRepository.findByNo(1).get().getName());
    }

    @Test
    public void 카테고리삭제() {
        // given
        categoryService.saveCategory("과일");
        categoryService.saveCategory("채소");

        // when
        categoryService.deleteCategory(1);
        categoryService.deleteCategory(2);

        // then
        assertEquals("해당 카테고리가 존재하지 않는다.", false, categoryRepository.findByName("과일").isPresent());
        assertEquals("해당 카테고리가 존재하지 않는다.", false, categoryRepository.findByName("채소").isPresent());
    }

}