package com.codesoom.assignment.product.repository;

import com.codesoom.assignment.product.domain.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Product의 조회, 저장, 삭제를 담당한다.
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
    /**
     * product 목록 전체를 조회한다.
     * @return product 목록
     */
    List<Product> findAll();

    /**
     * 요청된 식별자와 일치하는 product를 반환한다.
     * @param id 요청된 식별자
     * @return product
     */
    Optional<Product> findById(Long id);

    /**
     * 요청된 product를 저장한다.
     * @param product 저장할 product
     * @return 저장된 product
     */
    Product save(Product product);

    /**
     * 요청된 식별자와 일치하는 product를 제거한다.
     * @param id product 식별자.
     */
    void deleteById(Long id);
}
