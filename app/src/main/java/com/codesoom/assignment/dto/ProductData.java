package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Product;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ProductData {
    @NotBlank
    private final String name;

    @NotBlank
    private final String maker;

    @NotNull
    private final Integer price;

    private final String imageUrl;

    @Builder
    public ProductData(String name, String maker, Integer price, String imageUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toProduct() {
        return Product.builder()
                .name(this.name)
                .maker(this.maker)
                .price(this.price)
                .imageUrl(this.imageUrl)
                .build();
    }
}
