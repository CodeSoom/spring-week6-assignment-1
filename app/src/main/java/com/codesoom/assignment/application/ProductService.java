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
 * 상품 비즈니스 로직 담당
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
     * 저장된 모든 상품 목록 return
     * @return 저장된 모든 상품 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 id에 해당하는 상품 return
     * @param id 식별자
     * @return 주어진 id 해당 상품
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 주어진 상품을 저장하고 return
     * @param productData 상품 데이터
     * @return 저장된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * id에 해당하는 상품을 수정하고 return
     * @param id 식별자
     * @param productData 상품데이터
     * @return 수정한 상품
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);
        product.changeWith(mapper.map(productData, Product.class));
        return product;
    }

    /**
     * id에 해당하는 상품 삭제후 return
     * @param id 식별자
     * @return 삭제한 상품
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);
        return product;
    }

    /**
     * id에 해당하는 상품 return
     * @param id 식별자
     * @return 해당 id 상품
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
