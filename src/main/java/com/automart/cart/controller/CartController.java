package com.automart.cart.controller;

import com.automart.advice.exception.NotEnoughStockException;
import com.automart.advice.exception.SessionUnstableException;
import com.automart.cart.dto.CartAddProductRequestDto;
import com.automart.cart.dto.CartRemoveProductRequestDto;
import com.automart.cart.dto.CartResponseDto;
import com.automart.cart.service.CartService;
import com.automart.security.UserPrincipal;
import com.automart.user.dto.AuthResponseDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"3. Shopping Cart"})
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @ApiOperation(value = "4-2 장바구니 상품 추가", notes = "상품의 바코드를 인식하면 장바구니에 상품이 담깁니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 장바구니에 상품이 추가되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력이 아닙니다. 혹은 재고 부족으로 인해 상품을 담을 수 없습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
            @ApiResponse(code = 404, message = "해당 제품이 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<Void> addProductByCode(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @ApiParam("추가하려는 상품의 바코드 번호") @Valid @RequestBody CartAddProductRequestDto cartAddProductRequestDto) throws SessionUnstableException, NotEnoughStockException {
        cartService.addProductByCode(userPrincipal.getNo(), cartAddProductRequestDto.getProductCode());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @ApiOperation(value = "4-3 장바구니 조회", notes = "장바구니 목록이 반환됩니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 장바구니 목록이 반환되었습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
            @ApiResponse(code = 404, message = "해당 유저의 장바구니가 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/list")
    public ResponseEntity<CartResponseDto> showUserCart(@AuthenticationPrincipal UserPrincipal userPrincipal) throws SessionUnstableException {
        CartResponseDto cartResponseDtos = cartService.showUserCarts(userPrincipal.getNo());
        return ResponseEntity.status(HttpStatus.OK).body(cartResponseDtos);
    }


    @ApiOperation(value = "4-3 장바구니 상품 개수 증가", notes = "장바구니 화면에서 해당 상품의 개수 '+' 버튼을 누르면 장바구니의 상품 개수가 1개 증가됩니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "productNo", value = "개수를 증가하려는 제품의 고유번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 장바구니의 상품 개수가 증가되었습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
            @ApiResponse(code = 404, message = "상품을 찾을 수 없습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/add/{productNo}")
    public ResponseEntity<Void> addProductCount(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable int productNo)  throws SessionUnstableException, NotEnoughStockException {
        cartService.addProduct(userPrincipal.getNo(), productNo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @ApiOperation(value = "4-3 장바구니 상품 개수 감소", notes = "장바구니 화면에서 해당 상품의 개수 '-' 버튼을 누르면 장바구니의 상품 개수가 1개 감소됩니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "productNo", value = "개수를 감소하려는 제품의 고유번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 장바구니의 상품 개수가 감소되었습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
            @ApiResponse(code = 404, message = "상품을 찾을 수 없습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/sub/{productNo}")
    public ResponseEntity<Void> subProductCount(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable int productNo) throws SessionUnstableException {
        cartService.subtractProduct(userPrincipal.getNo(), productNo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @ApiOperation(value = "4-3 장바구니 상품 삭제", notes = "장바구니 화면에서 해당 상품의 '삭제하기' 버튼을 누르면 장바구니에서 해당 상품이 삭제됩니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 204, message = "정상적으로 장바구니의 상품이 삭제되었습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능"),
            @ApiResponse(code = 404, message = "상품을 찾을 수 없습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public ResponseEntity<Void> deleteProduct(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @ApiParam("삭제하려는 제품의 고유번호") @RequestBody CartRemoveProductRequestDto cartRemoveProductRequestDto) throws SessionUnstableException {
        cartService.takeProductOutOfCart(userPrincipal.getNo(), cartRemoveProductRequestDto.getProductNo());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
