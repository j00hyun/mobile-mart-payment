package com.automart.order.service;

import com.automart.advice.exception.NotFoundDataException;
import com.automart.advice.exception.SessionUnstableException;
import com.automart.cart.domain.Cart;
import com.automart.cart.domain.CartItem;
import com.automart.cart.repository.CartRepository;
import com.automart.cart.service.CartService;
import com.automart.order.domain.Order;
import com.automart.order.domain.OrderDetail;
import com.automart.order.dto.OrderRequestDto;
import com.automart.order.dto.OrderResponseDto;
import com.automart.order.repository.OrderDetailRepository;
import com.automart.product.domain.Product;
import com.automart.product.repository.ProductRepository;
import com.automart.user.domain.User;
import com.automart.order.repository.OrderRepository;
import com.automart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    /***
     * 주문하기
     * @param requestDto : 주문정보에 대한 Dto (UserNo, CartNos)
     * @return : 생성한 주문에 대한 정보를 담은 Dto를 반환
     */
    @Transactional
    public OrderResponseDto order(String email, OrderRequestDto requestDto) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(
                ()->new SessionUnstableException("해당 유저를 찾을 수 없습니다."));

        Cart cart = cartRepository.findByNo(requestDto.getCartNo()).orElseThrow(
                () -> new NullPointerException("장바구니를 불러오는 데 실패하였습니다."));

        if (cart.getCartItems().size() == 0) {
            throw new NullPointerException("장바구니에 상품을 담아주세요.");
        }

        // 주문상품정보를 생성한다.
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            orderDetails.add(OrderDetail.createOrderDetail(cartItem.getProduct(), cartItem.getCount(), cartItem.getPrice()));
        }

        // 주문 생성
        Order order = Order.createOrder(user,orderDetails);

        orderRepository.save(order);

        // 주문이 완료되었으니 장바구니 상품들을 삭제시킨다.
        cartService.removeAllCartItem(user, cart);

        return OrderResponseDto.of(order);
    }

    /***
     * 주문 취소(어드민)
     * @param orderNo : 취소할 Order의 고유번호
     */
    @Transactional
    public void cancelAll(Integer orderNo) {
        Order order = orderRepository.findByNo(orderNo).orElseThrow(
                () -> new NotFoundDataException("취소할 주문이 존재하지 않습니다.")
        );
        order.cancel(); // 취소한 주문의 경우 상태만 바뀌고 남아있는걸로 설정해놔서 영속성 제거가 안된상태
    }

    /***
     * 주문 단건 취소(어드민)
     * @param orderNo : 취소할 Order의 고유번호
     * @param productNo : 취소할 Product의 고유번호
     */
    @Transactional
    public void cancelOne(Integer orderNo, Integer productNo) { // 주문단건취소(어드민)

        Order order = orderRepository.findByNo(orderNo).orElseThrow(() -> new NotFoundDataException("해당 주문을 찾을 수 없습니다."));
        Product product = productRepository.findByNo(productNo).orElseThrow(() -> new NotFoundDataException("해당 제품을 찾을 수 없습니다."));

        OrderDetail orderDetail = orderDetailRepository.findByOrderAndProduct(order,product)
                .orElseThrow(() -> new NotFoundDataException("해당하는 주문 정보가 없습니다."));

        orderDetail.cancel(); // 취소한 주문의 경우 상태만 바뀌고 남아있는걸로 설정해놔서 영속성 제거가 안된상태
    }

    /***
     * 주문 단건 조회하기
     * @param orderNo : 조회할 주문의 번호
     * @return 주문 단건에 대한 정보를 담은 Dto를 반환
     */
    public OrderResponseDto showOrder(Integer orderNo) {
        Order order = orderRepository.findByNo(orderNo)
                .orElseThrow(()->new NotFoundDataException("주문이 존재하지 않습니다."));
        return OrderResponseDto.of(order);
    }

    /***
     * 전체 주문 조회하기
     * @return 전체 주문에 대한 정보를 담은 Dto를 반환
     */
    public List<OrderResponseDto> showOrders() {
        List<Order> orders = orderRepository.findAll();
        return OrderResponseDto.listOf(orders);
    }
}
