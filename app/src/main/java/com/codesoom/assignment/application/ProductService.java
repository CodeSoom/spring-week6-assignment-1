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
 * Product 관련 비즈니스를 처리하는 Service 클래스
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
     * Product 목록을 가져와서 반환한다.
     *
     * @return 조회된 Product 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * id로 Product를 조회한 후 이를 반환한다.
     *
     * @param id 상세 조회 할 Product의 id
     * @return 조회된 특정 Product
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * Product를 저장한 후 이를 반환한다.
     *
     * @param productData 등록할 Product의 정보가 담긴 DTO
     * @return 등록된 Product
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * id로 Product를 조회한 후 해당 Product의 정보를 수정하고, 수정된 Product를 반환한다.
     *
     * @param id 수정할 Product의 id
     * @param productData 수정할 정보가 담긴 DTO
     * @return 수정된 Product
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * id로 Product를 조회한 후 삭제하고, 삭제된 Product를 반환한다.
     *
     * @param id 삭제할 Product의 id
     * @return 삭제된 Product
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * id로 Product를 찾고, 찾은 Product를 반환한다.
     *
     * @param id 찾고싶은 Product의 id
     * @return 찾은 Product
     * @throws ProductNotFoundException
     *          id로 Product를 찾지 못한 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
