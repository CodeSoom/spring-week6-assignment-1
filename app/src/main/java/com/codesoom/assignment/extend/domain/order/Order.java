package com.codesoom.assignment.extend.domain.order;

import com.codesoom.assignment.extend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @JoinColumn(name = "order_item_id")
    private List<OrderItem> orderItems = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    protected Order() {
    }

    @Builder
    private Order(Long orderId, String status, User user, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.status = status;
        this.user = user;
        this.createdAt = createdAt;
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
    }
}
