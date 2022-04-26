package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductDto;

public interface ProductUpdater {
    Product save(Long id, ProductDto productDto);
}
