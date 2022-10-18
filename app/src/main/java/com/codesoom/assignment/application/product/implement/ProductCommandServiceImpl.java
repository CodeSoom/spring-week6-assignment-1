package com.codesoom.assignment.application.product.implement;

import com.codesoom.assignment.application.product.ProductCommand;
import com.codesoom.assignment.application.product.ProductCommandService;
import com.codesoom.assignment.common.exception.EntityNotFoundException;
import com.codesoom.assignment.common.mapper.ProductMapper;
import com.codesoom.assignment.common.response.ErrorCode;
import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codesoom.assignment.common.response.ErrorCode.PRODUCT_NOT_FOUND;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductCommandServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    @Override
    public Product createProduct(ProductCommand.Register command) {
        return productRepository.save(ProductMapper.INSTANCE.toEntity(command));
    }

    /**
     * @throws EntityNotFoundException 상품이 없을 경우
     */
    @Transactional
    @Override
    public Product updateProduct(ProductCommand.UpdateRequest command) {
        Product product = productMapper.toEntity(command);
        Product findProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        return findProduct.modifyProduct(product);
    }

    /**
     * @throws EntityNotFoundException 상품이 없을 경우
     */
    @Transactional
    @Override
    public void deleteProduct(Long id) {
        Product findProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        productRepository.delete(findProduct);
    }
}
