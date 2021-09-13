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

    @NotBlank
    private String name;

    @NotBlank
    private String maker;

    @NotBlank
    private Long price;

    @NotBlank
    private String imgUrl;

    @Builder
    public Product(String name, String maker, Long price, String imgUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Product updateProduct(ProductData productData) {

        this.name = productData.getName();
        this.maker = productData.getMaker();
        this.price = productData.getPrice();
        this.imgUrl = productData.getImgUrl();

        return this;

    }

}

