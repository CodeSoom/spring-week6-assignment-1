package com.codesoom.assignment.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;

/**
 * 상품과 관련된 작업을 서비스에 요청한다.
 */
@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private final ProductService productService;
    private final AuthenticationService authenticationService;

    public ProductController(ProductService productService, AuthenticationService authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    /**
     * 상품 목록 조회를 요청 하면, 존재하는 상품 목록을 반환한다.
     * @return 상품 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 특정 상품 조회를 요청하면, 상품을 반환한다.
     * @param id
     * @return 상품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 상품 생성을 요청하면, 생성된 상품을 반환한다.
     * @param authorization
     * @param productData
     * @return 생성된 상품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = authorization.substring("Bearer".length());
        Long userId = authenticationService.parseToken(accessToken);

        System.out.println("******* userId: " + userId);
        return productService.createProduct(productData);
    }

    /**
     * 상품 수정을 요청하면 수정된 상품이 반환된다.
     * @param id
     * @param productData
     * @return 수정된 상품
     */
    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    /**
     * 상품 삭제를 요청하면, 상품이 삭제된다.
     * @param id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleMissingRequestHeaderException() {

    }
}
