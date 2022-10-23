package com.codesoom.assignment.extend.domain.order;

import com.codesoom.assignment.extend.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Table(name = "order_items")
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;
    private String orderItemName;
    private int orderPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    protected OrderItem() {
    }

    /**
     * 주문 상품을 생성합니다.
     * @param product 주문할 상품 정보
     * @param quantity 주문할 상품 수량
     * @return 주문 상품
     */
    public static OrderItem create(Product product, int quantity) {
        OrderItem orderItem = new OrderItem();

        orderItem.product = product;
        orderItem.orderItemName = product.getName();
        orderItem.orderPrice = product.getPrice();
        orderItem.quantity = quantity;

        product.reduceStock(quantity);

        return orderItem;
    }

    /**
     * 주문 취소한 상품의 재고량을 복구시킵니다.
     */
    public void restock() {
        product.addStock(quantity);
    }
}
