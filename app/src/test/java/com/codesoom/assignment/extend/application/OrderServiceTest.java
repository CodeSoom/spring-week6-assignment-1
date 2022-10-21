package com.codesoom.assignment.extend.application;

import com.codesoom.assignment.extend.domain.category.Category;
import com.codesoom.assignment.extend.domain.category.CategoryRepository;
import com.codesoom.assignment.extend.domain.order.Order;
import com.codesoom.assignment.extend.domain.order.OrderRepository;
import com.codesoom.assignment.extend.domain.order.OrderStatus;
import com.codesoom.assignment.extend.domain.product.Product;
import com.codesoom.assignment.extend.domain.product.ProductRepository;
import com.codesoom.assignment.extend.domain.user.User;
import com.codesoom.assignment.extend.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class OrderServiceTest {

    private static final int INIT_STOCK = 13;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 테스트")
    public void order_test() {
        // given
        User user = getUser();
        Product product = getProduct();
        int quantity = 5;

        // when
        Order order = orderService.order(user.getUserId(), quantity, product.getProductId());

        // then
        Order found = orderRepository.findById(order.getOrderId()).get();

        assertThat(found.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(found.getOrderItems().size()).isEqualTo(1);
        assertThat(found.getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(product.getStock()).isEqualTo(INIT_STOCK - quantity);
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancel_test() {
        // given
        User user = getUser();
        Product product = getProduct();
        int quantity = 5;
        Order order = orderService.order(user.getUserId(), quantity, product.getProductId());

        // when
        orderService.cancel(order.getOrderId());

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(product.getStock()).isEqualTo(INIT_STOCK);
    }

    private User getUser() {
        return userRepository.save(User.builder()
                .name("test user")
                .email("test@example.com")
                .password("test12345")
                .build());
    }

    private Product getProduct() {
        return productRepository.save(Product.builder()
                .name("test product")
                .price(10000)
                .maker("test shop")
                .category(getCategory())
                .stock(INIT_STOCK)
                .build());
    }

    private Category getCategory() {
        return categoryRepository.save(Category.builder()
                .name("고양이 장난감")
                .build());
    }
}
