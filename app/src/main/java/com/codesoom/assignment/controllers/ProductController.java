package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import java.util.List;
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

import javax.validation.Valid;

/**
 * 상품에 대한 HTTP 요청 핸들러.
 *
 * @see ProductService
 * @see Product
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
     * 저장된 모든 상품의 집합을 응답합니다.
     *
     * @return 저장된 모든 상품 집합
     */
    @GetMapping
    public List<Product> list(@RequestHeader("Authorization") String accessToken) {
        validateAccessToken(accessToken);
        return productService.getProducts();
    }

    /**
     * 주어진 id와 일치하는 상품을 응답합니다.
     *
     * @param id 상품 식별자
     * @return 주어진 id와 일치하는 상품
     */
    @GetMapping("{id}")
    public Product detail(@RequestHeader("Authorization") String accessToken,
                          @PathVariable Long id) {
        validateAccessToken(accessToken);
        return productService.getProduct(id);
    }

    /**
     * 주어진 상품을 저장하고 저장된 상품을 응답합니다.
     *
     * @param productData 저장하고자 하는 상품 정보
     * @return 저장된 상품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid ProductData productData
    ) {
        validateAccessToken(accessToken);
        return productService.createProduct(productData);
    }

    /**
     * 주어진 id와 일치하는 상품을 수정하고 수정된 상품을 응답합니다.
     *
     * @param id          상품 식별자
     * @param productData 수정하고자 하는 상품
     * @return 수정된 상품
     */
    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    /**
     * 주어진 id와 일치하는 상품을 삭제합니다.
     *
     * @param id 상품 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
    }

    private void validateAccessToken(String bearerToken) {
        String accessToken = bearerToken.substring("Bearer ".length());
        authenticationService.decode(accessToken);
    }
}
