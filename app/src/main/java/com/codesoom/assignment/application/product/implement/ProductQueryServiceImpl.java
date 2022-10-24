package com.codesoom.assignment.application.product.implement;

import com.codesoom.assignment.application.product.ProductQueryService;
import com.codesoom.assignment.common.exception.EntityNotFoundException;
import com.codesoom.assignment.common.response.ErrorCode;
import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codesoom.assignment.common.response.ErrorCode.PRODUCT_NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;

    public ProductQueryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * @throws EntityNotFoundException 상품이 없을 경우
     */
    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
    }
}
