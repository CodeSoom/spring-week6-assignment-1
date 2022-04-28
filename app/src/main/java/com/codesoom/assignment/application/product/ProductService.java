package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.dto.product.CreateProductRequest;
import com.codesoom.assignment.dto.product.RemoveProductRequest;
import com.codesoom.assignment.dto.product.SearchOneProductRequest;
import com.codesoom.assignment.dto.product.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    List<Product>  getProducts();
    Product getProduct(SearchOneProductRequest request);
    Product createProduct(CreateProductRequest request);
    Product updateProduct(UpdateProductRequest request);
    Product deleteProduct(RemoveProductRequest request);
    Product findProduct(long productId);
}
