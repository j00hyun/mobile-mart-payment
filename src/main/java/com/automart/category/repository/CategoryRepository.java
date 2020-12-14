package com.automart.category.repository;

import com.automart.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // 카테고리 고유번호로 카테고리 조회하기(단건)
    @Query("select c from Category c where c.no =:no")
    public Optional<Category> findByNo(@Param("no") Integer no);

    // 이름으로 카테고리 조회하기
    public Optional<Category> findByName(String name);
}
