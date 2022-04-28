package com.codesoom.assignment.domain.product.builder;

import com.codesoom.assignment.domain.product.Product;

public interface OptionBuilder {
    OptionBuilder imageUrl(String imageUrl);

    Product build();
}
