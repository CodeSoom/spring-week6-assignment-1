package com.codesoom.assignment.controllers.product;

import com.codesoom.assignment.application.product.ProductService;
import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.dto.product.CreateProductRequest;
import com.codesoom.assignment.dto.product.RemoveProductRequest;
import com.codesoom.assignment.dto.product.SearchOneProductRequest;
import com.codesoom.assignment.dto.product.UpdateProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


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
        SearchOneProductRequest searchOneProductRequest = SearchOneProductRequest.createSearchRequestObjectFrom(id);
        return productService.getProduct(searchOneProductRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestBody @Valid CreateProductRequest productData
    ) {
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductRequest productData
    ) {
        productData.initProductId(id);
        return productService.updateProduct(productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @PathVariable Long id
    ) {
        RemoveProductRequest searchOneProductRequest = RemoveProductRequest.createRemoveRequestObjectFrom(id);
        productService.deleteProduct(searchOneProductRequest);
    }
}
