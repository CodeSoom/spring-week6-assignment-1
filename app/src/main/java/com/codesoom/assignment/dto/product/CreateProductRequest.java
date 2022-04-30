package com.codesoom.assignment.dto.product;

import com.codesoom.assignment.common.EntityMaker;
import com.codesoom.assignment.domain.product.Product;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import static com.codesoom.assignment.domain.product.Product.ProductBuilder;
@Getter
public class CreateProductRequest implements EntityMaker<Product> {
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

    @Override
    public Product entityMaker() {
        Product product= new ProductBuilder(name,maker,price)
                            .imageUrl(imageUrl)
                            .builder();
        return product;
    }
}
