package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@lombok.Generated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {
    private Long id;

    @Getter
    @NotBlank
    @Mapping("name")
    private String name;

    @Getter
    @NotBlank
    @Mapping("maker")
    private String maker;

    @Getter
    @NotNull
    @Mapping("price")
    private Integer price;

    @Getter
    @Mapping("imageUrl")
    private String imageUrl;
}
