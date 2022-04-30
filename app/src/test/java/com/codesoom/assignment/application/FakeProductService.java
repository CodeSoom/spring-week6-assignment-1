package com.codesoom.assignment.application;

import com.codesoom.assignment.application.product.ProductService;
import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.dto.product.CreateProductRequest;
import com.codesoom.assignment.dto.product.RemoveProductRequest;
import com.codesoom.assignment.dto.product.SearchOneProductRequest;
import com.codesoom.assignment.dto.product.UpdateProductRequest;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.infra.FakeProductStorage;

import java.util.List;

public class FakeProductService implements ProductService {
    private FakeProductStorage fakeProductStorage = new FakeProductStorage();

    @Override
    public List<Product> getProducts() {
        return fakeProductStorage.findAll();
    }

    @Override
    public Product getProduct(SearchOneProductRequest request) {
        return fakeProductStorage.findById(request.getProductId())
                                 .orElse(null);
    }

    @Override
    public Product createProduct(CreateProductRequest request) {
        Product product = request.entityMaker();
        return fakeProductStorage.save(product);
    }

    @Override
    public Product updateProduct(UpdateProductRequest request) {
        return null;
    }

    @Override
    public void deleteProduct(RemoveProductRequest request) {
        Product product = findProduct(request.getProductId());
        fakeProductStorage.delete(product);
    }

    @Override
    public Product findProduct(long productId) {
        return fakeProductStorage.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
