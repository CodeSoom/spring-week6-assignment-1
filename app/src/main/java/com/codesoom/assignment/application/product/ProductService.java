package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.dto.product.ProductData;

import java.util.List;

public interface ProductService {
    List<Product>  getProducts();
    Product getProduct(ProductData.SearchOneProductRequest request);
    Product createProduct(ProductData.CreateProductRequest request);
    Product updateProduct(ProductData.UpdateProductRequest request);
    Product deleteProduct(ProductData.RemoveProductRequest request);
    Product findProduct(long productId);
}
