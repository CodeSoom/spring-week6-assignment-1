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
 * 상품 관련 http 요청 처리를 담당합니다.
 */
@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    private final AuthenticationService authenticationService;

    public ProductController(ProductService productService, AuthenticationService authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    /**
     * 상품 목록 요청을 받아 조회하고 상품 목록을 리턴합니다.
     * @return 상품 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 상품 상세정보 요청을 받아 조회하고 상품을 리턴합니다.
     * @param id 요청 한 상품 id
     * @return 상품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 권한을 확인하고 새 상품 등록 요청을 받아 등록하고 리턴합니다.
     * @param authorization 권한 확인을 위한 토큰
     * @param productData 새로 등록할 상품 내용
     * @return 새로 등록 한 상품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = getAccessToken(authorization);
        authenticationService.parseToken(accessToken);
        return productService.createProduct(productData);
    }

    /**
     * 권한을 확인하고 상품 수정 요청을 받아 수정하고 리턴합니다.
     * @param authorization 권한 확인을 위한 토큰
     * @param id 수정 할 상품 id
     * @param productData 수정 할 상품 정보
     * @return 상품
     */
    @PatchMapping("{id}")
    public Product update(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = getAccessToken(authorization);
        authenticationService.parseToken(accessToken);
        return productService.updateProduct(id, productData);
    }

    /**
     * 권한을 확인하고 상품 삭제 요청을 받아 삭제합니다.
     * @param authorization 권한 확인을 위한 토큰
     * @param id 삭제 할 상품 id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        String accessToken = getAccessToken(authorization);
        authenticationService.parseToken(accessToken);
        productService.deleteProduct(id);
    }

    private String getAccessToken(String authorization) {
        return authorization.substring("Bearer ".length());
    }
}
