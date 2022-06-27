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
 * Product 에 대한 비즈니스 로직
 */
@Service
@Transactional
public class ProductService {
    /**
     * Product 데이터 저장소
     */
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
     * 저장된 모든 product 집합을 반환
     *
     * @return 저장된 모든 product 집합
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 id 와 일치하는 product 를 찾아서 반환
     *
     * @param id product 식별자
     * @return 주어진 id 와 일치하는 product
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 주어진 product 를 저장하고 저장된 product 를 반환
     *
     * @param productData 저장할 product
     * @return 저장된 product
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 주어진 id 와 일치하는 product 를 찾아서 수정한 후 반환
     *
     * @param id product 식별자
     * @param productData 수정할 product
     * @return 수정된 product
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 주어진 id 와 일치하는 product 를 삭제
     *
     * @param id product 식별자
     * @return 삭제된 product
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 주어진 id 와 일치하는 product 가 없을 때 ProductNotFoundException 예외 처리 반환
     *
     * @param id product 식별자
     * @return 주어진 id 와 일치하는 product 혹은 ProductNotFoundException 예외 처리
     * @throws ProductNotFoundException product 식별자로 product 를 찾지 못하는 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
