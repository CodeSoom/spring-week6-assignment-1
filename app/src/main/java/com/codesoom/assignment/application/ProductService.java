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
 * 제품 데이터를 가공하여 반환하거나 처리합니다.
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
     * 모든 제품 목록을 반환합니다.
     * @return 제품 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * ID에 해당하는 제품을 반환합니다.
     * @param id 제품 식별자
     * @return 제품
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 신규 제품을 등록합니다.
     * @param productData 신규 제품 정보
     * @return 등록한 제품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 제품 정보를 갱신합니다.
     * @param id 제품 식별자
     * @param productData 갱신할 제품 정보
     * @return 갱신한 제품
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 제품을 삭제합니다.
     * @param id 제품 식별자
     * @return 삭제한 제품
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * ID에 해당하는 제품을 찾습니다.
     * @param id 제품 식별자
     * @return 제품
     * @throws ProductNotFoundException ID가 null이거나 해당하는 제품이 없을 경우.
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
