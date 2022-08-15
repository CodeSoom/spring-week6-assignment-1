package com.codesoom.assignment.domain;

import com.codesoom.assignment.dto.ProductData;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Builder
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String maker;

    private Integer price;

    private String imageUrl;

    public Product() {}

    public Product(Long id, String name, String maker, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    /**
     * 상품 정보를 변경하고 리턴한다.
     *
     * @param productData 상품 정보
     * @return 상품
     */
    public Product change(ProductData productData) {
        this.name = productData.getName();
        this.maker = productData.getMaker();
        this.price = productData.getPrice();
        this.imageUrl = productData.getImageUrl();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;

        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(maker, product.maker) &&
                Objects.equals(price, product.price) &&
                Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, maker, price, imageUrl);
    }
}
