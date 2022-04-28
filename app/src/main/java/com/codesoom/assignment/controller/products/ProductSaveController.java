package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.products.ProductSaveRequest;
import com.codesoom.assignment.application.products.ProductSaveService;
import com.codesoom.assignment.config.AccessToken;
import com.codesoom.assignment.domain.products.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 * 상품 정보 저장 요청을 처리합니다.
 */
@ProductController
public class ProductSaveController {

    private final ProductSaveService service;

    public ProductSaveController(ProductSaveService service) {
        this.service = service;
    }

    /**
     * 상품 정보를 받아 저장하고, 저장된 정보를 반환합니다.
     *
     * @param productSaveDto 상품 등록 데이터
     * @return 등록된 상품
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Product saveProduct(@AccessToken String accessToken,
                               @Valid @RequestBody ProductSaveDto productSaveDto) {
        return service.saveProduct(productSaveDto);
    }

    static class ProductSaveDto implements ProductSaveRequest {

        @NotBlank(message = "이름을 입력하세요.")
        private String name;

        @NotBlank(message = "판매자를 입력하세요.")
        private String maker;

        @NotNull(message = "가격을 입력하세요.")
        private BigDecimal price;

        private String imageUrl;

        public ProductSaveDto() {
        }

        public ProductSaveDto(String name, String maker, BigDecimal price, String imageUrl) {
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

}
