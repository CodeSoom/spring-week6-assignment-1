package com.codesoom.assignment.product.service;

import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.dto.ProductData;
import com.codesoom.assignment.product.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {
    /**
     * 제품 목록 전체를 반환한다.
     * @return 제품 목록 리스트
     */
    List<Product> getProducts();

    /**
     * 식별자와 일치하는 제품을 반환한다. 만약 존재하지 않으면 예외를 반환한다.
     * @param id 제품 식별자
     * @return 제품
     */
    Product findProductById(Long id) throws ProductNotFoundException;

    /**
     * 요청된 제품을 받아 추가하고, 추가된 제품을 반환한다.
     * @param productData 추가할 제품
     * @return 추가된 제품
     */
    Product addProduct(ProductData productData);

    /**
     * 요청된 식별자와 일치하는 제품을 찾아 수정한다. 만약 존재하지 않으면 예외를 반환한다.
     * @param id 교체할 제품 식별자
     * @param productData 새로 교체할 제품
     * @return 교체된 제품
     */
    Product updateProduct(Long id, ProductData productData);

    /**
     * 요청된 식별자와 일치하는 제품을 찾아 삭제한다. 만약 존재하지 않으면 예외를 반환한다.
     * @param id 제품 식별자
     * @return 삭제된 제품
     */
    Product deleteProductById(Long id);
}
