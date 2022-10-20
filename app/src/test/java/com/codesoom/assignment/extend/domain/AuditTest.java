package com.codesoom.assignment.extend.domain;

import com.codesoom.assignment.config.JpaAuditConfig;
import com.codesoom.assignment.extend.domain.product.Product;
import com.codesoom.assignment.extend.domain.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditConfig.class)
class AuditTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void product_has_base_time() {
        Product product = Product.builder()
                .name("toy")
                .price(1000)
                .maker("shop")
                .build();

        productRepository.save(product);

        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getModifiedAt());
    }
}
