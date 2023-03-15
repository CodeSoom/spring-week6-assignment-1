package com.codesoom.assignment.controllers;

import com.codesoom.assignment.annotation.CheckJwtToken;
import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final AuthenticationService authenticationService;

    private final ProductService productService;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CheckJwtToken
    public Product create(
            @RequestBody @Valid ProductData productData
    ) {

        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    @CheckJwtToken
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {

        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CheckJwtToken
    public void destroy(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);
    }
}
