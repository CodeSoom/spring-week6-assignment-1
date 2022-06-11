package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.exception.DecodingInValidTokenException;
import com.codesoom.assignment.utils.Permission;
import org.springframework.http.HttpStatus;
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
        this.authenticationService = authenticationService;
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
    @Permission
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestHeader("Authorization") String token, @RequestBody @Valid ProductData productData) {
        try {
            authenticationService.decode(token);
        } catch (SecurityException e) {
            throw new DecodingInValidTokenException(token);
        }
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    @Permission
    public Product update(@PathVariable Long id, @RequestBody @Valid ProductData productData) {
        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @Permission
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
