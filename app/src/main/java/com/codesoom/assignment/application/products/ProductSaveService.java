package com.codesoom.assignment.application.products;


import com.codesoom.assignment.domain.products.Product;

/**
 * 상품 생성을 담당합니다.
 */
public interface ProductSaveService {

    /**
     * 상품을 생성합니다.
     *
     * @param productDto 사용자가 입력한 데이터
     * @return 생성된 상품
     */
    Product saveProduct(ProductSaveRequest productDto);

}
