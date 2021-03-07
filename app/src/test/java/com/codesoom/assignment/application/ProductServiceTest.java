package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductCreateData;
import com.codesoom.assignment.dto.ProductResultData;
import com.codesoom.assignment.dto.ProductUpdateData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("ProductService 테스트")
class ProductServiceTest {
    private Mapper dozerMapper;
    private ProductRepository productRepository;
    private ProductService productService;

    private static final String SETUP_PRODUCT_NAME = "setupName";
    private static final String SETUP_PRODUCT_MAKER = "setupMaker";
    private static final Integer SETUP_PRODUCT_PRICE = 100;
    private static final String SETUP_PRODUCT_IMAGEURL = "setupImage";

    private static final String CREATED_PRODUCT_NAME = "createdName";
    private static final String CREATED_PRODUCT_MAKER = "createdMaker";
    private static final Integer CREATED_PRODUCT_PRICE = 200;
    private static final String CREATED_PRODUCT_IMAGEURL = "createdImage";

    private static final String UPDATED_PRODUCT_NAME = "updatedName";
    private static final String UPDATED_PRODUCT_MAKER = "updatedMaker";
    private static final Integer UPDATED_PRODUCT_PRICE = 300;
    private static final String UPDATED_PRODUCT_IMAGEURL = "updatedImage";

    private static final Long EXISTED_ID = 1L;
    private static final Long CREATED_ID = 2L;
    private static final Long NOT_EXISTED_ID = 100L;

    private List<Product> products;
    private Product setupProduct;
    private Product createProduct;

    private List<ProductResultData> resultProducts;
    private ProductResultData resultProductOne;
    private ProductResultData resultProductTwo;

    @BeforeEach
    void setUp() {
        dozerMapper = DozerBeanMapperBuilder.buildDefault();
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(dozerMapper, productRepository);

        setupProduct = Product.builder()
                .id(EXISTED_ID)
                .name(SETUP_PRODUCT_NAME)
                .maker(SETUP_PRODUCT_MAKER)
                .price(SETUP_PRODUCT_PRICE)
                .imageUrl(SETUP_PRODUCT_IMAGEURL)
                .build();

        createProduct = Product.builder()
                .id(CREATED_ID)
                .name(CREATED_PRODUCT_NAME)
                .maker(CREATED_PRODUCT_MAKER)
                .price(CREATED_PRODUCT_PRICE)
                .imageUrl(CREATED_PRODUCT_IMAGEURL)
                .build();

        products = Arrays.asList(setupProduct, createProduct);

        resultProductOne = ProductResultData.of(setupProduct);
        resultProductTwo = ProductResultData.of(createProduct);
        resultProducts = Arrays.asList(resultProductOne, resultProductTwo);
    }

    @Nested
    @DisplayName("getProducts 메서드는")
    class Describe_getProducts {
        @Nested
        @DisplayName("만약 상품 목록이 존재한다면")
        class Context_ExistsListOfProducts {
            @Test
            @DisplayName("저장되어 있는 상품 목록을 리턴한다")
            void itReturnsListOfProducts() {
                given(productRepository.findAll()).willReturn(products);

                List<ProductResultData> list = productService.getProducts();
                assertThat(list).containsExactly(resultProductOne, resultProductTwo);

                verify(productRepository).findAll();
            }
        }

        @Nested
        @DisplayName("만약 상품 목록이 존재하지 않는다면")
        class Context_NotExistsListOfProduct {
            @Test
            @DisplayName("비어있는 상품 목록을 리턴한다")
            void itReturnsEmptyListOfProducts() {
                given(productRepository.findAll()).willReturn(List.of());

                List<ProductResultData> list = productService.getProducts();

                assertThat(list).isEmpty();

                verify(productRepository).findAll();
            }
        }
    }

    @Nested
    @DisplayName("getProduct 메서드는")
    class Describe_getProduct {
        @Nested
        @DisplayName("만약 저장되어 있는 상품의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 상품을 리턴한다")
            void itReturnsWithExistedProduct() {
                given(productRepository.findById(givenExistedId)).willReturn(Optional.of(setupProduct));

                ProductResultData product = productService.getProduct(givenExistedId);
                assertThat(product.getId()).isEqualTo(givenExistedId);

                verify(productRepository).findById(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않는 상품의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("상품을 찾을 수 없다는 예외를 던진다")
            void itThrowsProductNotFoundException() {
                assertThatThrownBy(() -> productService.getProduct(givenNotExistedId))
                        .isInstanceOf(ProductNotFoundException.class)
                        .hasMessageContaining("Product not found");

                verify(productRepository).findById(givenNotExistedId);
            }
        }
    }

