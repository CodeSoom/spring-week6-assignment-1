package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * ProductService에 대한 테스트 클래스
 */
class ProductServiceTest {
    private static final Long ID = 1L;
    private static final Long NON_EXISTING_ID = 1000L;
    private static final String NAME = "쥐돌이";
    private static final String NEW_NAME = "쥐순이";
    private static final String MAKER = "냥이월드";
    private static final int PRICE = 5000;

    private ProductService productService;

    private final ProductRepository productRepository = mock(ProductRepository.class);

    private Product product;
    private ProductData productData;

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        productService = new ProductService(mapper, productRepository);

        product = Product.builder()
                .id(ID)
                .name(NAME)
                .maker(MAKER)
                .price(PRICE)
                .build();

        productData = ProductData.builder()
                .name(NAME)
                .maker(MAKER)
                .price(PRICE)
                .build();
    }

    @Nested
    @DisplayName("getProducts 메서드는")
    class Describe_getProducts_method {
        @Nested
        @DisplayName("product가 저장되어 있지 않은 경우")
        class Context_if_no_product_stored {
            @BeforeEach
            void setUp() {
                given(productRepository.findAll()).willReturn(List.of());
            }

            @Nested
            @DisplayName("비어있는 리스트를 반환한다")
            class It_returns_list_contains_no_proudct {
                List<Product> products() {
                    return productRepository.findAll();
                }

                @Test
                void test() {
                    assertThat(products()).isEmpty();
                }
            }
        }

        @Nested
        @DisplayName("product가 1개 저장되어 있는 경우")
        class Context_if_one_product_stored {
            @BeforeEach
            void setUp() {
                given(productRepository.findAll()).willReturn(List.of(product));
            }

            @Nested
            @DisplayName("1개의 product가 저장된 리스트를 반환한다")
            class It_returns_list_contains_one_proudct {
                List<Product> products() {
                    return productRepository.findAll();
                }

                @Test
                void test() {
                    assertThat(products()).isNotEmpty();

                    Product product = products().get(0);
                    assertThat(product.getName()).isEqualTo(NAME);
                }
            }
        }
    }

    @Nested
    @DisplayName("getProduct 메서드는")
    class Describe_getProduct_method {
        @Nested
        @DisplayName("존재하는 id가 주어진 경우")
        class Context_if_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productRepository.findById(ID)).willReturn(Optional.of(product));
            }

            @Nested
            @DisplayName("id에 해당하는 product를 반환한다")
            class It_returns_product {
                Product product() {
                    return productService.getProduct(ID);
                }

                @Test
                void test() {
                    assertThat(product()).isNotNull();
                    assertThat(product().getName()).isEqualTo(NAME);
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어진 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productRepository.findById(NON_EXISTING_ID))
                        .willThrow(new ProductNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("ProductNotFoundException 예외를 던진다")
            class It_throws_productNotFoundException {
                Product product() {
                    return productService.getProduct(NON_EXISTING_ID);
                }

                @Test
                void test() {
                    assertThatThrownBy(() -> product())
                            .isInstanceOf(ProductNotFoundException.class);
                }
            }
        }
    }

    @Nested
    @DisplayName("createProduct 메서드는")
    class Describe_createProduct_method {
        @Nested
        @DisplayName("모든 속성이 유효한 product가 주어졌을 경우")
        class Context_if_product_with_valid_attributes_given {
            @BeforeEach
            void setUp() {
                given(productRepository.save(any(Product.class)))
                        .will(invocation -> {
                            Product source = invocation.getArgument(0);
                            return Product.builder()
                                    .id(ID)
                                    .name(source.getName())
                                    .maker(source.getMaker())
                                    .price(source.getPrice())
                                    .build();
                        });
            }

            @Nested
            @DisplayName("product를 반환한다")
            class It_returns_product {
                @Test
                void test() {
                    Product product = productService.createProduct(productData);

                    assertThat(product.getId()).isEqualTo(ID);
                    assertThat(product.getName()).isEqualTo(NAME);
                    assertThat(product.getMaker()).isEqualTo(MAKER);

                    verify(productRepository).save(any(Product.class));
                }
            }
        }
    }

    @Nested
    @DisplayName("updateProduct 메서드는")
    class Describe_updateProduct_method {
        @BeforeEach
        void setUp() {
            productData = ProductData.builder()
                    .name(NEW_NAME)
                    .maker(MAKER)
                    .price(PRICE)
                    .build();
        }

        @Nested
        @DisplayName("존재하는 id가 주어졌을 경우")
        class Context_if_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productRepository.findById(ID)).willReturn(Optional.of(product));
            }

            @Nested
            @DisplayName("product를 반환한다")
            class It_returns_product {
                Product product() {
                    return productService.updateProduct(ID, productData);
                }

                @Test
                void test() {
                    assertThat(product().getId()).isEqualTo(ID);
                    assertThat(product().getName()).isEqualTo(NEW_NAME);
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어졌을 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productRepository.findById(NON_EXISTING_ID))
                        .willThrow(new ProductNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("ProductNotFoundException 예외를 던진다")
            class It_throws_productNotFoundException {
                Product product() {
                    return productService.updateProduct(NON_EXISTING_ID, productData);
                }

                @Test
                void test() {
                    assertThatThrownBy(() -> product())
                            .isInstanceOf(ProductNotFoundException.class);
                }
            }
        }
    }

    @Nested
    @DisplayName("deleteProduct 메서드는")
    class Describe_deleteProduct_method {
        @Nested
        @DisplayName("존재하는 id가 주어졌을 경우")
        class Context_if_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productRepository.findById(ID)).willReturn(Optional.of(product));

                productService.deleteProduct(ID);
            }

            @Nested
            @DisplayName("product를 반환한다")
            class It_returns_product {
                @Test
                void test() {
                    verify(productRepository).delete(any(Product.class));
                }
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어졌을 경우")
        class Context_if_non_existing_id_given {
            @BeforeEach
            void setUp() {
                given(productRepository.findById(NON_EXISTING_ID))
                        .willThrow(new ProductNotFoundException(NON_EXISTING_ID));
            }

            @Nested
            @DisplayName("ProductNotFoundException 예외를 던진다")
            class It_throws_productNotFoundException {
                Product product() {
                    return productService.deleteProduct(NON_EXISTING_ID);
                }

                @Test
                void test() {
                    assertThatThrownBy(() -> product())
                            .isInstanceOf(ProductNotFoundException.class);
                }
            }
        }
    }
}
