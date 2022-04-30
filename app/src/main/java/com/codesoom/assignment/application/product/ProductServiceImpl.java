package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;
import com.codesoom.assignment.dto.product.CreateProductRequest;
import com.codesoom.assignment.dto.product.RemoveProductRequest;
import com.codesoom.assignment.dto.product.SearchOneProductRequest;
import com.codesoom.assignment.dto.product.UpdateProductRequest;
import com.codesoom.assignment.errors.ProductNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(SearchOneProductRequest request) {
        long productId = request.getProductId();
        return findProduct(productId);
    }

    @Override
    public Product createProduct(CreateProductRequest request) {
        Product product = request.entityMaker();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(UpdateProductRequest request) {
        long productId = request.getProductId();
        Product product = findProduct(productId);
        Product tobeProduct = request.entityMaker();
        product = product.changeWith(tobeProduct);

        return product;
    }

    @Override
    public void deleteProduct(RemoveProductRequest request) {
        Product product = findProduct(request.getProductId());
        productRepository.delete(product);
    }

    @Override
    public Product findProduct(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

}
