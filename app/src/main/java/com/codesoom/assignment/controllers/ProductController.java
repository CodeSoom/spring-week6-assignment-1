package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.JwtDecoder;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import java.util.List;
import javax.validation.Valid;
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

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {

    private final ProductService productService;
    private final JwtDecoder jwtDecoder;

    public ProductController(ProductService productService,
        JwtDecoder jwtDecoder) {
        this.productService = productService;
        this.jwtDecoder = jwtDecoder;
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
        @RequestHeader("Authorization") String auth,
        @RequestBody @Valid ProductData productData
    ) {
        checkTokenValid(auth);

        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(
        @RequestHeader("Authorization") String auth,
        @PathVariable Long id,
        @RequestBody @Valid ProductData productData
    ) {
        checkTokenValid(auth);

        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
        @RequestHeader("Authorization") String auth,
        @PathVariable Long id
    ) {
        checkTokenValid(auth);

        productService.deleteProduct(id);
    }

    /**
     * 토큰이 유효한지 검사합니다.
     *
     * @param auth 토큰을 찾을 문자열
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우
     */
    private void checkTokenValid(String auth) {
        String token = auth.substring("Bearer ".length());

        try {
            Claims decode = jwtDecoder.decode(token);
            decode.get("userId", Long.class);
        } catch (RuntimeException re) {
            throw new InvalidTokenException();
        }
    }

}
