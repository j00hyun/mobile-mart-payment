package com.automart.product.controller;

import com.automart.product.service.ProductService;
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
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @ApiOperation("상품 등록")
    @PostMapping("/register")
    public ResponseEntity<ProductResponseDto> saveProduct(@RequestBody @Valid ProductSaveRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.saveProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDto);
    }

    @ApiOperation("상품 수정")
    @PutMapping("/edit/{productNo}")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody @Valid ProductUpdateRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.updateProduct(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiOperation("상품 제거")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> removeProduct(@RequestParam(value="productNo") int productNo) {
        productService.removeProduct(productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation("상품 상세 조회")
    @GetMapping("/detail/{productNo}")
    public ResponseEntity<ProductResponseDto> showProduct(@PathVariable int productNo) {
        ProductResponseDto productResponseDto = productService.showProduct(productNo);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiOperation("상품 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<List<ProductResponseDto>> showProducts() {
        List<ProductResponseDto> productResponseDtos = productService.showProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDtos);
    }
}
