package com.codesoom.assignment.extend.domain.order;

import com.codesoom.assignment.extend.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Table(name = "order_items")
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime createdAt;

    protected OrderItem() {
    }

    @Builder
    private OrderItem(Long orderItemId, Order order, int quantity, Product product, LocalDateTime createdAt) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.quantity = quantity;
        this.product = product;
        this.createdAt = createdAt;
    }
}
