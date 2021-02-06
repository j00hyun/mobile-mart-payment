package com.automart.order.service;

import com.automart.advice.exception.NotFoundDataException;
import com.automart.advice.exception.SessionUnstableException;
import com.automart.cart.domain.CartItem;
import com.automart.cart.repository.CartItemRepository;
import com.automart.category.domain.Category;
import com.automart.order.domain.Order;
import com.automart.order.domain.OrderDetail;
import com.automart.order.dto.OrderRequestDto;
import com.automart.order.dto.OrderResponseDto;
import com.automart.order.repository.OrderDetailRepository;
import com.automart.order.repository.OrderRepository;
import com.automart.product.domain.Product;
import com.automart.product.repository.ProductRepository;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import com.automart.user.repository.UserRepository;
import com.automart.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 주문하기() throws Exception {

        // given : 유저가 상품을 카트에 담은 상황에서
        Category category = Category.builder().name("foods").build();
        em.persist(category);

        Product product1 = Product.createProduct(category, "banana", 3200,4000, 3,50,"2020.10.10", 12345, null);
        Product product2 = Product.createProduct(category, "apple", 2000,3000, 3,50,"2020.10.10",12344, null);

        em.persist(product1);
        em.persist(product2);


        User user = User.builder()
                .email("test@naver.com")
                .password("testpwd")
                .tel("010-4444-6666")
                .name("testUser")
                .snsType(AuthProvider.local).build();

        em.persist(user);

        CartItem cartItem1 = CartItem.createCartItem(user,product1);
        CartItem cartItem2 = CartItem.createCartItem(user,product2);

        cartItem1.addCartItem(); // 카트1에 담긴 바나나의 수량 증가
        cartItem1.addCartItem(); // 카트1에 담긴 바나나의 수량 증가
        cartItem2.addCartItem(); // 카트2에 담긴 바나나의 수량 증가

        em.persist(cartItem1);
        em.persist(cartItem2);

        // when : 주문을 할 때

        List<Integer> carts = new ArrayList<>();
        carts.add(cartItem1.getNo());
        carts.add(cartItem2.getNo());
        OrderRequestDto requestDto = new OrderRequestDto();

        OrderResponseDto orderDto = orderService.order(user.getEmail(),requestDto);

        // then
        Order findOrder = orderRepository.findByNo(orderDto.getOrderNo()).get();
        assertThat(findOrder.getNo()).isEqualTo(orderDto.getOrderNo());

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

    @Test
    public void 주문에서특정상품취소하기() throws Exception {
        // given
        int orderNo = 1;
        int productNo = 1;
        Order order = orderRepository.findByNo(orderNo).orElseThrow(() -> new NotFoundDataException("해당 주문을 찾을 수 없습니다."));
        Product product = productRepository.findByNo(productNo).orElseThrow(() -> new NotFoundDataException("해당 제품을 찾을 수 없습니다."));

        // when
        Optional<OrderDetail> orderDetail = orderDetailRepository.findByOrderAndProduct(order,product);
        if(orderDetail.isPresent()) {
            System.out.println("조회 성공");
            assertThat(orderDetail.get().getStatus()).isEqualTo("ORDER");

            // then
            orderDetail.get().setStatus("CANCLE");
            assertThat(orderDetail.get().getStatus()).isEqualTo("CANCLE");
        }
    }
}
