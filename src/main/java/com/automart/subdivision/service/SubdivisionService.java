package com.automart.subdivision.service;

import com.automart.advice.exception.NotFoundDataException;
import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import com.automart.subdivision.domain.Subdivision;
import com.automart.subdivision.repository.SubdivisionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubdivisionService {

    private final CategoryRepository categoryRepository;
    private final SubdivisionRepository subdivisionRepository;

    /**
     * 소분류 생성
     * @param name : 생성할 소분류 이름
     * @param categoryNo : 소분류를 위치시킬 카테고리 고유 번호
     */
    public void saveSubdivision(String name, int categoryNo) throws NotFoundDataException{

        Category category = categoryRepository.findByNo(categoryNo)
                .orElseThrow(() -> new NotFoundDataException("해당 카테고리가 존재하지 않습니다."));

        subdivisionRepository.save(
                Subdivision.builder()
                .category(category)
                .name(name)
                .build()
        );
    }

    /**
     * 소분류 이름 수정
     * @param no : 수정하려는 소분류 고유 번호
     * @param name : 변경하려는 이름
     * @return : 변경된 소분류
     */
    public Subdivision updateSubdivision(int no, String name) throws NotFoundDataException{

        Subdivision subdivision = subdivisionRepository.findByNo(no)
                .orElseThrow(() -> new NotFoundDataException("해당 소분류가 존재하지 않습니다."));

        subdivision.setName(name);
        subdivisionRepository.save(subdivision);
        return subdivision;
    }

    /**
     * 소분류 삭제
     * @param no : 삭제할 소분류 고유 번호
     */
    public void deleteSubdivision(int no) throws NotFoundDataException{
        Subdivision subdivision = subdivisionRepository.findByNo(no)
                .orElseThrow(() -> new NotFoundDataException("해당 소분류가 존재하지 않습니다."));
        categoryRepository.deleteByNo(no);
    }

}
