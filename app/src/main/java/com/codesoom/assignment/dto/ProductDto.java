package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
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
        return Product.builder()
                .name(productDto.getName())
                .maker(productDto.getMaker())
                .price(productDto.getPrice())
                .imageUrl(productDto.getImageUrl())
                .build();
    }
}
