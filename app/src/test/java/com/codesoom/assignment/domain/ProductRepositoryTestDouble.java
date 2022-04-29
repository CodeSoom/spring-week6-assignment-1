package com.codesoom.assignment.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryTestDouble {
    List<ProductDouble> productTestDoubles = new ArrayList<>();

    public List<ProductDouble> findAll() {
        return productTestDoubles;
    }

    public Optional<ProductDouble> findById(Long id) {
        return productTestDoubles.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public ProductDouble save(ProductDouble product) {
        productTestDoubles.add(product);
        return product;
    }

    public void delete(ProductDouble productTestDouble) {
        productTestDoubles.remove(productTestDouble);
    }

    public void deleteById(Long id) {
        Optional<ProductDouble> product = findById(id);
        product.ifPresent(productTestDouble -> productTestDoubles.remove(productTestDouble));
    }
}
