package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.product.Product;

import java.util.List;

public interface ProductQueryService {

    /**
     * 전체 상품 목록을 리턴한다.
     * @return 전체 상품 목록
     */
    List<Product> getProducts();

    /**
     * 상품ID에 해당하는 상품을 리턴한다.
     * @param id 상품 ID
     * @return 검색된 상품
     */
    Product getProduct(Long id);
}
