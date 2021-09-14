package com.codesoom.assignment.product.service;

import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.dto.ProductData;
import com.codesoom.assignment.product.exception.ProductNotFoundException;
import com.codesoom.assignment.product.repository.ProductRepository;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    public ProductServiceImpl(ProductRepository productRepository, Mapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product addProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductData productData) {
        Product foundProduct = findProductById(id);

        foundProduct.update(mapper.map(productData, Product.class));
        return foundProduct;
    }

    public Product deleteProductById(Long id) {
        Product foundProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.deleteById(id);
        return foundProduct;
    }
}
