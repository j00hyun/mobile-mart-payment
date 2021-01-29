package com.automart.category.controller;

import com.automart.category.dto.CategoryRemoveRequestDto;
import com.automart.category.dto.CategorySaveRequestDto;
import com.automart.category.dto.CategoryUpdateRequestDto;
import com.automart.category.service.CategoryService;
import com.automart.user.dto.AuthResponseDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Api(tags = {"4. Category Management"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 등록", notes = "새로운 카테고리를 등록한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 카테고리가 추가되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "관리자만 접근 가능 혹은 동일한 데이터가 존재합니다."),
            @ApiResponse(code = 404, message = "해당 카테고리가 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<Void> saveCategory(@ApiParam("생성할 카테고리의 고유 코드와 이름") @Valid @RequestBody CategorySaveRequestDto requestDto) {
        categoryService.saveCategory(requestDto.getCode(), requestDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "카테고리 이름 수정", notes = "카테고리 이름을 수정한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 카테고리명이 수정되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "관리자만 접근 가능"),
            @ApiResponse(code = 404, message = "해당 카테고리가 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{categoryCode}")
    public ResponseEntity<Void> updateCategory(@PathVariable @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).{0,15}$", message = "카테고리 고유코드(대문자,숫자조합)를 입력해주세요.") String categoryCode,
                                               @ApiParam("수정된 카테고리 이름") @Valid @RequestBody CategoryUpdateRequestDto requestDto) {
        categoryService.updateCategory(categoryCode, requestDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "카테고리 제거", notes = "카테고리를 삭제한다..", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 204, message = "정상적으로 카테고리가 삭제되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "관리자만 접근 가능"),
            @ApiResponse(code = 404, message = "해당 카테고리가 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<Void> removeCategory(@ApiParam("삭제할 카테고리의 고유 코드") @Valid @RequestBody CategoryRemoveRequestDto categoryRemoveRequestDto) {
        categoryService.deleteCategory(categoryRemoveRequestDto.getCode());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

