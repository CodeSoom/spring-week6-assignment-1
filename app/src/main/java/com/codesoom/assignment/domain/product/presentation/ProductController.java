package com.codesoom.assignment.domain.product.presentation;

import com.codesoom.assignment.common.auth.Login;
import com.codesoom.assignment.domain.product.application.ProductService;
import com.codesoom.assignment.domain.product.domain.Product;
import com.codesoom.assignment.domain.product.presentation.dto.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    @Login(required = false)
    public List<Product> list() {
        return productService.getProducts();
    }

    @GetMapping("{id}")
    @Login(required = false)
    public Product detail(@PathVariable Long id) throws Exception {
        if (id == 3L) {
            throw new Exception();
        }

        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public Product create(
            @RequestBody @Valid ProductData productData
    ) {
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    @Login
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void destroy(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
    }
}
