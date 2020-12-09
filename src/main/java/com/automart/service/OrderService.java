package com.automart.service;


import com.automart.domain.Cart;
import com.automart.domain.Order;
import com.automart.domain.OrderDetail;
import com.automart.domain.User;
import com.automart.repository.CartRepository;
import com.automart.repository.OrderRepository;
import com.automart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    /***
     * 주문하기
     * @param userNo : 주문을 할 User의 고유번호
     * @param cartNos : User의 cart리스트
     * @return : 유저의 식별자
     */
    @Transactional
    public Integer Order(Integer userNo, Integer ...cartNos) throws Exception {

        User user = userRepository.findByNo(userNo).orElseThrow(
                ()->new IllegalAccessException("잘못된 유저입니다."));

        if (cartNos == null || cartNos.length == 0) { // 카트가 존재하지 않을 때 예외처리
            throw new NullPointerException("장바구니에 상품을 담아주세요.");
        }

        // 주문상품정보를 생성한다.
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Integer cartNo : cartNos) {
            Cart cart = cartRepository.findByNo(cartNo);
            System.out.println("과연.."+cart.getProduct());
            orderDetails.add(OrderDetail.createOrderDetail(cart.getProduct(), cart.getCount(), cart.getPrice())); // 여기서 NPE발생
        }

        // 주문 생성
        Order order = Order.createOrder(user,orderDetails);

        orderRepository.save(order);
        return order.getNo();
    }

    /***
     * 주문 취소
     * @param orderNo : 취소할 Order의 고유번호
     */
    @Transactional
    public void cancel(Integer orderNo) {
        Order order = orderRepository.findByNo(orderNo).orElseThrow(
                () -> new IllegalStateException("취소할 주문이 존재하지 않습니다.")
        );
        order.cancel();
    }

    // 주문목록 조회하기

    @Transactional
    public List<Order> findOrders() {
        return orderRepository.findAll();
    }
}
