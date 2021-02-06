package com.automart.product.repository;

import com.automart.category.domain.Category;
import com.automart.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 상품 고유번호로 상품 조회하기(단건)
    public Optional<Product> findByNo(Integer no);

    // 상품 바코드로 상품 조회하기
    public Optional<Product> findByCode(int code);

    // 특정 카테고리의 상품목록 조회하기
    public List<Product> findAllByCategory(Category category);

}
