package com.automart.category.controller;

import com.automart.category.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation("카테고리 등록")
    @PostMapping(value = "/register", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<Void> saveCategory(@RequestBody @Valid String categoryName) {
        categoryService.saveCategory(categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @ApiOperation("카테고리 이름 수정")
//    @PutMapping(value = "/edit/{categoryNo}/{categoryName}", produces = "text/plain;charset=UTF-8")
//    public ResponseEntity<Void> updateCategory(@PathVariable int categoryNo, @PathVariable String categoryName) {
//        categoryService.updateCategory(categoryNo, categoryName);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @ApiOperation("카테고리 제거")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> removeCategory(@RequestParam(value = "categoryNo") int categoryNo) {
        categoryService.deleteCategory(categoryNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

