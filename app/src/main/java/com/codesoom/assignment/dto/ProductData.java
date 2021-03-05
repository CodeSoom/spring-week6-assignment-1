package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@lombok.Generated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {
    private Long id;

    @Getter
    @Setter
    @NotBlank
    @Mapping("name")
    private String name;

    @Getter
    @Setter
    @NotBlank
    @Mapping("maker")
    private String maker;

    @Getter
    @Setter
    @NotNull
    @Mapping("price")
    private Integer price;

    @Getter
    @Mapping("imageUrl")
    private String imageUrl;
}
