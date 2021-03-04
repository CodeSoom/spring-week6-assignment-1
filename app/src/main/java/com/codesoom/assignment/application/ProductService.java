package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 상품과 관련된 요청을 처리한다.
 */
@Service
@Transactional
public class ProductService {
    private final Mapper mapper;
    private final ProductRepository productRepository;

    public ProductService(
            Mapper dozerMapper,
            ProductRepository productRepository
    ) {
        this.mapper = dozerMapper;
        this.productRepository = productRepository;
    }

    /**
     * 상품 목록을 얻는다.
     * @return
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 상품을 얻는다.
     * @param id
     * @return 상품
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품을 생성한다.
     * @param productData
     * @return 생성된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 상품을 수정한다.
     * @param id
     * @param productData
     * @return 수정된 상품
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 상품을 삭제한다.
     * @param id
     * @return 삭제된 상품
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 상품을 찾아 조회한다.
     * @param id
     * @return 조회된 상품
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
