package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductData {

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "메이커 이름은 필수입니다.")
    private String maker;

    @NotNull(message = "가격은 필수입니다.")
    private Long price;

    @NotBlank(message = "이미지는 필수입니다.")
    private String imgUrl;

    @Builder
    public ProductData(String name, String maker, Long price, String imgUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imgUrl = imgUrl;
    }

}
