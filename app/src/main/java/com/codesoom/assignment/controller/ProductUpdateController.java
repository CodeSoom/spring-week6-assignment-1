package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.ProductUpdateService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

/**
 * 상픔 변경 요청을 처리합니다.
 */
@ProductController
public class ProductUpdateController {

    private final ProductUpdateService service;

    public ProductUpdateController(ProductUpdateService service) {
        this.service = service;
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
    public Product updateProduct(@PathVariable Long id,
                                 @Valid @RequestBody ProductDto productDto) {
        return service.updateProduct(id, productDto);
    }

}
