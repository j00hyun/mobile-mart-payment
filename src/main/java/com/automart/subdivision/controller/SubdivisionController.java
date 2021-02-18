package com.automart.subdivision.controller;

import com.automart.subdivision.dto.SubdivisionRemoveRequestDto;
import com.automart.subdivision.dto.SubdivisionSaveRequestDto;
import com.automart.subdivision.dto.SubdivisionUpdateRequestDto;
import com.automart.subdivision.service.SubdivisionService;
import com.automart.user.dto.AuthResponseDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"4. Subdivision Management"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/subdivisions")
public class SubdivisionController {

    private final SubdivisionService subdivisionService;

    @ApiOperation(value = "소분류 등록", notes = "새로운 소분류를 등록한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 소분류가 추가되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "관리자만 접근 가능 혹은 동일한 데이터가 존재합니다."),
            @ApiResponse(code = 404, message = "해당 카테고리가 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<Void> saveCategory(@ApiParam("생성할 소분류의 고유 이름과 속한 카테고리 고유번호") @Valid @RequestBody SubdivisionSaveRequestDto requestDto) {
        subdivisionService.saveSubdivision(requestDto.getName(), requestDto.getCategoryNo());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "소분류 이름 수정", notes = "소분류 이름을 수정한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 소분류명이 수정되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "관리자만 접근 가능"),
            @ApiResponse(code = 404, message = "해당 소분류가 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{subdivNo}")
    public ResponseEntity<Void> updateCategory(@PathVariable int subdivNo,
                                               @ApiParam("수정할 소분류 이름") @Valid @RequestBody SubdivisionUpdateRequestDto requestDto) {
        subdivisionService.updateSubdivision(subdivNo, requestDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "소분류 제거", notes = "소분류를 삭제한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 204, message = "정상적으로 소분류가 삭제되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "관리자만 접근 가능"),
            @ApiResponse(code = 404, message = "해당 소분류가 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<Void> removeCategory(@ApiParam("삭제할 소분류의 고유 번호") @Valid @RequestBody SubdivisionRemoveRequestDto requestDto) {
        subdivisionService.deleteSubdivision(requestDto.getNo());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

