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

import com.codesoom.assignment.domain.product.builder.*;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
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

    private Product(ProductBuilder productBuilderInfo) {
        this.name = productBuilderInfo.name;
        this.maker = productBuilderInfo.maker;
        this.price = productBuilderInfo.price;
        this.imageUrl = productBuilderInfo.imageUrl;
    }


    public static class ProductBuilder implements ProductBuilderInfo {
        private String name;
        private String maker;
        private long productId;
        private Integer price;
        private String imageUrl;


        private ProductBuilder() {
        }

        public static Name builder() {
            return new ProductBuilder();
        }
        public static ProductId builderWithId(){
            return new ProductBuilder();
        }

        @Override
        public Maker name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Price maker(String maker) {
            this.maker = maker;
            return this;
        }

        @Override
        public OptionBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return null;
        }

        @Override
        public Product build() {
            return new Product(this);
        }

        @Override
        public OptionBuilder price(int price) {
            this.price = price;
            return this;
        }

        @Override
        public Name productId(long productId) {
            this.productId =productId;
            return this;
        }
    }
}
