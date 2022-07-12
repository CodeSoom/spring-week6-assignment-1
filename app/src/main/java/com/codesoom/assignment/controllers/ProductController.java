package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.jwt.JsonWebToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
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
            @RequestHeader("Authorization") String jwt,
            @RequestBody @Valid ProductData productData
    ) {
        JsonWebToken.verify(jwt);
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        JsonWebToken.verify(jwt);
        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) {
        JsonWebToken.verify(jwt);
        productService.deleteProduct(id);
    }
}
