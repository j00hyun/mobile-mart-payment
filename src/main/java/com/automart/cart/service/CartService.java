package com.automart.cart.service;

import com.automart.cart.domain.Cart;
import com.automart.cart.repository.CartRepository;
import com.automart.category.domain.Category;
import com.automart.category.repository.CategoryRepository;
import com.automart.exception.ForbiddenMakeCategoryException;
import com.automart.exception.NotEnoughStockException;
import com.automart.exception.NotFoundUserException;
import com.automart.product.domain.Product;
import com.automart.product.repository.ProductRepository;
import com.automart.user.domain.User;
import com.automart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 카트에 상품 담기
     * @param userNo : 사용자 고유 번호
     * @param productNo : 담을 제품 고유 번호
     */
    public void addProductToCart(int userNo, int productNo) throws NotFoundUserException, NotEnoughStockException {
        User user = userRepository.findByNo(userNo)
                .orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(() -> new NotEnoughStockException("해당 제품이 존재하지 않습니다."));

        Optional<Cart> cart = cartRepository.findByUserAndProduct(user, product);

        // 카트에 이미 동일 상품이 존재하는 경우
        if(cart.isPresent()) {
            cart.get().addCart();
            cartRepository.save(cart.get());
        } else {
            // 카트에 동일 상품이 존재하지 않는 경우
            cartRepository.save(Cart.createCart(user, product));
        }
    }

    /**
     * 카트에 담긴 상품 갯수 1개 감소
     * @param userNo : 사용자 고유번호
     * @param productNo : 갯수 감소시킬 상품 고유번호
     */
    public void subtractProduct(int userNo, int productNo) throws NotFoundUserException, NotEnoughStockException {
        User user = userRepository.findByNo(userNo)
                .orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(() -> new NotEnoughStockException("해당 제품이 존재하지 않습니다."));

        Optional<Cart> cart = cartRepository.findByUserAndProduct(user, product);

        if(cart.isPresent()) {
            cart.get().subtractCart();
            cartRepository.save(cart.get());
        }
    }

    /**
     * 카트에서 상품 제거
     * @param userNo : 사용자 고유번호
     * @param productNo : 제거할 상품 고유번호
     */
    public void takeProductOutOfCart(int userNo, int productNo) throws NotFoundUserException, NotEnoughStockException {
        User user = userRepository.findByNo(userNo)
                .orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
        Product product = productRepository.findByNo(productNo)
                .orElseThrow(() -> new NotEnoughStockException("해당 제품이 존재하지 않습니다."));

        Optional<Cart> cart = cartRepository.findByUserAndProduct(user, product);
        if(cart.isPresent()) {
            cart.get().removeCart();
            cartRepository.delete(cart.get());
        }
    }
}
