package com.codesoom.assignment.domain;

import com.codesoom.assignment.dto.ProductData;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String maker;
    private Long price;
    private String imageUrl;

    @Builder
    public Product(String name, String maker, Long price, String imageUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product updateProduct(ProductData productData) {

        this.name = productData.getName();
        this.maker = productData.getMaker();
        this.price = productData.getPrice();
        this.imageUrl = productData.getImageUrl();

        return this;

    }

}

