package com.automart.category.repository;

import com.automart.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // 주문 고유번호로 주문 조회하기(단건)
    @Query("select c from Category c where c.no =:no")
    public Category findByNo(@Param("no") Integer no);
}
