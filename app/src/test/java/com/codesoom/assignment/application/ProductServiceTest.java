package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.ProductResponse;
import com.codesoom.assignment.errors.ProductNotFoundException;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("ProductService 클래스의")
class ProductServiceTest {
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductService productService = new ProductService(productRepository);
    private final ProductData GIVEN_DATA = ProductData.builder()
            .name("장난감")
            .maker("코드숨")
            .price(5000)
            .build();

    private final ProductData GIVEN_DATA_TO_CHANGE = ProductData.builder()
            .name("변경 장난감")
            .maker("변경 코드숨")
            .price(5555)
            .build();

    private final Product PRODUCT = Product.builder()
            .id(1L)
            .name("장난감")
            .maker("코드숨")
            .price(5000)
            .build();

    private final Product CHANGED_PRODUCT = Product.builder()
            .id(1L)
            .name("변경 장난감")
            .maker("변경 코드숨")
            .price(5555)
            .build();

    private final ProductResponse PRODUCT_RESPONSE = ProductResponse.from(PRODUCT);
    private final ProductResponse CHANGED_PRODUCT_RESPONSE = ProductResponse.from(CHANGED_PRODUCT);

    @BeforeEach
    void setUp() {
        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        given(productRepository.findAll()).willReturn(List.of(product));

        given(productRepository.findById(1L)).willReturn(Optional.of(product));
    }

    @Test
    void getProductsWithNoProduct() {
        given(productRepository.findAll()).willReturn(List.of());

        assertThat(productService.getProducts()).isEmpty();
    }

    @Test
    void getProducts() {
        List<Product> products = productService.getProducts();

        assertThat(products).isNotEmpty();

        Product product = products.get(0);

        assertThat(product.getName()).isEqualTo("쥐돌이");
    }

    @Test
    void getProductWithExsitedId() {
        Product product = productService.getProduct(1L);

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("쥐돌이");
    }

    @Test
    void getProductWithNotExsitedId() {
        assertThatThrownBy(() -> productService.getProduct(1000L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Nested
    @DisplayName("createProduct 메서드는")
    class Describe_createProduct {
        @Nested
        @DisplayName("상품 정보가 주어지면")
        class Context_with_productData {
            @BeforeEach
            void prepare() {
                given(productRepository.save(GIVEN_DATA.toProduct()))
                        .willReturn(PRODUCT);
            }

            @Test
            @DisplayName("생성된 상품 정보를 리턴한다")
            void It_returns_createdProductData() {
                assertThat(productService.createProduct(GIVEN_DATA))
                        .isEqualTo(PRODUCT_RESPONSE);

                verify(productRepository).save(any(Product.class));
            }
        }
    }

    @Test
    @DisplayName("변경할 상품 정보가 주어지면 상품 정보를 수정하고 리턴한다")
    void returnChangedProductWhenUpdateGivenData() {
        // Given
        given(productRepository.findById(1L)).willReturn(Optional.ofNullable(PRODUCT));

        // When
        ProductResponse productResponse = productService.updateProduct(1L, GIVEN_DATA_TO_CHANGE);

        // Then
        assertThat(productResponse).isEqualTo(CHANGED_PRODUCT_RESPONSE);
        verify(productRepository).findById(1L);
    }

    @Test
    void updateProductWithNotExistedId() {
        ProductData productData = ProductData.builder()
                .name("쥐순이")
                .maker("냥이월드")
                .price(5000)
                .build();

        assertThatThrownBy(() -> productService.updateProduct(1000L, productData))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void deleteProductWithExistedId() {
        productService.deleteProduct(1L);

        verify(productRepository).delete(any(Product.class));
    }

    @Test
    void deleteProductWithNotExistedId() {
        assertThatThrownBy(() -> productService.deleteProduct(1000L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
