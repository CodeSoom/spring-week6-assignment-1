package com.codesoom.assignment.product.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품 저장소.
 */
public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    void delete(Product product);
}
