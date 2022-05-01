// 고양이 장난감 쇼핑몰
// Product 모델
// User 모델
// Order 모델
// ... 모델
// Application (UseCase)
// Product -> 관리자 등록/수정/삭제 -> list/detail
// 주문 -> 확인 -> 배송 등 처리

// Product
// 0. 식별자 - identifier (ID)
// 1. 이름 - 쥐돌이
// 2. 제조사 - 냥이월드
// 3. 가격 - 5,000원 (판매가)
// 4. 이미지 - static, CDN => image URL

package com.codesoom.assignment.domain.product;

import com.codesoom.assignment.common.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String maker;

    private Integer price;

    private String imageUrl;

    public Product changeWith(Product source) {
        this.name = source.name;
        this.maker = source.maker;
        this.price = source.price;
        this.imageUrl = source.imageUrl;

        return this;
    }

    public Product initProductId(Long productId) {
        this.id = productId;
        return this;
    }

    private Product(ProductBuilder productBuilderInfo) {
        this.name = productBuilderInfo.name;
        this.maker = productBuilderInfo.maker;
        this.price = productBuilderInfo.price;
        this.imageUrl = productBuilderInfo.imageUrl;
    }

    public static class ProductBuilder implements Builder<Product> {
        private String name;
        private String maker;
        private Integer price;
        private String imageUrl;

        public ProductBuilder(String name, String maker, Integer price) {
            this.name = name;
            this.maker = maker;
            this.price = price;
        }

        public ProductBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        @Override
        public Product builder() {
            return new Product(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(maker, product.maker) && Objects.equals(price, product.price) && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, maker, price, imageUrl);
    }
}
