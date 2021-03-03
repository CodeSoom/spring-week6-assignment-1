package com.codesoom.assignment.product.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 상품 명세서.
 */
@Getter
@NoArgsConstructor
public class ProductData {
    /**
     * 상품 식별자.
     */
    private Long id;

    /** 상품명. */
    @NotBlank
    @Mapping("name")
    private String name;

    /** 상품제조사. */
    @NotBlank
    @Mapping("maker")
    private String maker;

    /**
     * 상품가격.
     */
    @NotNull
    @Mapping("price")
    private Integer price;

    /**
     * 상품 이미지.
     */
    @Mapping("imageUrl")
    private String imageUrl;

    /**
     * 상품 명세서
     *
     * @param id       상품식별자
     * @param name     상품명
     * @param maker    상품제조사
     * @param price    상품가격
     * @param imageUrl 상품이미지
     */
    @Builder
    public ProductData(Long id, String name, String maker, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
