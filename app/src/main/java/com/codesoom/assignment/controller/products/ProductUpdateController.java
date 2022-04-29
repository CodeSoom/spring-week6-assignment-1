package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.auth.AuthorizationService;
import com.codesoom.assignment.application.products.ProductSaveRequest;
import com.codesoom.assignment.application.products.ProductUpdateService;
import com.codesoom.assignment.config.AccessToken;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 상픔 변경 요청을 처리합니다.
 */
@ProductController
public class ProductUpdateController {

    private final ProductUpdateService service;
    private final AuthorizationService authorizationService;

    public ProductUpdateController(ProductUpdateService service, AuthorizationService authorizationService) {
        this.service = service;
        this.authorizationService = authorizationService;
    }

    /**
     * 상품 정보를 변경하고, 변경 결과를 반환합니다.
     *
     * @param id 상품 식별자
     * @param productDto 상품 변경 데이터
     * @return 변경된 상품
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public Product updateProduct(@AccessToken String accessToken,
                                 @PathVariable Long id,
                                 @Valid @RequestBody ProductUpdateDto productDto) {
        authorizationService.parseToken(accessToken);
        return service.updateProduct(id, productDto);
    }


    public static class ProductUpdateDto implements ProductSaveRequest {

        @NotBlank(message = "이름을 입력하세요.")
        private String name;

        @NotBlank(message = "판매자를 입력하세요.")
        private String maker;

        @NotNull(message = "가격을 입력하세요.")
        private BigDecimal price;

        private String imageUrl;

        public ProductUpdateDto() {
        }

        public ProductUpdateDto(String name, String maker, BigDecimal price, String imageUrl) {
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
