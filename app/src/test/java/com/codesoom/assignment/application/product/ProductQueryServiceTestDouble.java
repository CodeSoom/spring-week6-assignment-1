package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductDto;
import com.codesoom.assignment.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryServiceTestDouble implements ProductSelector {
    private final ProductCommandServiceTestDouble productCommandServiceTestDouble;

    ProductQueryServiceTestDouble(ProductCommandServiceTestDouble productCommandServiceTestDouble) {
        this.productCommandServiceTestDouble = productCommandServiceTestDouble;
    }

    @Override
    public Product product(Long id) {
        ProductDto productDto = ProductDto.builder()
                .name("제품1")
                .maker("나이키")
                .price(100000)
                .imageUrl("이미지URL")
                .build();
        productCommandServiceTestDouble.create(productDto);

        List<Product> products = productCommandServiceTestDouble.getProductList();
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new ProductNotFoundException("ID [" + id + "]를 찾지 못했기 떄문제 조회를 실패했습니다."));
    }

    @Override
    public List<Product> products() {
        return productCommandServiceTestDouble.getProductList();
    }
}
