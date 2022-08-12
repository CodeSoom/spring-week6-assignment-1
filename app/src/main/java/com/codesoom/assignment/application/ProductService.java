package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.ProductResponse;
import com.codesoom.assignment.errors.ProductNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품 정보를 받아 상품을 생성하고 상품 응답을 리턴한다.
     *
     * @param productData 상품 정보
     * @return 상품 응답
     */
    public ProductResponse createProduct(ProductData productData) {
        Product product = productRepository.save(productData.toProduct());
        return ProductResponse.from(product);
    }

    /**
     * 상품 정보를 변경하고 상품 응답을 리턴한다.
     *
     * @param id 식별자
     * @param productData 변경할 정보
     * @return 상품 응답
     * @throws ProductNotFoundException 상품을 찾지 못한 경우
     */
    public ProductResponse updateProduct(Long id, ProductData productData) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Product changedProduct = product.change(productData);

        return ProductResponse.from(changedProduct);
    }

    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
