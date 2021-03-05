package com.codesoom.assignment.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 고양이 상품 정보를 다룬다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class Product {

    /** 상품 식별자 */
    @Id
    @GeneratedValue
    private Long id;

    /** 상품 이름 */
    private String name;

    /** 상품 제조사 */
    private String maker;

    /** 상품 가격 */
    private Integer price;

    /** 상품 제조사 */
    private String imageUrl;

    @Builder
    public Product(Long id, String name, String maker, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    //상품수정은 dozerMapper가 대신합니다.
//    /** 상품 정보를 업데이트한다. */
//    public void change(Product product) {
//        this.name = product.getName();
//        this.maker = product.getMaker();
//        this.price = product.getPrice();
//        this.imageUrl = product.getImageUrl();
//    }
}
