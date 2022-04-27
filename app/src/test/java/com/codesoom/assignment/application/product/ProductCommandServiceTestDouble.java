package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductDto;
import com.codesoom.assignment.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCommandServiceTestDouble implements ProductCreator, ProductChanger {
    private final static List<Product> productList = new ArrayList<>();

    @Override
    public Product create(ProductDto productDto) {
        Product product = ProductDto.from(productDto);
        productList.add(product);
        return product;
    }

    @Override
    public Product save(Long id, ProductDto productDto) {
        for (int i = 0; i <= productList.size(); i++) {
            Product product = productList.get(i);

            if (product.getId().equals(id)) {
                product.change(
                        productDto.getName(),
                        productDto.getMaker(),
                        productDto.getPrice(),
                        productDto.getImageUrl()
                );
                productList.set(i, product);
                return product;
            }
        }
        throw new ProductNotFoundException("ID [" + id + "]를 찾지 못했기 떄문제 업데이트를 실패했습니다.");
    }

    @Override
    public void deleteById(Long id) {
        Product product = productList.stream()
                .filter(product1 -> product1.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new ProductNotFoundException("ID [" + id + "]를 찾지 못했기 때문에 삭제를 실패했습니다.")
                );
        productList.remove(product);
    }

    public List<Product> getProductList() {
        return productList;
    }
}
