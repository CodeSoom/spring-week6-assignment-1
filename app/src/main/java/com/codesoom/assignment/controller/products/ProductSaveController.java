package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.products.ProductSaveService;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;


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
     * @param productDto 상품 등록 데이터
     * @return 등록된 상품
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Product saveProduct(@Valid @RequestBody ProductDto productDto) {
        return service.saveProduct(productDto);
    }

}
