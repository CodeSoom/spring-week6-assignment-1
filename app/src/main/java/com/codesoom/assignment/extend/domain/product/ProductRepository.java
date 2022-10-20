package com.codesoom.assignment.extend.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("productsRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {
}
