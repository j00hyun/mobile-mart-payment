package com.automart.service;

import com.automart.domain.*;
import com.automart.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired OrderService orderService;
    @Autowired UserService userService;

    @Autowired OrderRepository orderRepository;
    @Autowired UserRepository userRepository;
    @Autowired CartRepository cartRepository;
    @Autowired EntityManager em;

    @Test
    public void 주문하기() throws Exception {

        // given : 유저가 상품을 카트에 담은 상황에서
        Category category = Category.createCategory("foods");
        em.persist(category);

        Product product1 = Product.createProduct(category, "banana", 3200,4000, 3,12345,null,null);
        Product product2 = Product.createProduct(category, "apple", 2000,3000, 3,23456,null,null);

        em.persist(product1);
        em.persist(product2);


        User user = User.createUserByApp("test@naver.com","testpwd","010-4444-6666","testUser");

        em.persist(user);

        Cart cart1 = Cart.createCart(user,product1);
        Cart cart2 = Cart.createCart(user,product2);

        cart1.addCart(); // 카트1에 담긴 바나나의 수량 증가
        cart1.addCart(); // 카트1에 담긴 바나나의 수량 증가
        cart2.addCart(); // 카트2에 담긴 바나나의 수량 증가

        em.persist(cart1);
        em.persist(cart2);

        // when : 주문을 할 때
        Integer orderNo = orderService.Order(user.getNo(), cart1.getNo(), cart2.getNo());

        // then
        Order findOrder = orderRepository.findByNo(orderNo).get();
        assertThat(findOrder.getNo()).isEqualTo(orderNo);

        assertEquals("주문을 하면 주문상태가 ORDER가 된다.", "ORDER", findOrder.getState());
        assertEquals("1건의 주문에는 주문한 상품의 종류만큼 들어가있어야 한다.", 2, findOrder.getOrderDetails().size());
        assertEquals("주문한 상품들의 총 가격이 정확해야한다.",13600,findOrder.getTotalPrice());
        assertEquals("주문한 수량만큼 재고가 줄어들어야한다.",0,product1.getStock());
        assertEquals("주문한 수량만큼 재고가 줄어들어야한다.",1,product2.getStock());
    }


//    /**
//     * 유저 식별자 동일한지 확인용
//     * @throws Exception
//     */
//    @Test
//    public void 회원가입() throws Exception {
//        // given
//        User user = User.createUserByApp("test@naver.com","testpwd","010-4444-6666","testUser");
//        // when
//        User savedUser = userRepository.save(user); // = em.persist(user)
//        // then
//        assertThat(savedUser.getNo()).isEqualTo(user.getNo());
//    }

}