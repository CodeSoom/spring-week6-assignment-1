package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.ProductRepositoryTestDouble;
import com.codesoom.assignment.domain.ProductDouble;
import com.codesoom.assignment.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductQueryServiceTestDoubleOfDouble {

    @Autowired
    private ProductRepositoryTestDouble productRepositoryTestDouble;

    public ProductQueryServiceTestDoubleOfDouble(ProductRepositoryTestDouble productRepositoryTestDouble) {
        this.productRepositoryTestDouble = productRepositoryTestDouble;
    }

    public ProductDouble product(Long id) {
        Optional<ProductDouble> product = productRepositoryTestDouble.findById(id);
        return product.orElseThrow(
                () -> new ProductNotFoundException("ID [" + id + "]를 찾지 못했기 떄문제 조회를 실패했습니다.")
        );
    }

    public List<ProductDouble> productList() {
        return productRepositoryTestDouble.findAll();
    }

    public void createProduct(Long id) {
        ProductDouble product = ProductDouble.of(
                id,
                "제품1",
                "메이커1",
                100000,
                "webSiteUrl"
        );
        productRepositoryTestDouble.save(product);
    }
}
