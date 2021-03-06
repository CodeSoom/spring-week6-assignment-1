package com.codesoom.assignment.product.controller;

import com.codesoom.assignment.product.application.ProductService;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.dto.ProductData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 상품에 대한 사용자 요청을 처리한다.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
    }
}
