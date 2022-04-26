package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductDto;
import com.codesoom.assignment.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@DisplayName("ProductQueryService 에서")
class ProductQueryServiceTest {

    @Autowired
    private ProductRepository productRepository;
    private ProductQueryService productQueryService;

    @BeforeEach
    void setUp() {
        this.productQueryService = new ProductQueryService(productRepository);
    }

    /**
     * 하나의 Product 를 생성해 등록
     * @return 생성한 Product를 리턴
     */
    private Product createProduct() {
        ProductDto productDto = ProductDto
                .builder()
                .name("제품1")
                .maker("제품 메이커")
                .price(100)
                .imageUrl("imageUrl")
                .build();
        return productRepository.save(ProductDto.from(productDto));
    }

    @Nested
    @DisplayName("products 메소드는")
    class Describe_of_products {

        @Nested
        @DisplayName("조회할 수 있는 제품이 있다면")
        class Context_with_list {

            @BeforeEach
            void setUp() {
                createProduct();
            }

            @Test
            @DisplayName("제품이 있는 배열을 리턴한다")
            void it_return_list_with_product() {
                List<Product> products = productQueryService.products();

                assertThat(products).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("조회할 수 있는 제품이 없다면")
        class Context_with_emtpy_list {

            @BeforeEach
            void setUp() {
                createProduct();
                productRepository.deleteAll();
            }

            @Test
            @DisplayName("빈 배열을 리턴한다")
            void it_return_empty_list() {
                List<Product> products = productQueryService.products();

                assertThat(products).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("product 메소드는")
    class Describe_of_product {

        @Nested
        @DisplayName("조회할 수 있는 id가 주어지면")
        class Context_with_valid_id {
            private Long productId;

            @BeforeEach
            void setUp() {
                Product product = createProduct();
                productId = product.getId();
            }

            @Test
            @DisplayName("id와 동일한 제품을 리턴한다")
            void it_return_product() {
                Product product = productQueryService.product(productId);

                assertThat(product).isNotNull();
            }
        }

        @Nested
        @DisplayName("조회할 수 없는 id가 주어지면")
        class Context_with_invalid_id {
            private Long productId;

            @BeforeEach
            void setUp() {
                Product product = createProduct();
                productId = product.getId();

                productRepository.deleteById(productId);
            }

            @Test
            @DisplayName("ProductNotFoundException을 던진다")
            void it_throw_productNotFoundException() {
                assertThatThrownBy(() -> productQueryService.product(productId))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }
}