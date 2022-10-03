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
     * 모든 상품을 조회한다.
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 상품을 조회한다.
     *
     * @param id 조회할 상품의 식별자
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
     * @return 식별자에 해당하는 상품
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }


    /**
     * 상품을 등록한다.
     *
     * @param productData 등록할 상품의 정보
     * @return 등록된 상품의 정보
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 상품을 수정한다.
     *
     * @param id 수정할 상품의 식별자
     * @param productData 수정할 상품의 정보
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
     * @return 수정된 상품의 정보
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 상품을 삭제한다.
     *
     * @param id 삭제할 상품의 식별자
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
     * @return 삭제된 상품의 정보
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 식별자에 해당하는 상품을 조회한다.
     *
     * @param id 조회할 상품의 식별자
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
     * @return 조회된 상품의 정보
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
