package com.codesoom.assignment.controllers.product;

import com.codesoom.assignment.application.product.ProductService;
import com.codesoom.assignment.application.product.ProductServiceImpl;
import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.dto.product.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.codesoom.assignment.dto.product.ProductData.RemoveProductRequest;
import static com.codesoom.assignment.dto.product.ProductData.SearchOneProductRequest;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        SearchOneProductRequest searchOneProductRequest = SearchOneProductRequest.initProductId(id);
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
        RemoveProductRequest searchOneProductRequest = RemoveProductRequest.initProductId(id);
        productService.deleteProduct(searchOneProductRequest);
    }
}
