package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.request.ProductData;
import com.codesoom.assignment.exception.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final Mapper mapper;
    private final ProductRepository productRepository;

    public ProductService(Mapper dozerMapper, ProductRepository productRepository) {
        this.mapper = dozerMapper;
        this.productRepository = productRepository;
    }

    /**
     * 모든 상품을 조회한다.
     * @return 모든 상품
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 상품을 조회한다.
     * @param id 조회할 상품 아이디
     * @return 찾은 상품
     * @throws ProductNotFoundException 상품을 찾지 못한 경우
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품을 저장한다.
     * @param productData 저장할 상품
     * @return 저장된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 상품을 수정한다.
     * @param id 수정할 상품 아이디
     * @param productData 수정할 상품
     * @return 수정된 상품
     * @throws ProductNotFoundException 수정할 상품을 찾지 못한 경우
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 상품을 삭제한다.
     * @param id 삭제할 상품 아이디
     * @return 삭제한 상품
     * @throws ProductNotFoundException 삭제할 상품을 찾지 못한 경우
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 상품을 조회하여 반환한다.
     * @param id 조회할 상품 아이디
     * @return 찾은 상품
     * @throws ProductNotFoundException 상품을 찾지 못한 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
