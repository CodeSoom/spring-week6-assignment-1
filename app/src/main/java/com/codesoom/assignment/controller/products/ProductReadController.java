package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.products.ProductReadService;
import com.codesoom.assignment.domain.products.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;


/**
 * 상품 조회 요청을 처리합니다.
 */
@ProductController
public class ProductReadController {

    private final ProductReadService service;

    public ProductReadController(ProductReadService service) {
        this.service = service;
    }

    /** 모든 등록된 상품을 반환합니다. */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Product> getProducts() {
        return service.findAll();
    }

    /**
     * 요청받은 식별자로 찾은 상품을 반환합니다.
     *
     * @param id 상품 식별자
     * @return 찾은 상품
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return service.findById(id);
    }

}
