package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import java.util.List;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 상품에 대한 비즈니스 로직을 담당.
 *
 * @see Product
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
     * 저장소에 저장된 모든 상품의 집합을 반환합니다.
     *
     * @return 저장된 모든 상품 집합
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 id와 일치하는 상품을 저장소에서 찾아 반환합니다.
     *
     * @param id 상품 식별자
     * @return 주어진 id와 일치하는 상품
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 주어진 상품를 저장소에 저장합니다.
     *
     * @param productData 저장하고자 하는 상품
     * @return 저장된 상
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 주어진 id와 일치하는 상품을 저장소에서 찾아 수정한 뒤 반환합니다.
     *
     * @param id          상품 식별자
     * @param productData 수정하고자 하는 상품
     * @return 수정된 상품
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 주어진 id와 일치하는 상품을 저장소에서 삭제합니다.
     *
     * @param id 상품 식별자
     * @return 삭제된 상
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 주어진 id와 일치하는 상품을 저장소에서 찾아 반환합니다.
     *
     * @param id 상품 식별자
     * @return 주어진 id와 일치하는 상품
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
