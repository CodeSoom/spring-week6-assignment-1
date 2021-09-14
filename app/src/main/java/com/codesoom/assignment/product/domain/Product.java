package com.codesoom.assignment.product.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String maker;

    @NotNull
    private Integer price;

    private String imageUrl;

    public void update(Product source) {
        this.name = source.getName();
        this.maker = source.getMaker();
        this.price = source.getPrice();
        this.imageUrl = source.getImageUrl();
    }
}
