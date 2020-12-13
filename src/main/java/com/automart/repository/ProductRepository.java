package com.automart.repository;

import com.automart.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 상품 고유번호로 상품 조회하기(단건)
    @Query(value = "select p from Product p where p.no =:no")
    public Optional<Product> findByNo(@Param("no") Integer no);


}
