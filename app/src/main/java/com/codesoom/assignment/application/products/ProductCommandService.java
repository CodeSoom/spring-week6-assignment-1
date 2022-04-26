package com.codesoom.assignment.application.products;

import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductRepository;
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
                .orElseThrow(() ->
                        new ProductNotFoundException(String.format("%s에 해당하는 상품을 찾을 수 없어 수정에 실패하였습니다.", id)));

        product.update(productSaveRequest.product());

        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        final Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(String.format("%s에 해당하는 상품을 찾을 수 없어 삭제 하지 못했습니다.", id)));

        repository.delete(product);
    }

}
