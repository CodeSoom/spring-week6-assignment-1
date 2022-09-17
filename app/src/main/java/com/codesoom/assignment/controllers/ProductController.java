package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.errors.UserNotFoundException;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleMissingRequestHeaderException(){

    }

    /**
     * 모든 상품을 조회한다.
     */
    @GetMapping
    public List<Product> list() {
        System.out.println("test");
        return productService.getProducts();
    }

    /**
     * 상품을 조회한다.
     *
     * @param id 조회할 상품의 식별자
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
     * @return 식별자에 해당하는 상품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 상품을 저장한다.
     *
     * @param productData 상품 저장 정보
     * @param authorization Json Web Token
     * @throws InvalidTokenException 유효하지 않은 토큰일 경우
     * @throws UserNotFoundException 사용자가 존재하지 않을 경우
     * @return 저장된 상품의 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.createProduct(productData);
    }

    /**
     * 상품을 수정한다.
     *
     * @param id 수정할 상품의 식별자
     * @param productData 상품 수정 정보
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
     * @return 수정된 상품
     */
    @PatchMapping("{id}")
    public Product update(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    /**
     * 상품을 삭제한다.
     *
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
     * @param id 삭제할 상품의 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
    }
}
