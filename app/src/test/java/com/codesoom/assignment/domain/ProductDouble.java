package com.codesoom.assignment.domain;

public class ProductDouble {
    Long id;

    String name;

    String maker;

    Integer price;

    String imageUrl;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMaker() {
        return maker;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    protected ProductDouble() {};

    public ProductDouble(Long id, String name, String maker, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static ProductDouble of(Long id, String name, String maker, Integer price, String imageUrl) {
        return new ProductDouble(id, name, maker, price, imageUrl);
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
