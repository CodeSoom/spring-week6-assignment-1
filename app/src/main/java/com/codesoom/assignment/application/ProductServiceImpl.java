package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {

        this.productRepository = productRepository;

    }

    @Override
    public List<Product> getProducts() {

        return productRepository.findAll();

    }

    @Override
    public Product getProduct(Long id) {

        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());

    }

    @Override
    public Product createProduct(ProductData source) {

        Product product = Product.builder()
                        .name(source.getName())
                        .maker(source.getMaker())
                        .price(source.getPrice())
                        .imgUrl(source.getImgUrl())
                        .build();

        return productRepository.save(product);

    }

    @Override
    public Product updateProduct(Long id, ProductData source) {

        Product product = getProduct(id);

        return product.updateProduct(source);

    }

    @Override
    public void deleteProduct(Long id) {

        Product product = getProduct(id);

        productRepository.delete(product);

    }

}

