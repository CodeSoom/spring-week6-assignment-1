package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.product.ProductCommand;
import com.codesoom.assignment.application.product.ProductCommandService;
import com.codesoom.assignment.application.product.ProductQueryService;
import com.codesoom.assignment.common.mapper.ProductMapper;
import com.codesoom.assignment.common.resolver.AccessToken;
import com.codesoom.assignment.common.resolver.AuthUser;
import com.codesoom.assignment.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    private final ProductCommandService productCommandService;

    private final ProductQueryService productQueryService;

    private final ProductMapper productMapper;

    public ProductController(ProductCommandService productCommandService, ProductQueryService productQueryService, ProductMapper productMapper) {
        this.productCommandService = productCommandService;
        this.productQueryService = productQueryService;
        this.productMapper = productMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto.ProductInfo> list() {
        return productQueryService.getProducts().stream()
                .map(ProductDto.ProductInfo::new)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto.ProductInfo detail(@PathVariable Long id) {
        return new ProductDto.ProductInfo(productQueryService.getProduct(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto.ProductInfo registerProduct(@RequestBody @Valid ProductDto.RequestParam request,
                                                  @AccessToken AuthUser authUser) {
        System.out.println(authUser);
        final ProductCommand.Register command = productMapper.of(request);
        return new ProductDto.ProductInfo(productCommandService.createProduct(command));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto.ProductInfo updateProduct(@PathVariable Long id,
                                                @RequestBody @Valid ProductDto.RequestParam request,
                                                @AccessToken AuthUser authUser) {
        final ProductCommand.UpdateRequest command = productMapper.of(id, request);
        return new ProductDto.ProductInfo(productCommandService.updateProduct(command));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id,
                              @AccessToken AuthUser authUser) {
        productCommandService.deleteProduct(id);
    }
}
