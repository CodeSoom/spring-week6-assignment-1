package com.codesoom.assignment.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "PRODUCT_SEQ_GENERATOR",
        sequenceName = "PRODUCT_SEQ",
        initialValue = 1, allocationSize = 1
)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    String name;

    String maker;

    Integer price;

    String imageUrl;

    private Product(String name, String maker, Integer price, String imageUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static Product of(String name, String maker, Integer price, String imageUrl) {
        return new Product(name, maker, price, imageUrl);
    }

    public void change(String name,
                       String maker,
                       Integer price,
                       String imageUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return String.format(
                "{ id = %s name = %s, maker = %s, price = %s, imageUrl = %s}",
                id,
                name,
                maker,
                price,
                imageUrl
        );
    }
}
