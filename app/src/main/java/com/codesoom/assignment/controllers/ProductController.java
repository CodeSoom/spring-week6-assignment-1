package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 제품에 대한 요청를 처리하고 응답합니다.
 */
@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    private final AuthenticationService authenticationService;

    public ProductController(ProductService productService,
                             AuthenticationService authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    /**
     * 모든 제품 목록을 반환합니다.
     * @return 제품 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 요청 식별자에 해당하는 제품을 반환합니다.
     * @param id 제품 식별자
     * @return 제품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 요청 제품을 등록하고, 등록한 제품을 반환합니다.
     * @param authorization Request Header, Authorization Bearer <TOKEN>
     * @param productData 신규 등록할 제품
     * @return 등록한 제품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = authorization.substring("Bearer ".length());

        Long userId = authenticationService.parseToken(accessToken);

        return productService.createProduct(productData);
    }

    /**
     * 제품 정보를 갱신하고, 갱신한 제품을 반환합니다.
     * @param id 제품 식별자
     * @param productData 갱신할 제품
     * @return 갱신한 제품
     */
    @PatchMapping("{id}")
    public Product update(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = authorization.substring("Bearer ".length());

        Long userId = authenticationService.parseToken(accessToken);

        return productService.updateProduct(id, productData);
    }

    /**
     * 제품을 삭제합니다.
     * @param id 제품 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        String accessToken = authorization.substring("Bearer ".length());

        Long userId = authenticationService.parseToken(accessToken);

        productService.deleteProduct(id);
    }
}
