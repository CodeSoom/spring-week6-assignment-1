package com.codesoom.assignment.application.products;

import com.codesoom.assignment.application.ServiceTest;
import com.codesoom.assignment.application.products.ProductCommandService;
import com.codesoom.assignment.domain.products.Product;
import com.codesoom.assignment.domain.products.ProductDto;
import com.codesoom.assignment.domain.products.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductSaveTest extends ServiceTest {

    private ProductCommandService service;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() {
        this.service = new ProductCommandService(repository);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("saveProduct 메서드는")
    @Nested
    class Describe_save_product  {
        @DisplayName("상품을 등록하고, 등록 된 상품을 반환한다.")
        @Test
        void will_return_saved_product() {
            //given
            final ProductDto productDto
                    = new ProductDto("name", "maker", BigDecimal.valueOf(2000), "image");

            //when
            final Product product = service.saveProduct(productDto);

            //then
            assertThat(repository.findById(product.getId())).isNotEmpty();
        }
    }

}
