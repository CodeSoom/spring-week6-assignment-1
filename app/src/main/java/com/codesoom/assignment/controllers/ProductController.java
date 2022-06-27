package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * /products URL에 대한 HTTP 요청을 처리하는 Controller 클래스
 */
@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Product의 목록을 조회한 후 조회된 Product 목록을 반환한다.
     *
     * @return 조회된 Product 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * id로 Product를 조회한 후 조회된 Product를 반환한다.
     *
     * @param id 상세 조회할 Product의 id
     * @return 조회된 Product
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * Product를 생성한 후, 생성된 Product를 반환한다.
     *
     * @param productData 생성할 Product의 정보가 담긴 DTO
     * @return 생성된 Product
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestBody @Valid ProductData productData
    ) {
        return productService.createProduct(productData);
    }

    /**
     * Product 정보를 수정한 후, 수정된 Product를 반환한다.
     *
     * @param id 수정할 Product의 id
     * @param productData 수정할 정보가 담긴 DTO
     * @return 수정된 Product
     */
    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    /**
     * Product를 삭제한다.
     *
     * @param id 삭제할 Product의 id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
    }
}
