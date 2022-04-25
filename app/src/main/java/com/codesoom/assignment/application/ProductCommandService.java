package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 상품 변경을 담당합니다.
 */
@Transactional
@Service
public class ProductCommandService implements ProductSaveService, ProductUpdateService, ProductDeleteService {

    private final ProductRepository repository;

    public ProductCommandService(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product saveProduct(ProductSaveRequest saveRequest) {
        return repository.save(saveRequest);
    }

    @Override
    public Product updateProduct(Long id, ProductSaveRequest productSaveRequest) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.update(productSaveRequest.product());

        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        final Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        repository.delete(product);
    }

}
