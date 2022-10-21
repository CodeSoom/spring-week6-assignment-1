package com.codesoom.assignment.extend.domain.product;

import com.codesoom.assignment.extend.domain.BaseTime;
import com.codesoom.assignment.extend.domain.category.Category;
import com.codesoom.assignment.exception.StockOutException;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Table(name = "products")
@Entity(name = "Products")
public class Product extends BaseTime {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String maker;

    @Column(nullable = false)
    private Integer price;

    private String imageUrl;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    protected Product() {
    }

    @Builder
    private Product(Long productId, String name, String maker, Integer price, String imageUrl, int stock, Category category) {
        this.productId = productId;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.category = category;
    }

    public void update(String name, String maker, Integer price, String imageUrl, int stock) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    /**
     * 상품 재고를 추가합니다.
     * @param amount 추가할 양
     */
    public void addStock(int amount) {
        this.stock += amount;
    }

    /**
     * 상품 재고를 줄입니다.
     * @param amount 줄일 양
     * @throws StockOutException 재고가 줄이고자 하는 양보다 부족한 경우
     */
    public void reduceStock(int amount) {
        int restStock = this.stock - amount;
        if (restStock < 0) {
            throw new StockOutException("재고가 부족하여 줄일 수 없습니다.");
        }
        this.stock = restStock;
    }
}
