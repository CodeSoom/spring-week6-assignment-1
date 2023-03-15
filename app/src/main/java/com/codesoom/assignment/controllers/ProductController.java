package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.MissingResourceException;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final AuthenticationService authenticationService;


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
            @RequestBody @Valid ProductData productData
    ) {
        authenticationService.parseToken(getAccessToken(authorization));
        return productService.createProduct(productData);
    }


    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        authenticationService.parseToken(getAccessToken(authorization));
        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        authenticationService.parseToken(getAccessToken(authorization));
        productService.deleteProduct(id);
    }

    private  String getAccessToken(String authorization) {
        return authorization.substring("Bearer ".length());
    }
}
