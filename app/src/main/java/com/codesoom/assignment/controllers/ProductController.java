package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;
    private final AuthService authService;

    public ProductController(
            ProductService productService,
            AuthService authService
    ) {
        this.productService = productService;
        this.authService = authService;
    }

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
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid ProductData productData
    ) {
        validateToken(headers);

        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(
            @RequestHeader HttpHeaders headers,
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        validateToken(headers);

        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @RequestHeader HttpHeaders headers,
            @PathVariable Long id
    ) {
        validateToken(headers);

        productService.deleteProduct(id);
    }

    private void validateToken(HttpHeaders headers) {
        try {
            String token = headers
                    .get("Authorization")
                    .get(0)
                    .substring("Bearer ".length());

            authService.parseToken(token);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }
}
