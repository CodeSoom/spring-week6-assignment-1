package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Product;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class ProductDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String maker;

    @NotNull
    private Integer price;

    private String imageUrl;

    public static Product from(ProductDto productDto) {
        return Product.of(
                productDto.getName(),
                productDto.getMaker(),
                productDto.getPrice(),
                productDto.getImageUrl()
        );
    }
}
