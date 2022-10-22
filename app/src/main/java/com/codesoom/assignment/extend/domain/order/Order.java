package com.codesoom.assignment.extend.domain.order;

import com.codesoom.assignment.extend.domain.user.User;
import io.jsonwebtoken.lang.Assert;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime createdAt;

    protected Order() {
    }

    /**
     * 주문 상품을 추가합니다.
     * @param item 주문한 상품
     */
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    /**
     * 주문을 생성합니다.
     * @param user 주문한 회원
     * @param orderItems 주문한 상품들
     * @return 생성된 주문
     */
    public static Order create(User user, OrderItem... orderItems) {
        Assert.notNull(orderItems, "주문을 위해 주문 상품은 필수입니다.");

        Order order = new Order();

        order.status = OrderStatus.ORDER;
        order.createdAt = LocalDateTime.now();
        order.user = user;

        for (OrderItem item : orderItems) {
            order.addOrderItem(item);
        }

        return order;
    }

    /**
     * 주문을 취소합니다.
     */
    public void cancel() {
        this.status = OrderStatus.CANCEL;

        for (OrderItem item : orderItems) {
            item.restock();
        }
    }

}
