package com.automart.cart.controller;

import com.automart.advice.exception.NotEnoughStockException;
import com.automart.advice.exception.NotFoundUserException;
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
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = {"3. Cart"})
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @ApiOperation(value = "4-2 장바구니 상품 추가", notes = "상품의 바코드를 인식하면 장바구니에 상품이 담깁니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "productCode", value = "추가하려는 상품의 바코드 번호", required = true, dataType = "int", defaultValue = "12345")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 장바구니에 상품이 추가되었습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "사용자 정보가 올바르지 않습니다."),
            @ApiResponse(code = 406, message = "상품을 찾을 수 없거나 재고가 부족합니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/create/{productCode}")
    public ResponseEntity<Void> addProductByCode(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @PathVariable int productCode) throws NotFoundUserException, NotEnoughStockException {
        cartService.addProductByCode(userPrincipal.getNo(), productCode);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiOperation(value = "4-3 장바구니 조회", notes = "장바구니 목록이 반환됩니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 장바구니 목록이 반환되었습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "사용자 정보가 올바르지 않습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/list")
    public ResponseEntity<List<CartResponseDto>> showUserCart(@AuthenticationPrincipal UserPrincipal userPrincipal) throws NotFoundUserException {
        List<CartResponseDto> cartResponseDtos = cartService.showUserCarts(userPrincipal.getNo());
        return ResponseEntity.status(HttpStatus.OK).body(cartResponseDtos);
    }


    @ApiOperation(value = "4-3 장바구니 상품 개수 증가", notes = "장바구니 화면에서 해당 상품의 개수 '+' 버튼을 누르면 장바구니의 상품 개수가 1개 증가됩니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "productNo", value = "개수를 증가하려는 제품의 고유번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 장바구니의 상품 개수가 증가되었습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "사용자 정보가 올바르지 않습니다."),
            @ApiResponse(code = 406, message = "상품을 찾을 수 없거나 재고가 부족합니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/add/{productNo}")
    public ResponseEntity<Void> addProductCount(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable int productNo)  throws NotFoundUserException, NotEnoughStockException {
        cartService.addProduct(userPrincipal.getNo(), productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiOperation(value = "4-3 장바구니 상품 개수 감소", notes = "장바구니 화면에서 해당 상품의 개수 '-' 버튼을 누르면 장바구니의 상품 개수가 1개 감소됩니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "productNo", value = "개수를 감소하려는 제품의 고유번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 장바구니의 상품 개수가 감소되었습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "사용자 정보가 올바르지 않습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/sub/{productNo}")
    public ResponseEntity<Void> subProductCount(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                @PathVariable int productNo) throws NotFoundUserException {
        cartService.subtractProduct(userPrincipal.getNo(), productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiOperation(value = "4-3 장바구니 상품 삭제", notes = "장바구니 화면에서 해당 상품의 '삭제하기' 버튼을 누르면 장바구니에서 해당 상품이 삭제됩니다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "productNo", value = "삭제하려는 제품의 고유번호", required = true, dataType = "int", defaultValue = "1")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 장바구니의 상품이 삭제되었습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "사용자 정보가 올바르지 않습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{productNo}")
    public ResponseEntity<Void> deleteProduct(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable int productNo) throws NotFoundUserException {
        cartService.takeProductOutOfCart(userPrincipal.getNo(), productNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