    @Nested
    @DisplayName("createProduct 메서드는")
    class Describe_class {
        @Nested
        @DisplayName("만약 상품이 주어진다면")
        class Content_WithProduct {
            private ProductCreateData productCreateData;

            @BeforeEach
            void setUp() {
                productCreateData = ProductCreateData.builder()
                        .name(CREATED_PRODUCT_NAME)
                        .maker(CREATED_PRODUCT_MAKER)
                        .price(CREATED_PRODUCT_PRICE)
                        .imageUrl(CREATED_PRODUCT_IMAGEURL)
                        .build();
            }

            @Test
            @DisplayName("상품을 저장하고 저장된 상품를 리턴한다")
            void itSavesProductAndReturnsSavedProduct() {
                given(productRepository.save(any(Product.class))).willReturn(createProduct);

                ProductResultData productResultData = productService.createProduct(productCreateData);

                assertThat(productResultData.getId()).isEqualTo(createProduct.getId());
                assertThat(productResultData.getName()).isEqualTo(productCreateData.getName());
                assertThat(productResultData.getMaker()).isEqualTo(productCreateData.getMaker());
                assertThat(productResultData.getPrice()).isEqualTo(productCreateData.getPrice());
                assertThat(productResultData.getImageUrl()).isEqualTo(productCreateData.getImageUrl());

                verify(productRepository).save(any(Product.class));
            }
        }
    }

    @Nested
    @DisplayName("updateProduct 메서드는")
    class Describe_update {
        @Nested
        @DisplayName("만약 저징되어 있는 상품의 아이디와 수정 할 상품이 주어진다면")
        class Context_WithExistedIdAndProduct {
            private final Long givenExistedId = EXISTED_ID;
            private ProductUpdateData productUpdateData;

            @BeforeEach
            void setUp() {
                productUpdateData = ProductUpdateData.builder()
                        .name(UPDATED_PRODUCT_NAME)
                        .maker(UPDATED_PRODUCT_MAKER)
                        .price(UPDATED_PRODUCT_PRICE)
                        .imageUrl(UPDATED_PRODUCT_IMAGEURL)
                        .build();
            }

            @Test
            @DisplayName("주어진 아이디에 해당하는 상품을 수정하고 수정된 상품을 리턴한다")
            void itUpdatesProductAndReturnsUpdatedProduct() {
                given(productRepository.findById(givenExistedId)).willReturn(Optional.of(setupProduct));

                ProductResultData productResultData = productService.updateProduct(givenExistedId, productUpdateData);

                assertThat(productResultData.getId()).isEqualTo(givenExistedId);
                assertThat(productResultData.getName()).isEqualTo(productUpdateData.getName());
                assertThat(productResultData.getMaker()).isEqualTo(productUpdateData.getMaker());
                assertThat(productResultData.getPrice()).isEqualTo(productUpdateData.getPrice());

                verify(productRepository).findById(givenExistedId);
            }
        }
    }

    @Nested
    @DisplayName("deleteProduct 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("만약 저장되어 있는 상품의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 상품을 삭제하고 삭제된 상품을 리턴한다")
            void itDeletesProductAndReturnsDeletedProduct() {
                given(productRepository.findById(givenExistedId)).willReturn(Optional.of(setupProduct));

                ProductResultData productResultData = productService.deleteProduct(givenExistedId);

                assertThat(productResultData.getId()).isEqualTo(givenExistedId);
                assertThat(productResultData.getName()).isEqualTo(setupProduct.getName());
                assertThat(productResultData.getMaker()).isEqualTo(setupProduct.getMaker());
                assertThat(productResultData.getPrice()).isEqualTo(setupProduct.getPrice());
                assertThat(productResultData.getImageUrl()).isEqualTo(setupProduct.getImageUrl());

                verify(productRepository).delete(setupProduct);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 상품의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("상품을 찾을 수 없다는 예외를 던진다")
            void itThrowsProductNotFoundException() {
                given(productRepository.findById(givenNotExistedId))
                        .willThrow(new ProductNotFoundException(givenNotExistedId));

                assertThatThrownBy(() -> productService.deleteProduct(givenNotExistedId))
                        .isInstanceOf(ProductNotFoundException.class)
                        .hasMessageContaining("Product not found");

                verify(productRepository).findById(givenNotExistedId);
            }
        }
    }
}
