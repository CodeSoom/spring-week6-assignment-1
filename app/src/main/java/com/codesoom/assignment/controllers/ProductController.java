package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.WrongUserException;
import com.codesoom.assignment.utils.JWT;
import io.jsonwebtoken.Claims;
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
    private final JWT jwt;

    public ProductController(
            ProductService productService,
            AuthService authService,
            JWT jwt
    ) {
        this.productService = productService;
        this.authService = authService;
        this.jwt = jwt;
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
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        final String accessToken = authorization.substring("Bearer ".length());
        final Claims body = jwt.decode(accessToken).getBody();
        final Boolean valid = authService.valid(
                body.get("id", Long.class),
                body.get("email", String.class),
                body.get("name", String.class)
        );

        if (!valid) {
            throw new WrongUserException();
        }

        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        final String accessToken = authorization.substring("Bearer ".length());
        final Claims body = jwt.decode(accessToken).getBody();
        final Boolean valid = authService.valid(
                body.get("id", Long.class),
                body.get("email", String.class),
                body.get("name", String.class)
        );

        if (!valid) {
            throw new WrongUserException();
        }

        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        final String accessToken = authorization.substring("Bearer ".length());
        final Claims body = jwt.decode(accessToken).getBody();
        final Boolean valid = authService.valid(
                body.get("id", Long.class),
                body.get("email", String.class),
                body.get("name", String.class)
        );

        if (!valid) {
            throw new WrongUserException();
        }

        productService.deleteProduct(id);
    }
}
