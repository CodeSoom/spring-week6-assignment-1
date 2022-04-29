package com.codesoom.assignment.domain;

import com.codesoom.assignment.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("ProductRepository 에서")
class ProductRepositoryTest {
    private static final String PRODUCT_NAME = "상품1";
    private static final String PRODUCT_MAKER = "메이커1";
    private static final Integer PRODUCT_PRICE = 100000;
    private static final String PRODUCT_IMAGE_URL = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F9941A1385B99240D2E";

    private static final Integer UPDATE_PRODUCT_PRICE = 700;
    private static final String UPDATE_PRODUCT_NAME = "상품100";
    private static final String UPDATE_PRODUCT_MAKER = "maker100";
    private static final String UPDATE_PRODUCT_IMAGE_URL = "changedImageUrl";

    @Autowired
    private ProductRepository productRepository;

    /**
     * 하나의 Product 를 생성해 등록합니다.
     *
     * @return 생성된 Product를 반환
     */
    private Product createProduct() {
        Product product = Product.of(
                PRODUCT_NAME,
                PRODUCT_MAKER,
                PRODUCT_PRICE,
                PRODUCT_IMAGE_URL
        );

        return productRepository.save(product);
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Nested
    @DisplayName("findAll 메소드는")
    class Describe_readAll_of_product {

        @Nested
        @DisplayName("상품이 존재할 경우")
        class Context_with_products {

            @BeforeEach
            void setUp() {
                createProduct();
            }

            @Test
            @DisplayName("상품이 존재하는 배열을 리턴합니다")
            void it_returns_list_of_product() {
                List<Product> products = productRepository.findAll();

                assertThat(products).isNotEmpty();
            }
        }

        @Nested
        @DisplayName("상품이 존재하지 않을 경우")
        class Context_with_empty_list {

            @BeforeEach
            void setUp() {
                productRepository.deleteAll();
            }

            @Test
            @DisplayName("빈 배열을 반환합니다")
            void it_return_empty_list() {
                List<Product> products = productRepository.findAll();

                assertThat(products).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("findById 메소드는")
    class Describe_read_of_product {
        private Product product;

        @BeforeEach
        void setUp() {
            product = createProduct();
        }

        @Nested
        @DisplayName("찾을 수 없는 객체의 Id 가 주어지면")
        class Context_without_product {
            private Long productId;

            @BeforeEach
            void setUp() {
                productId = product.getId();
                productRepository.deleteById(productId);
            }

            @Test
            @DisplayName("비어있는 객체가 반환된다")
            void it_throw_productNotFoundException() {
                Optional<Product> product = productRepository.findById(productId);

                assertThat(product).isEmpty();
            }
        }

        @Nested
        @DisplayName("찾을 수 있는 객체의 Id 가 주어지면")
        class Context_with_product {
            private long productId;

            @BeforeEach
            void setUp() {
                productId = product.getId();
            }

            @Test
            @DisplayName("Id와 동일한 객체를 리턴한다")
            void it_return_product() {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ProductNotFoundException(productId));
                assertThat(product).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("save 메소드는")
    class Describe_save_of_product {

        @Nested
        @DisplayName("제품을 생성할 객체가 주어지면")
        class Context_with_create_of_product {
            private Product product;

            @BeforeEach
            void setUp() {
                product = Product.of(
                        PRODUCT_NAME,
                        PRODUCT_MAKER,
                        PRODUCT_PRICE,
                        PRODUCT_IMAGE_URL
                );
            }

            @Test
            @DisplayName("제품을 생성하고, 생성된 제품을 리턴한다")
            void it_return_new_product() {
                Product createdProduct = productRepository.save(product);

                assertThat(createdProduct).isNotNull();
                assertThat(createdProduct.getPrice()).isEqualTo(PRODUCT_PRICE);
            }
        }

        @Nested
        @DisplayName("제품을 업데이트 할 객체가 주어지면")
        class Context_with_update_of_product {
            private Product product;

            @BeforeEach
            void setUp() {
                product = createProduct();
                product.change(
                        UPDATE_PRODUCT_NAME,
                        UPDATE_PRODUCT_MAKER,
                        UPDATE_PRODUCT_PRICE,
                        UPDATE_PRODUCT_IMAGE_URL
                );
            }

            @Test
            @DisplayName("제품을 업데이트하고, 업데이트한 객체를 리턴한다")
            void it_return_updated_product() {
                Product updatedProduct = productRepository.save(product);

                assertThat(updatedProduct).isNotNull();
                assertThat(updatedProduct.getId()).isEqualTo(product.getId());
                assertThat(updatedProduct.getName()).isEqualTo(UPDATE_PRODUCT_NAME);
            }
        }
    }

    @Nested
    @DisplayName("delete 는")
    class Describe_delete_of_product {

        @Nested
        @DisplayName("삭제할 수 있는 상품이 주어지면")
        class Context_with_delete_of_product {
            private Product createdProduct;

            @BeforeEach
            void setUp() {
                createdProduct = createProduct();
            }

            @Test
            @DisplayName("상품을 삭제하고, 삭제되었는지 확인한다")
            void it_delete_product() {
                productRepository.delete(createdProduct);

                Optional<Product> deletedProduct = productRepository.findById(createdProduct.getId());

                assertThat(deletedProduct).isEmpty();
            }
        }
    }
}
