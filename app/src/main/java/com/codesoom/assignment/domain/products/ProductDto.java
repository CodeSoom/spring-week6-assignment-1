package com.codesoom.assignment.domain.products;

import com.codesoom.assignment.application.products.ProductSaveRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * 상품 등록/수정 시 사용할 DTO 클래스 입니다.
 * 사용자가 입력한 정보를 받기 위해 컨트롤러에서만 사용합니다.
 */
public class ProductDto implements ProductSaveRequest {

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @NotBlank(message = "판매자를 입력하세요.")
    private String maker;

    @NotNull(message = "가격을 입력하세요.")
    private BigDecimal price;

    private String imageUrl;

    public ProductDto() {
    }

    public ProductDto(String name, String maker, BigDecimal price, String imageUrl) {
        this.name = name;
        this.maker = maker;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getMaker() {
        return this.maker;
    }

    @Override
    public BigDecimal getPrice() {
        return this.price;
    }

    @Override
    public String getImageUrl() {
        return this.imageUrl;
    }

}
