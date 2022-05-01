package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductDto;
import com.codesoom.assignment.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductCommandService implements ProductCreator, ProductChanger {
    private final ProductRepository productRepository;

    public ProductCommandService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(ProductDto productDto) {
        Product product = ProductDto.from(productDto);
        return productRepository.save(product);
    }

    @Override
    public Product save(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("ID [" + id + "]를 찾지 못했기 떄문제 업데이트를 실패했습니다."));
        product.change(
                productDto.getName(),
                productDto.getMaker(),
                productDto.getPrice(),
                productDto.getImageUrl()
        );
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("ID [" + id + "]를 찾지 못했기 때문에 삭제를 실패했습니다.")
                );
        productRepository.delete(product);
    }
}
