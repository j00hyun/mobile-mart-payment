package com.automart.category.controller;

import com.automart.category.dto.CategoryRemoveRequestDto;
import com.automart.category.dto.CategorySaveRequestDto;
import com.automart.category.dto.CategoryUpdateRequestDto;
import com.automart.category.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation("카테고리 등록")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<Void> saveCategory(@Valid @RequestBody CategorySaveRequestDto requestDto) {
        categoryService.saveCategory(requestDto.getCode(), requestDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("카테고리 이름 수정")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{categoryCode}")
    public ResponseEntity<Void> updateCategory(@PathVariable String categoryCode,
                                               @Valid @RequestBody CategoryUpdateRequestDto requestDto) {
        categoryService.updateCategory(categoryCode, requestDto.getName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("카테고리 제거")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<Void> removeCategory(@RequestBody CategoryRemoveRequestDto categoryRemoveRequestDto) {
        categoryService.deleteCategory(categoryRemoveRequestDto.getCode());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

