package com.codesoom.assignment.controllers.product;

import com.codesoom.assignment.application.product.ProductServiceImpl;
import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.dto.product.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        ProductData.SearchOneProductRequest searchOneProductRequest = new ProductData.SearchOneProductRequest(id);
        return productService.getProduct(searchOneProductRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestBody @Valid ProductData.CreateProductRequest productData
    ) {
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData.UpdateProductRequest productData
    ) {
        productData.initProductId(id);
        return productService.updateProduct(productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id
    ) {
        ProductData.RemoveProductRequest searchOneProductRequest = new ProductData.RemoveProductRequest(id);
        productService.deleteProduct(searchOneProductRequest);
    }
}
