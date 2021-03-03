package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품을 전체조회, 조회, 저장, 삭제하는 저장소이다.
 */
public interface ProductRepository {
    /** 전체 상품을 조회한다. */
    List<Product> findAll();

    /** 주어진 아이디에 해당하는 상품을 조회한다. */
    Optional<Product> findById(Long id);

    /** 주어진 상품을 저장하고 해당 객체를 리턴한다. */
    Product save(Product product);

    /** 주어진 상품을 삭제한다. */
    void delete(Product product);
}
