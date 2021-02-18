package com.automart.subdivision.repository;

import com.automart.category.domain.Category;
import com.automart.subdivision.domain.Subdivision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SubdivisionRepository extends JpaRepository<Subdivision, Integer> {

    // 소분류 고유 번호로 소분류 조회하기(단건)
    public Optional<Subdivision> findByNo(int no);

    // 카테고리에 해당하는 소분류 조회하기
    public List<Subdivision> findByCategory(Category category);

    // 고유 번호로 소분류 삭제하기
    public void deleteByNo(int no);
}
