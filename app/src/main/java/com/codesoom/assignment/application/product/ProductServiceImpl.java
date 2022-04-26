package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;
import com.codesoom.assignment.dto.product.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService  {
     private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(ProductData.SearchOneProductRequest request) {
        long productId = request.getId();
        return findProduct(productId);
    }

    @Override
    public Product createProduct(ProductData.CreateProductRequest request) {
        Product product = request.toEntity();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(ProductData.UpdateProductRequest request) {
        long productId = request.getId();
        Product product = findProduct(productId);
        Product tobeProduct = request.toEntity();
        product = product.changeWith(tobeProduct);

        return product;
    }

    @Override
    public Product deleteProduct(ProductData.RemoveProductRequest request) {
        Product product = findProduct(request.getId());
        productRepository.delete(product);
        return product;
    }

    @Override
    public Product findProduct(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

}
