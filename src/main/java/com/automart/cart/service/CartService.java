package com.automart.cart.service;

import com.automart.advice.exception.NotFoundDataException;
import com.automart.advice.exception.SessionUnstableException;
import com.automart.cart.domain.Cart;
import com.automart.cart.domain.CartItem;
import com.automart.cart.dto.CartResponseDto;
import com.automart.cart.repository.CartItemRepository;
import com.automart.advice.exception.NotEnoughStockException;
import com.automart.cart.repository.CartRepository;
import com.automart.product.domain.Product;
import com.automart.product.repository.ProductRepository;
import com.automart.user.domain.User;
import com.automart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 바코드번호로 카트에 상품 담기
     * @param userNo : 사용자 고유 번호
     * @param productCode : 담을 제품의 바코드 번호
     */
    public void addProductByCode(int userNo, int productCode) throws SessionUnstableException, NotEnoughStockException, NotFoundDataException {
        log.info("카트에 상품 담기");

        User user = userRepository.findByNo(userNo)
                .orElseThrow(() -> new SessionUnstableException("해당 유저를 찾을 수 없습니다."));
        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new NotFoundDataException("해당 제품이 존재하지 않습니다."));

        Optional<Cart> cart = cartRepository.findByUser(user);

        if(cart.isPresent()) {
            Optional<CartItem> cartItem = cartItemRepository.findByCartAndProduct(cart.get(), product);

            // 장바구니가 존재하고 장바구니 목록에 이미 동일 상품이 존재하는 경우
            if(cartItem.isPresent()) {
                cartItem.get().addCartItem();
                cartItemRepository.save(cartItem.get());
            } else {
                // 장바구니가 존재하지만 장바구니 목록에 동일 상품이 존재하지 않는 경우
                cartItemRepository.save(CartItem.createCartItem(user, product));
            }
        } else {
            // 장바구니가 존재하지 않는 경우
            Cart newCart = Cart.createCart(user);
            cartRepository.save(newCart);

            cartItemRepository.save(CartItem.createCartItem(user, product));
        }
    }


    /**
     * 카트에 담긴 상품 갯수 1개 증가
     * @param userNo : 사용자 고유번호
     * @param productNo : 갯수 증가시킬 상품 고유번호
     */
    public void addProduct(int userNo, int productNo) throws SessionUnstableException, NotFoundDataException {
        log.info("카트에 담긴 상품 증가");

        User user = userRepository.findByNo(userNo)
                .orElseThrow(() -> new SessionUnstableException("해당 유저를 찾을 수 없습니다."));
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(() -> new NotFoundDataException("해당 제품이 존재하지 않습니다."));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundDataException("해당 유저의 장바구니가 존재하지 않습니다."));

        Optional<CartItem> cartItem = cartItemRepository.findByCartAndProduct(cart, product);

        // 카트에 이미 동일 상품이 존재하는 경우에만 상품 증가 가능
        if(cartItem.isPresent()) {
            cartItem.get().addCartItem();
            cartItemRepository.save(cartItem.get());
        }
    }


    /**
     * 카트에 담긴 상품 갯수 1개 감소
     * @param userNo : 사용자 고유번호
     * @param productNo : 갯수 감소시킬 상품 고유번호
     */
    public void subtractProduct(int userNo, int productNo) throws SessionUnstableException, NotEnoughStockException, NotFoundDataException {
        log.info("카트에 담긴 상품 개수 감소");

        User user = userRepository.findByNo(userNo)
                .orElseThrow(() -> new SessionUnstableException("해당 유저를 찾을 수 없습니다."));
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(() -> new NotEnoughStockException("해당 제품이 존재하지 않습니다."));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundDataException("해당 유저의 장바구니가 존재하지 않습니다."));

        Optional<CartItem> cartItem = cartItemRepository.findByCartAndProduct(cart, product);

        if(cartItem.isPresent()) {
            cartItem.get().subtractCartItem();
            cartItemRepository.save(cartItem.get());
        }
    }


    /**
     * 카트에서 상품 제거
     * @param userNo : 사용자 고유번호
     * @param productNo : 제거할 상품 고유번호
     */
    public void takeProductOutOfCart(int userNo, int productNo) throws SessionUnstableException, NotFoundDataException {
        log.info("카트에 담긴 상품 삭제");

        User user = userRepository.findByNo(userNo)
                .orElseThrow(() -> new SessionUnstableException("해당 유저를 찾을 수 없습니다."));
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(() -> new NotFoundDataException("해당 제품이 존재하지 않습니다."));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundDataException("해당 유저의 장바구니가 존재하지 않습니다."));

        Optional<CartItem> cartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if(cartItem.isPresent()) {
            cartItem.get().removeCartItem();
            cartItemRepository.delete(cartItem.get());
        }
    }

    /**
     * 카트 전체 삭제
     * @param user : 사용자
     * @param cart : 해당 사용자의 장바구니
     */
    public void removeAllCartItem(User user, Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        // 카트에 담겨있는 카트 아이템 모두 삭제
        for(CartItem cartItem : cartItems) {
            cartItem.removeCartItem();
            cartItemRepository.delete(cartItem);
        }

        // 유저에게 할당된 카트 삭제
        user.clearCart();
        cartRepository.delete(cart);
    }


    /**
     * 해당 사용자 장바구니 목록 조회하기
     * @param userNo : 사용자 고유번호
     * @return 장바구니 목록 정보
     */
    public CartResponseDto showUserCarts(int userNo) throws SessionUnstableException, NotFoundDataException{
        log.info("해당 사용자 장바구니 목록 조회");

        User user = userRepository.findByNo(userNo)
                .orElseThrow(() -> new SessionUnstableException("해당 유저를 찾을 수 없습니다."));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundDataException("해당 유저의 장바구니가 존재하지 않습니다."));

        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);
        return CartResponseDto.of(cart);
    }
}
