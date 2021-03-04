package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductCreateData;
import com.codesoom.assignment.dto.ProductResultData;
import com.codesoom.assignment.dto.ProductUpdateData;
import com.codesoom.assignment.errors.ProductBadRequestException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    public ProductResultData getProductResultData(Product product) {
        return ProductResultData.builder()
                .id(product.getId())
                .name(product.getName())
                .maker(product.getMaker())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }

    /**
     * 전체 상품 목록을 리턴한다.
     *
     * @return 저장되어 있는 전체 상품 목록
     */
    public List<ProductResultData> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::getProductResultData)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 식별자에 해당하는 상품을 리턴한다.
     *
     * @param id - 조회하고자 하는 상품의 식별자
     * @return 주어진 식별자에 해당하는 상품
     * @throws ProductNotFoundException 만약 주어진
     *         {@code id}에 해당되는 상품이 저장되어 있지 않은 경우
     */
    public ProductResultData getProduct(Long id) {
        Product product =  productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return getProductResultData(product);
    }

    /**
     * 주어진 상품을 저장하고 해당 객체를 리턴한다.
     *
     * @param productCreateData - 새로 저장하고자 하는 상품
     * @return 저장 된 상품
     * @throws ProductBadRequestException 만약 주어진 상품의
     *         이름이 비어있거나, 메이커가 비어있거나, 가격이 비어있는 경우
     */
    public ProductResultData createProduct(@Valid ProductCreateData productCreateData) {
        Product product = mapper.map(productCreateData, Product.class);
        Product savedProduct =  productRepository.save(product);

        return getProductResultData(savedProduct);
    }

    /**
     * 주어진 식별자에 해당하는 상품을 수정하고 해당 객체를 리턴한다.
     *
     * @param id - 수정하고자 하는 상품의 식별자
     * @param productUpdateData - 수정 할 새로운 상품
     * @return 수정 된 상품
     * @throws ProductNotFoundException 만약 주어진
     *         @code id}에 해당되는 상품이 저장되어 있지 않은 경우
     */
    public ProductResultData updateProduct(Long id, ProductUpdateData productUpdateData) {
        ProductResultData productResultData = getProduct(id);

        mapper.map(productUpdateData, productResultData);

        return productResultData;
    }

    /**
     * 주어진 식별자에 해당하는 상품을 삭제하고 해당 객체를 리턴한다.
     *
     * @param id - 삭제하고자 하는 상품의 식별자
     * @return 삭제 된 상품
     * @throws ProductNotFoundException 만약
     *         @code id}에 해당되는 상품이 저장되어 있지 않은 경우
     */
    public ProductResultData deleteProduct(Long id) {
        ProductResultData productResultData = getProduct(id);

        Product deletedProduct = productResultData.toEntity();

        productRepository.delete(deletedProduct);

        return productResultData;
    }
}
