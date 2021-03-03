package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductBadRequestException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 상품에 대한 요청을 수행한다.
 */
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
     * 전체 상품 목록을 리턴한다.
     *
     * @return 저장되어 있는 전체 상품 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 식별자에 해당하는 상품을 리턴한다.
     *
     * @param id - 조회하고자 하는 상품의 식별자
     * @return 주어진 식별자에 해당하는 상품
     * @throws ProductNotFoundException 만약 주어진
     *         {@code id}에 해당되는 상품이 저장되어 있지 않은 경우
     */
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    /**
     * 주어진 상품을 저장하고 해당 객체를 리턴한다.
     *
     * @param productData - 새로 저장하고자 하는 상품
     * @return 저장 된 상품
     * @throws ProductBadRequestException 만약 주어진 상품의
     *         이름이 비어있거나, 메이커가 비어있거나, 가격이 비어있는 경우
     */
    public Product createProduct(ProductData productData) {
        if(productData.getName().isBlank())
            throw new ProductBadRequestException("name");

        if(productData.getMaker().isBlank())
            throw new ProductBadRequestException("maker");

        if(productData.getPrice() == null) {
            throw new ProductBadRequestException("price");
        }

        Product product = mapper.map(productData, Product.class);

        return productRepository.save(product);
    }

    /**
     * 주어진 식별자에 해당하는 상품을 수정하고 해당 객체를 리턴한다.
     *
     * @param id - 수정하고자 하는 상품의 식별자
     * @param productData - 수정 할 새로운 상품
     * @return 수정 된 상품
     * @throws ProductNotFoundException 만약 주어진
     *         @code id}에 해당되는 상품이 저장되어 있지 않은 경우
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = getProduct(id);

        mapper.map(productData, product);

        return product;
    }

    /**
     * 주어진 식별자에 해당하는 상품을 삭제하고 해당 객체를 리턴한다.
     *
     * @param id - 삭제하고자 하는 상품의 식별자
     * @return 삭제 된 상품
     * @throws ProductNotFoundException 만약
     *         @code id}에 해당되는 상품이 저장되어 있지 않은 경우
     */
    public Product deleteProduct(Long id) {
        Product product = getProduct(id);

        productRepository.delete(product);

        return product;
    }
}
