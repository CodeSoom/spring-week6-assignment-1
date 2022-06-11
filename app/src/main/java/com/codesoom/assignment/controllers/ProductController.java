package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 상품과 관련된 HTTP 요청 처리를 담당한다.
 */
@RestController
@RequestMapping("/products")
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * 모든 상품을 응답한다.
     *
     * @return 모든 상품들
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 주어진 id에 해당하는 상품을 찾아 리턴한다.
     *
     * @param id - 찾으려는 상품의 식별자
     * @return 찾은 상품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 주어진 상품을 저장하고 상품을 응답한다.
     *
     * @param productData - 저장하려는 상품 정보
     * @return 저장한 상품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestBody @Valid ProductData productData
    ) {
        return productService.createProduct(productData);
    }

    /**
     * 주어진 상품 식별자에 해당하는 상품을 찾아 수정하고 수정한 상품을 리턴한다.
     *
     * @param id - 수정하려는 상품의 식별자
     * @param productData - 수정할 상품 정보
     * @return 수정한 상품
     */
    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    /**
     * 주어진 식별자에 해당하는 상품을 삭제한다.
     *
     * @param id - 삭제하려는 상품의 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
    }
}
