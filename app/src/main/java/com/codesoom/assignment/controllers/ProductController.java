package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.argumentresolver.Login;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.LoginUser;
import com.codesoom.assignment.dto.request.ProductData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
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
    public Product create(@Login LoginUser loginUser, @RequestBody @Valid ProductData productData) {
        log.info("회원 {} > 상품 생성 요청", loginUser.getId());
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(@PathVariable Long id, @Login LoginUser loginUser, @RequestBody @Valid ProductData productData) {
        log.info("회원 {} > 상품 {} 수정 요청", loginUser.getId(), id);
        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id, @Login LoginUser loginUser) {
        log.info("회원 {} > 상품 {} 삭제 요청", loginUser.getId(), id);
        productService.deleteProduct(id);
    }
}
