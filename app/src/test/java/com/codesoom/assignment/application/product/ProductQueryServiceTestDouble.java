package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductDto;
import com.codesoom.assignment.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryServiceTestDouble implements ProductSelector {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product product(Long id) {
        ProductDto productDto = ProductDto.builder()
                .name("제품1")
                .maker("나이키")
                .price(100000)
                .imageUrl("이미지URL")
                .build();
        productRepository.save(ProductDto.from(productDto));

        List<Product> products = productRepository.findAll();
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new ProductNotFoundException("ID [" + id + "]를 찾지 못했기 떄문제 조회를 실패했습니다."));
    }

    @Override
    public List<Product> products() {
        return productRepository.findAll();
    }
}
