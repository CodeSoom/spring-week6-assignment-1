package com.codesoom.assignment.domain.products;

import com.codesoom.assignment.application.products.ProductSaveRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Override
    List<Product> findAll();

    default Product save(ProductSaveRequest productSaveRequest) {
        return save(productSaveRequest.product());
    }

}
