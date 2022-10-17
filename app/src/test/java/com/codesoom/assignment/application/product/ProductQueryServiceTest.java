package com.codesoom.assignment.application.product;

import com.codesoom.assignment.application.product.implement.ProductQueryServiceImpl;
import com.codesoom.assignment.utils.ProductSampleFactory;
import com.codesoom.assignment.common.exception.ProductNotFoundException;
import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("ProductQueryService 클래스")
class ProductQueryServiceTest {
    @DataJpaTest
    class JpaTest {
        @Autowired
        ProductRepository repository;
        ProductQueryService service;

        public ProductRepository getProductRepository() {
            return repository;
        }

        public ProductQueryService getProductService() {
            if (service == null) {
                service = new ProductQueryServiceImpl(repository);
            }
            return service;
        }
    }

    @Nested
    @DisplayName("getProducts 메소드는")
    class Describe_getProducts {
        @Nested
        @DisplayName("데이터가 존재한다면")
        class Context_with_non_empty_data extends JpaTest {

            private final List<Product> givenProducts = new ArrayList<>();

            @BeforeEach
            void prepare() {
                givenProducts.add(getProductRepository().save(ProductSampleFactory.createProduct()));
                givenProducts.add(getProductRepository().save(ProductSampleFactory.createProduct()));
            }

            @Test
            @DisplayName("모든 상품을 리턴한다")
            void it_returns_all_products() {
                final List<Product> actualProducts = getProductService().getProducts();

                assertThat(actualProducts).hasSize(givenProducts.size());
            }
        }

        @Nested
        @DisplayName("데이터가 존재하지 않는다면")
        class Context_with_empty_data extends JpaTest {
            @Test
            @DisplayName("빈 컬렉션을 리턴한다")
            void it_returns_empty_data() {
                final List<Product> actualProducts = getProductService().getProducts();

                assertThat(actualProducts).hasSize(0);
            }
        }

    }

    @Nested
    @DisplayName("getProduct 메소드는")
    class Describe_getProduct {
        @Nested
        @DisplayName("유효한 ID가 주어지면")
        class Context_with_valid_id extends JpaTest {
            private Product givenProduct;

            @BeforeEach
            void prepare() {
                givenProduct = getProductRepository().save(ProductSampleFactory.createProduct());
            }

            @Test
            @DisplayName("상품을 찾아 리턴한다")
            void it_returns_searched_product() {
                final Product actualProduct = getProductService().getProduct(givenProduct.getId());

                assertThat(actualProduct.getName()).isEqualTo(givenProduct.getName());
                assertThat(actualProduct.getMaker()).isEqualTo(givenProduct.getMaker());
                assertThat(actualProduct.getPrice()).isEqualTo(givenProduct.getPrice());
            }
        }

        @Nested
        @DisplayName("유효하지않은 ID가 주어지면")
        class Context_with_invalid_id extends JpaTest {
            private final Long PRODUCT_ID = -1L;

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> getProductService().getProduct(PRODUCT_ID)).isInstanceOf(ProductNotFoundException.class);
            }
        }
    }


}
