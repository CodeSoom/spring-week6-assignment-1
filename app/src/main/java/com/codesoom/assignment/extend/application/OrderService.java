package com.codesoom.assignment.extend.application;

import com.codesoom.assignment.exception.OrderNotFoundException;
import com.codesoom.assignment.exception.ProductNotFoundException;
import com.codesoom.assignment.exception.UserNotFoundException;
import com.codesoom.assignment.extend.domain.order.Order;
import com.codesoom.assignment.extend.domain.order.OrderItem;
import com.codesoom.assignment.extend.domain.order.OrderRepository;
import com.codesoom.assignment.extend.domain.product.Product;
import com.codesoom.assignment.extend.domain.product.ProductRepository;
import com.codesoom.assignment.extend.domain.user.User;
import com.codesoom.assignment.extend.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * 주문을 처리합니다.
     * @param userId 주문자 번호
     * @param quantity 수량
     * @param productId 상품 번호
     * @return 생성된 주문 정보
     */
    @Transactional
    public Order order(Long userId, int quantity, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId + " id의 회원을 찾을 수 없습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId + " id의 상품을 찾을 수 없습니다."));

        OrderItem orderItem = OrderItem.create(product, quantity);
        Order order = Order.create(user, orderItem);
        return orderRepository.save(order);
    }

    /**
     * 주문을 취소합니다.
     * @param orderId 취소할 주문 번호
     */
    @Transactional
    public void cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId + " id의 주문을 찾을 수 없습니다."));

        order.cancel();
    }
}
