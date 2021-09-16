package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;

import java.util.List;

/**
 * 상품 조회, 수정, 삭제 서비스
 */
public interface ProductService {

    /**
     * 모든 상품 목록을 리턴합니다.
     * @return 상품 목록
     */
    List<Product> getProducts();

    /**
     * 상품 하나를 조회해 리턴합니다.
     * @param id 조회할 상품의 id
     * @return 조회된 상품
     * @throws ProductNotFoundException 상품을 못찾을 경우
     */
    Product getProduct(Long id);

    /**
     * 상품을 저장소에 등록합니다.
     * @param source 등록할 상품의 내용
     * @return 등록한 상품
     */
    Product createProduct(ProductData source);

    /**
     * 상품의 내용을 수정합니다
     * @param id 수정할 상품의 id
     * @param source 상품의 수정 내용
     * @return 수정한 상품
     * @throws ProductNotFoundException 상품을 못찾을 경우
     */
    Product updateProduct(Long id, ProductData source);

    /**
     * 상품 삭제
     * @param id 삭제할 상품의 id
     */
    void deleteProduct(Long id);

}

