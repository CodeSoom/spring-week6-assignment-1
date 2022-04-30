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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("ProductCommandService 에서")
class ProductCommandServiceTest {
    private static final String PRODUCT_NAME = "상품1";
    private static final String PRODUCT_MAKER = "메이커1";
    private static final Integer PRODUCT_PRICE = 100000;
    private static final String PRODUCT_IMAGE_URL = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F9941A1385B99240D2E";

    private static final String UPDATE_PRODUCT_NAME = "상품1000";
    private static final Integer UPDATE_PRODUCT_PRICE = 100;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCommandService productCommandService;

    /**
     * 하나의 Product 를 생성해 등록
     * @return 생성한 Product를 리턴
     */
    private Product createProduct() {
        ProductDto productDto = ProductDto
                .builder()
                .name(PRODUCT_NAME)
                .maker(PRODUCT_MAKER)
                .price(PRODUCT_PRICE)
                .imageUrl(PRODUCT_IMAGE_URL)
                .build();
        return productCommandService.create(productDto);
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_of_create {

        @Nested
        @DisplayName("생성할 수 있는 제품의 데이터가 주어지면")
        class Context_with_create_product {
            private final ProductDto productDto = ProductDto
                    .builder()
                    .name(PRODUCT_NAME)
                    .maker(PRODUCT_MAKER)
                    .price(PRODUCT_PRICE)
                    .imageUrl(PRODUCT_IMAGE_URL)
                    .build();


            @Test
            @DisplayName("제품을 생성하고, 생성된 제품을 반환한다")
            void it_return_created_product() {
                Product product = productCommandService.create(productDto);

                assertThat(product).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("save 메소드는")
    class Describre_of_save {
        private final ProductDto updateDto = ProductDto
                .builder()
                .name(UPDATE_PRODUCT_NAME)
                .maker(PRODUCT_MAKER)
                .price(UPDATE_PRODUCT_PRICE)
                .imageUrl(PRODUCT_IMAGE_URL)
                .build();

        @Nested
        @DisplayName("업데이트할 수 있는 제품의 id와 데이터가 주어지면")
        class Context_with_valid_id {
            private Long productId;

            @BeforeEach
            void setUp() {
                Product product = createProduct();
                productId = product.getId();
            }

            @Test
            @DisplayName("제품을 수정하고, 수정한 제품을 반환한다")
            void it_update_and_return_product() {
                Product product = productCommandService.save(productId, updateDto);

                assertThat(product).isNotNull();
                assertThat(product.getName()).isEqualTo(UPDATE_PRODUCT_NAME);
                assertThat(product.getPrice()).isEqualTo(UPDATE_PRODUCT_PRICE);
            }
        }

        @Nested
        @DisplayName("업데이트할 수 없는 제품의 id가 주어지면")
        class Context_with_invalid_id {
            private Long productId;

            @BeforeEach
            void setUp() {
                Product product = createProduct();
                productId = product.getId();
                productCommandService.deleteById(productId);
            }

            @Test
            @DisplayName("ProductNotFoundException을 던진다")
            void it_throw_productNotFoundException() {
                assertThatThrownBy(() -> productCommandService.save(productId, updateDto))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_of_delete {

        @Nested
        @DisplayName("삭제할 수 있는 제품이 주어지면")
        class Context_with_valid_product {
            private Long productId;

            @BeforeEach
            void setUp() {
                Product product = createProduct();
                productId = product.getId();
            }

            @Test
            @DisplayName("상품을 삭제한다")
            void it_delete_product() {
                productCommandService.deleteById(productId);

                assertThatThrownBy(
                        () -> productRepository.findById(productId).orElseThrow(
                                        () -> new ProductNotFoundException(productId)
                        ))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("삭제할 수 없는 제품이 주어지면")
        class Context_with_invalid_product {
            private Long productId;

            @BeforeEach
            void setUp() {
                Product product = createProduct();
                productId = product.getId();

                productCommandService.deleteById(productId);
            }

            @Test
            @DisplayName("ProductNotFoundException을 던진다")
            void it_throw_productNotFoundException() {
                assertThatThrownBy(
                        () -> productRepository.findById(productId).orElseThrow(
                                () -> new ProductNotFoundException(productId)
                        ))
                        .isInstanceOf(ProductNotFoundException.class);
            }
        }
    }
}
