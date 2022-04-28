package com.codesoom.assignment.dto.product;

import com.codesoom.assignment.common.EntityMaker;
import com.codesoom.assignment.domain.product.Product;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateProductRequest implements EntityMaker<Product> {

    private long productId;

    @NotBlank
    private final String name;

    @NotBlank
    private final String maker;

    @NotNull
    private final int price;
    private final String imageUrl;

    public UpdateProductRequest(
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
        return "UpdateProductRequest{" +
                "id=" + productId +
                ", name='" + name + '\'' +
                ", maker='" + maker + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public void initProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public Product entityMaker() {
        return Product.ProductBuilder.builder()
                .name(name)
                .maker(maker)
                .price(price)
                .imageUrl(imageUrl)
                .build();
    }
}
