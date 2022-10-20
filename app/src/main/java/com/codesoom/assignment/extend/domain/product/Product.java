package com.codesoom.assignment.extend.domain.product;

import com.codesoom.assignment.extend.domain.BaseTime;
import com.codesoom.assignment.extend.domain.category.Category;
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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    protected Product() {
    }

    @Builder
    private Product(Long productId, String name, String maker, Integer price, String imageUrl, Category category) {
        this.productId = productId;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void update(String name, String maker, Integer price, String imageUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
