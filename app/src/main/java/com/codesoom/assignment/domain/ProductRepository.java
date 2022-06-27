package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * Product 관련 DB 처리를 하는 Repository 인터페이스
 */
public interface ProductRepository {
    /**
     * 전체 Product를 찾고, 찾은 Product 목록을 반환한다.
     *
     * @return 조회된 전체 Product 리스트
     */
    List<Product> findAll();

    /**
     * 매개변수로 주어진 id와 id가 일치하는 Product를 찾고, 찾은 Product를 반환한다.
     *
     * @param id 조회할 Product의 id
     * @return 조회된 Product
     */
    Optional<Product> findById(Long id);

    /**
     * Product를 저장한 후에 저장된 Product를 반환한다.
     *
     * @param product 저장할 Product
     * @return 저장된 Product
     */
    Product save(Product product);

    /**
     * Product를 삭제한다.
     *
     * @param product 삭제할 Product
     */
    void delete(Product product);
}
