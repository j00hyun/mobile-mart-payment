package com.automart.product.controller;

import com.automart.advice.exception.ForbiddenSaveException;
import com.automart.product.dto.ProductResponseDto;
import com.automart.product.dto.ProductSaveRequestDto;
import com.automart.product.dto.ProductUpdateRequestDto;
import com.automart.product.service.ProductService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = {"5. Product Management"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    @ApiOperation(value = "4 상품 추가", notes = "새로운 상품을 등록한다.\n" +
            "swagger에서는 테스트 오류나므로 postman으로 테스트\n" +
            "상품 이미지 크기는 1MB이하여야함\n" +
            "상품 이미지 저장 경로는 products/{categoryNo}/{productNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 201, message = "상품이 정상적으로 등록되었습니다."),
            @ApiResponse(code = 403, message = "상품 등록에 실패하였습니다. (날짜 형식 오류, 이미지 업로드 오류, 카테고리 오류)")
    })
    @PostMapping("")
    public ResponseEntity<ProductResponseDto> saveProduct(@ApiIgnore @RequestHeader("Authorization") String token,
                                                          @Valid @ModelAttribute ProductSaveRequestDto requestDto) throws ForbiddenSaveException {
        ProductResponseDto productResponseDto = productService.saveProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDto);
    }


    @ApiOperation(value = "3-1 상품 수정", notes = "상품을 수정한다.\n" +
            "이미지를 바꿀시 swagger에서는 테스트 오류나므로 postman으로 테스트\n" +
            "상품 이미지 크기는 1MB이하여야함\n" +
            "상품 이미지 저장 경로는 products/{categoryNo}/{productNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "productNo", value = "수정하려는 상품 고유번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품이 정상적으로 수정되었습니다."),
            @ApiResponse(code = 403, message = "상품 수정에 실패하였습니다. (날짜 형식 오류, 이미지 업로드 오류, 제품 고유번호 오류)")
    })
    @PutMapping("/{productNo}")
    public ResponseEntity<ProductResponseDto> updateProduct(@ApiIgnore @RequestHeader("Authorization") String token,
                                                            @PathVariable int productNo,
                                                            @Valid @ModelAttribute ProductUpdateRequestDto requestDto) throws ForbiddenSaveException {
        ProductResponseDto productResponseDto = productService.updateProduct(productNo, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }


    @ApiOperation(value = "상품 제거", notes = "등록된 상품을 삭제한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "productNo", value = "삭제하려는 상품 고유번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 200, message = "상품이 정상적으로 삭제되었습니다."),
            @ApiResponse(code = 403, message = "상품 삭제에 실패하였습니다. (제품 고유번호 오류)")
    })
    @DeleteMapping("/{productNo}")
    public ResponseEntity<Void> removeProduct(@ApiIgnore @RequestHeader("Authorization") String token,
                                              @PathVariable int productNo) {
        productService.removeProduct(productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @ApiOperation("상품 상세 조회")
//    @GetMapping("/detail/{productNo}")
//    public ResponseEntity<ProductResponseDto> showProduct(@PathVariable int productNo) {
//        ProductResponseDto productResponseDto = productService.showProduct(productNo);
//        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
//    }

//    @ApiOperation("상품 목록 조회")
//    @GetMapping("/list")
//    public ResponseEntity<List<ProductResponseDto>> showProducts() {
//        List<ProductResponseDto> productResponseDtos = productService.showProducts();
//        return ResponseEntity.status(HttpStatus.OK).body(productResponseDtos);
//    }
}
