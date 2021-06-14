package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.uitls.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 고양이 장난감의 CRUD에 대한 http 요청을 처리합니다.
 *
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
     * 전체 고양이 장난감 목록을 리턴합니다.
     *
     * @return 고양이 장난감 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 고양이 장난감 정보를 리턴합니다.
     *
     * @param id 고양이 장난감 식별자
     * @return 고양이 장난감 정보
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 고양이 장난감 정보를 생성합니다.
     *
     * @param authorization 접속 계정의 인증값
     * @param productData 생성할 고양이 장난감 정보
     * @return 생성된 고양이 장난감 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = authorization.substring("Bearer ".length());
        authenticationService.parseToken(accessToken);
        return productService.createProduct(productData);
    }

    /**
     * 고양이 장난감 정보를 수정합니다.
     *
     * @param authorization 접속 계정의 인증값
     * @param id 수정할 고양이 장난감의 식별자
     * @param productData 수정할 고양이 장난감 정보
     * @return 수정된 고양이 장난감 정보
     */
    @PatchMapping("{id}")
    public Product update(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = authorization.substring("Bearer ".length());
        authenticationService.parseToken(accessToken);
        return productService.updateProduct(id, productData);
    }

    /**
     * 고양이 장난감 정보를 삭제합니다
     *
     * @param authorization 접속 계정의 인증값
     * @param id 삭제할 고양이 장난감의 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        String accessToken = authorization.substring("Bearer ".length());
        authenticationService.parseToken(accessToken);
        productService.deleteProduct(id);
    }
}
