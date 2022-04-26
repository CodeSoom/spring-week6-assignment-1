package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.product.ProductCommandService;
import com.codesoom.assignment.application.product.ProductQueryService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;


    public ProductController(
            ProductQueryService productQueryService,
            ProductCommandService productCommandService
    ) {
        this.productCommandService = productCommandService;
        this.productQueryService = productQueryService;
    }

    @GetMapping
    public List<Product> list() {
        return productQueryService.products();
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productQueryService.product(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody ProductDto productDto) {
        return productCommandService.create(productDto);
    }

    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ) {
        return productCommandService.save(id, productDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        productCommandService.deleteById(id);
    }
}
