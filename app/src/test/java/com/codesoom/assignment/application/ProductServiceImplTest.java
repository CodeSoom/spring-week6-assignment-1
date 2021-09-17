package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ProductService 클래스")
@DataJpaTest
class ProductServiceImplTest {

    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {

        productService = new ProductServiceImpl(productRepository);

    }

    @AfterEach
    void clean() {

        productRepository.deleteAll();

    }

    @Nested
    @DisplayName("getProducts 메소드는")
    class Describe_getProducts {

        @Nested
        @DisplayName("상품들이 존재할 때")
        class Context_exist_products {

            List<Product> productList = new ArrayList<>();

            @BeforeEach
            void setUp() {

                Product product1 = productService.createProduct(ProductData.builder()
                        .name("name1")
                        .maker("maker2")
                        .price(1000L)
                        .imageUrl("img1")
                        .build());

                Product product2 = productService.createProduct(ProductData.builder()
                        .name("name2")
                        .maker("maker2")
                        .price(2000L)
                        .imageUrl("img2")
                        .build());

                productList.add(product1);
                productList.add(product2);

            }

            @Test
            @DisplayName("상품들을 리턴합니다.")
            void It_return_products() {

                List<Product> products = productService.getProducts();

                assertEquals(productList.size(), products.size());

            }

        }

        @Nested
        @DisplayName("상품들이 없을 때")
        class Context_exist_not_products {

            @Test
            @DisplayName("비어있는 리스트를 리턴합니다.")
            void It_return_empty_list() {

                List<Product> products = productService.getProducts();

                assertThat(products).isEmpty();

            }

        }

    }

    @Nested
    @DisplayName("getProduct 메소드는")
    class Describe_getProduct {

        @Nested
        @DisplayName("저장소에 찾으려는 상품이 있다면")
        class Context_exist_product {

            Long validId;

            @BeforeEach
            void setUp() {

                Product product = createTestProduct();

                validId = product.getId();

            }

            @Test
            @DisplayName("찾은 상품을 리턴합니다.")
            void It_return_product() {

                Product findProduct = productService.getProduct(validId);

                assertEquals("name1", findProduct.getName());
                assertEquals("maker1", findProduct.getMaker());
                assertEquals(1000L, findProduct.getPrice());
                assertEquals("img1", findProduct.getImageUrl());

            }

        }

        @Nested
        @DisplayName("찾는 상품이 없다면")
        class Context_exist_not_product {

            Long invalidId = 1000L;

            @BeforeEach
            void setUp() {

                productRepository.deleteAll();

            }

            @Test
            @DisplayName("ProductNotFoundException 예외를 던진다.")
            void It_return_noSuchElementException() {

                Assertions.assertThatThrownBy(() -> {
                    productService.getProduct(invalidId);
                }).isInstanceOf(ProductNotFoundException.class);

            }

        }

    }

    @Nested
    @DisplayName("createProduct 메소드는")
    class Describe_createProduct {

        @Nested
        @DisplayName("등록할 상품이 있다면 상품저장 전용 정보를 입력받아")
        class Context_input_productData {

            ProductData productData;

            @BeforeEach
            void setUp() {

                productData = createTestProductData();

            }

            @Test
            @DisplayName("상품을 저장 후 저장한 상품을 리턴합니다.")
            void It_save_return_product() {

                Product createProduct = productService.createProduct(productData);

                assertEquals("name1", createProduct.getName());
                assertEquals("maker1", createProduct.getMaker());
                assertEquals(1000L, createProduct.getPrice());
                assertEquals("img1", createProduct.getImageUrl());

            }

        }

    }

    @Nested
    @DisplayName("updateProduct 메소드는")
    class Describe_updateProduct {

        @Nested
        @DisplayName("상품에 수정할 내용이 있다면")
        class Context_exist_update_product {

            ProductData updateData;
            Long validId;

            @BeforeEach
            void setUp() {

                Product product = createTestProduct();

                updateData = ProductData.builder()
                        .name("updateName")
                        .maker("updateMaker")
                        .price(5000L)
                        .imageUrl("updateImg")
                        .build();

                validId = product.getId();

            }

            @Test
            @DisplayName("상품을 수정한 후 수정한 상품을 리턴합니다.")
            void It_update_return_product() {

                Product updateProduct = productService.updateProduct(validId, updateData);

                assertEquals("updateName", updateProduct.getName());
                assertEquals("updateMaker", updateProduct.getMaker());
                assertEquals(5000L, updateProduct.getPrice());
                assertEquals("updateImg", updateProduct.getImageUrl());

            }

        }

        @Nested
        @DisplayName("수정한 상품을 찾을 수 없다면")
        class Context_exist_not_updateProduct {

            Long invalidId = 1000L;

            @BeforeEach
            void setUp() {

                productRepository.deleteAll();

            }

            @Test
            @DisplayName("ProductNotFoundException 예외를 던진다.")
            void It_return_noSuchElementException() {

                Assertions.assertThatThrownBy(() -> {
                    productService.getProduct(invalidId);
                }).isInstanceOf(ProductNotFoundException.class);

            }

        }

    }

    @Nested
    @DisplayName("deleteProduct 메소드는")
    class Describe_deleteProduct {

        @Nested
        @DisplayName("삭제할 상품이 있다면")
        class Context_exist_deleteProduct {

            Long validId;

            @BeforeEach
            void setUp() {

                Product product = createTestProduct();

                validId = product.getId();

            }

            @Test
            @DisplayName("삭제할 상품의 id를 받아 삭제합니다.")
            void It_delete_product() {

                productService.deleteProduct(validId);

                assertThatThrownBy(() -> productService.getProduct(validId))
                        .isInstanceOf(ProductNotFoundException.class);

            }

        }

    }

    private Product createTestProduct() {
        return productService.createProduct(ProductData.builder()
                .name("name1")
                .maker("maker1")
                .price(1000L)
                .imageUrl("img1")
                .build());
    }

    private ProductData createTestProductData() {
        return ProductData.builder()
                .name("name1")
                .maker("maker1")
                .price(1000L)
                .imageUrl("img1")
                .build();
    }

}

