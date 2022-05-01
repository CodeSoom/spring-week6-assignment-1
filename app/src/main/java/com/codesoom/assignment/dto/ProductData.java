package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class ProductData {
    private Long id;

    @Builder
    public ProductData(Long id, String name, String maker, Integer price) {
        this.id = id;
        this.name = name;
        this.maker = maker;
        this.price = price;
    }

    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Mapping("maker")
    private String maker;

    @NotNull
    @Mapping("price")
    private Integer price;

    @Mapping("imageUrl")
    private String imageUrl;
}
