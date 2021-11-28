package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // 로그인 필요
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestBody @Valid ProductData productData,
            @RequestHeader("Authorization") String authorization
    ) {
        String accessToken = authorization.substring("Bearer ".length());
        Long userId = authenticationService.parseToken(accessToken);
        return productService.createProduct(productData);
    }

    // 로그인 필요
    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData,
            @RequestHeader("Authorization") String authorization
    ) {
        String accessToken = authorization.substring("Bearer ".length());
        Long userId = authenticationService.parseToken(accessToken);
        return productService.updateProduct(id, productData);
    }

    // 로그인 필요
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        String accessToken = authorization.substring("Bearer ".length());
        Long userId = authenticationService.parseToken(accessToken);
        productService.deleteProduct(id);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleMissingRequestHeaderException() {

    }
}
