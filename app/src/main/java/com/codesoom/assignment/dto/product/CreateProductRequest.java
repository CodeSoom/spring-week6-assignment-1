package com.codesoom.assignment.dto.product;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CreateProductRequest {
    @NotBlank
    private final String name;

    @NotBlank
    private final String maker;

    @NotNull
    private final int price;
    private final String imageUrl;

    public CreateProductRequest(
            String name,
            String maker,
            int price,
            String imageUrl
    ) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "CreateProduct{" +
                "name='" + name + '\'' +
                ", maker='" + maker + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

}
