package com.codesoom.assignment.infrastructure.product;

import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, Long> {

}
