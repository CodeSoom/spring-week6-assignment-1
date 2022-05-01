package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.Product;

import java.util.List;

public interface ProductSelector {
    Product product(Long id);

    List<Product> products();
}
