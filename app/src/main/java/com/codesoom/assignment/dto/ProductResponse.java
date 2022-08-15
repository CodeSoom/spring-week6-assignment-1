package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ProductResponse {
    private final Long id;

    private final String name;

    private final String maker;

    private final Integer price;

    private final String imageUrl;

    @Builder
    public ProductResponse(Long id, String name, String maker, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .maker(product.getMaker())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductResponse response = (ProductResponse) o;

        return Objects.equals(id, response.id) &&
                Objects.equals(name, response.name) &&
                Objects.equals(maker, response.maker) &&
                Objects.equals(price, response.price) &&
                Objects.equals(imageUrl, response.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, maker, price, imageUrl);
    }
}
