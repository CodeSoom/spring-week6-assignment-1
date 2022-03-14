package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
     * 상품 목록을 리턴한다.
     *
     * @return 상품목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * id에 해당하는 상품을 리턴한다.
     *
     * @param productId 상품의 id
     * @return 찾는 상품
     */
    public Product getProduct(Long productId) {
        return findProduct(productId);
    }

    /**
     * 상품을 생성하고 리턴한다.
     *
     * @param productData 저장될 상품
     * @return 생성된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * id에 해당되는 상품을 수정하고 리턴한다.
     *
     * @param productId   상품의 id
     * @param productData 수정할 productData
     * @return 수정된 상품
     */
    public Product updateProduct(Long productId, ProductData productData) {
        Product product = findProduct(productId);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * id에 해당되는 상품을 삭제하고 리턴한다.
     *
     * @param productId 상품의 id
     */
    public Product deleteProduct(Long productId) {
        Product product = findProduct(productId);

        productRepository.delete(product);

        return product;
    }

    /**
     * id에 해당하는 user을 찾고 없다면 예외를 던진다.
     *
     * @param productId 상품의 id
     * @return 상품의 정보
     * @throws ProductNotFoundException 예외를 던진다.
     */
    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
