package com.automart.service;

import com.automart.advice.exception.DuplicateDataException;
import com.automart.cart.domain.CartItem;
import com.automart.cart.dto.CartResponseDto;
import com.automart.cart.repository.CartItemRepository;
import com.automart.cart.service.CartService;
import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import com.automart.category.service.CategoryService;
import com.automart.advice.exception.NotEnoughStockException;
import com.automart.order.service.OrderService;
import com.automart.order.domain.Order;
import com.automart.order.dto.OrderRequestDto;
import com.automart.order.dto.OrderResponseDto;
import com.automart.order.repository.OrderRepository;
import com.automart.product.service.ProductService;
import com.automart.product.domain.Product;
import com.automart.product.dto.ProductSaveRequestDto;
import com.automart.product.repository.ProductRepository;
import com.automart.user.domain.AuthProvider;
import com.automart.user.service.UserService;
import com.automart.user.domain.User;
import com.automart.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void 회원가입() {
        // given
        User user = User.builder()
                .email("loove1997@naver.com")
                .password("1234")
                .name("박주현")
                .tel("01041026206")
                .snsType(AuthProvider.local)
                .build();

        // when
        userService.saveUser(user);

        // then
        User resultUser = userRepository.findByNo(1).get();
        assertEquals("회원 이메일 확인", "loove1997@naver.com", resultUser.getEmail());
    }

    @Test
    public void 이메일중복회원가입에러() {
        // given
        User user1 = User.builder()
                .email("loove1997@naver.com")
                .password("1234")
                .name("박주현")
                .tel("01041026206")
                .snsType(AuthProvider.local)
                .build();
        userService.saveUser(user1);

        User user2 = User.builder()
                .email("loove1997@naver.com")
                .password("abcd")
                .name("권성훈")
                .tel("01089110489")
                .snsType(AuthProvider.local)
                .build();

        // when
        DuplicateDataException exception = assertThrows(DuplicateDataException.class, () -> {
            userService.saveUser(user2);
        });

        // then
        assertEquals("예외 메세지 발생", "동일한 이메일의 회원이 이미 존재합니다.", exception.getMessage());
    }

    @Test
    public void 핸드폰번호중복회원가입에러() {
        // given
        User user1 = User.builder()
                .email("loove1997@naver.com")
                .password("1234")
                .name("박주현")
                .tel("01041026206")
                .snsType(AuthProvider.local)
                .build();
        userService.saveUser(user1);

        User user2 = User.builder()
                .email("sh0489@naver.com")
                .password("abcd")
                .name("권성훈")
                .tel("01041026206")
                .snsType(AuthProvider.local)
                .build();

        // when
        DuplicateDataException exception = assertThrows(DuplicateDataException.class, () -> {
            userService.saveUser(user2);
        });

        // then
        assertEquals("예외 메세지 발생", "동일한 휴대폰 번호의 회원이 이미 존재합니다.", exception.getMessage());
    }

    @Test
    public void 인증번호전송() {
        // when
        userService.validatePhone("01041026206");
    }

    @Test
    public void 임시비밀번호전송() {
        // when
        userService.generateTempPw("01041026206");
    }

    @Test
    public void 비밀번호변경() {
        // given
        User user = User.builder()
                .email("loove1997@naver.com")
                .password("1234")
                .name("박주현")
                .tel("01041026206")
                .snsType(AuthProvider.local)
                .build();
        userService.saveUser(user);

        // when
        userService.changePassword(user.getNo(), "abcd");

        // then
        assertEquals("비밀번호가 변경되어야한다.", "abcd", user.getPassword());

    }
}

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CartItemRepository cartItemRepository;

    /**
     * 테스트에 필요한 기본적인 유저, 카테고리, 상품 생성
     */
    @BeforeEach
    public void prepare() throws Exception {
        User user = User.builder()
                .email("loove1997@naver.com")
                .password("1234")
                .name("박주현")
                .tel("01041026206")
                .snsType(AuthProvider.local)
                .build();

        userService.saveUser(user);
        categoryService.saveCategory("N-1", "과일");

        ProductSaveRequestDto productDto1 = ProductSaveRequestDto.builder()
                .categoryCode("N-1")
                .name("사과")
                .price(2000)
                .cost(1000)
                .stock(10)
                .code(11111111)
                .location(null)
                .build();

        ProductSaveRequestDto productDto2 = ProductSaveRequestDto.builder()
                .categoryCode("N-1")
                .name("바나나")
                .price(2000)
                .cost(1000)
                .stock(10)
                .code(11111111)
                .location(null)
                .build();

        productService.saveProduct(productDto1);
        productService.saveProduct(productDto2);
    }

    @Test
    public void 새로운상품담기() {
        // given
        User user = userRepository.findByNo(1).get();
        Product product = productRepository.findByNo(1).get();

        // when
        cartService.addProductByCode(1, 1);

        // then
        assertEquals("장바구니의 유저가 존재해야함.", user, cartItemRepository.findByNo(1).get().getCart().getUser());
        assertEquals("해당 유저의 장바구니가 존재해야함.", cartItemRepository.findByNo(1).get(), user.getCart().getCartItems().get(0));
        assertEquals("해당 상품에서 카트를 조회할수 있어야함.", product.getCartItems().get(0).getNo(), cartItemRepository.findByNo(1).get().getNo());
        assertEquals("해당 상품의 재고가 바뀌지 않아야함.", 10, product.getStock());
    }

    @Test
    public void 담긴상품또담기() {
        // given
        User user = userRepository.findByNo(1).get();
        Product product = productRepository.findByNo(1).get();
        cartService.addProduct(1, 1);

        // when
        cartService.addProduct(1, 1);

        // then
        assertEquals("해당 유저의 장바구니가 1개여야함.", 1, user.getCart().getCartItems().size());
        assertEquals("장바구니의 상품 개수가 2개여야함.", 2, cartItemRepository.findByNo(1).get().getCount());
        assertEquals("장바구니의 상품 총 가격이 (가격 X 개수) 야함.", product.getPrice() * 2, cartItemRepository.findByNo(1).get().getPrice());
        assertEquals("상품의 재고가 바뀌지 않아야함.", 10, product.getStock());
    }

    @Test
    public void 재고없는상품담기() {
        // given
        Product product = productRepository.findByNo(1).get();
        product.removeStock(10);
        productRepository.save(product);

        // when
        NotEnoughStockException exception = assertThrows(NotEnoughStockException.class, () -> {
            cartService.addProductByCode(1, 1);
        });

        // then
        assertEquals("상품 개수 0개여야함", 0, product.getStock());
        assertEquals("예외 메세지 발생", "재고 부족으로 인해 상품을 담을 수 없습니다.", exception.getMessage());
    }

    @Test
    public void 카트상품개수감소() {
        // given
        cartService.addProductByCode(1, 1);
        cartService.addProduct(1, 1);

        // when
        cartService.subtractProduct(1, 1);

        // then
        assertEquals("장바구니 상품 개수 1개여야함.", 1, cartItemRepository.findByNo(1).get().getCount());
    }

    @Test
    public void 카트상품제거() {
        // given
        User user = userRepository.findByNo(1).get();
        Product product = productRepository.findByNo(1).get();
        cartService.addProductByCode(1, 1);
        cartService.addProductByCode(1, 1);

        // when
        cartService.takeProductOutOfCart(1, 1);

        // then
        assertEquals("해당 카트가 존재하지 않아야함.", false, cartItemRepository.findByNo(1).isPresent());
        assertEquals("해당 유저의 카트가 존재하지 않아야함.", 0, user.getCart().getCartItems().size());
        assertEquals("해당 상품의 카트가 존재하지 않아야함.", 0, product.getCartItems().size());
    }

    @Test
    public void 카트목록조회() {
        // given
        User user = userRepository.findByNo(1).get();
        Product product1 = productRepository.findByNo(1).get();
        Product product2 = productRepository.findByNo(2).get();

        cartService.addProductByCode(1, 1);
        cartService.addProductByCode(1, 2);
        cartService.addProductByCode(1, 2);

        // when
        CartResponseDto cartResponseDtos = cartService.showUserCarts(1);

        // then
        assertEquals("장바구니에 상품이 2개 담겨있어야함.", 2, cartResponseDtos.getCartItems().size());
    }
}