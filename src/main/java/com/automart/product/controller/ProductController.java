package com.automart.product.controller;

import com.automart.product.Service.ProductService;
import com.automart.product.dto.ProductResponseDto;
import com.automart.product.dto.ProductSaveRequestDto;
import com.automart.product.dto.ProductUpdateRequestDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ApiOperation("상품 등록")
    @PostMapping
    public ResponseEntity<ProductResponseDto> saveProduct(@RequestBody @Valid ProductSaveRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.saveProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDto);
    }

    @ApiOperation("상품 수정")
    @PutMapping
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody @Valid ProductUpdateRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.updateProduct(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiOperation("상품 제거")
    @DeleteMapping("/{productNo}")
    public ResponseEntity<Void> removeProduct(@PathVariable int productNo) {
        productService.removeProduct(productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("상품 상세 조회")
    @GetMapping("/{productNo}")
    public ResponseEntity<ProductResponseDto> showProduct(@PathVariable int productNo) {
        ProductResponseDto productResponseDto = productService.showProduct(productNo);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiOperation("상품 목록 조회")
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> showProducts() {
        List<ProductResponseDto> productResponseDtos = productService.showProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDtos);
    }
}
