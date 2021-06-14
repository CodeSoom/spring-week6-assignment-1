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
 * 고양이 장난감에 대한 생성, 조회, 수정, 삭제 처리를 담당합니다.
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
     * 전체 고양이 장난감 목록을 리턴합니다.
     *
     * @return 고양이 장난감 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 고양이 장난감 정보를 리턴합니다
     *
     * @param id 고양이 장난감 식별자
     * @return 고양이 장난감 정보
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 고양이 장난감 정보를 생성합니다.
     *
     * @param productData 생성하고자 하는 고양이 장난감 정보
     * @return 생성된 고양이 장난감 정보
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 고양이 장난감 정보를 수정합니다.
     *
     * @param id 수정하려는 고양이 장난감 식별자
     * @param productData 수정하려는 새로운 고양이 장난감 정보
     * @return 수정된 고양이 장난감 정보
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 고양이 장난감 정보를 삭제합니다.
     *
     * @param id 삭제하려는 고양이 장난감 식별자
     * @return 삭제된 고양이 장난감 정보
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 고양이 장난감 식별자를 통해 정보를 찾습니다.
     *
     * @throws ProductNotFoundException id에 해당하는 고양이 장난감 정보가 존재하지 않습니다.
     *
     * @param id 고양이 장난감 식별자
     * @return 고양이 장난감 정보
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
