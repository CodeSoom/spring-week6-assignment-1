package com.codesoom.assignment.product.service;

import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.product.dto.ProductData;
import com.codesoom.assignment.product.exception.ProductNotFoundException;
import com.codesoom.assignment.product.repository.ProductRepository;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@Nested
@DisplayName("ProductService 클래스")
class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;

    private Product givenProduct;
    private final List<Product> givenProductList = new ArrayList<>();
    private static Long EXIST_ID;
    private static Long NOT_EXIST_ID;

    private static final String NAME = "Test Name";
    private static final String MAKER = "Test Maker";
    private static final int PRICE = 10000;
    private static final String IMAGE_URL = "test Image Url";

    @BeforeEach
    public void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository, mapper);

        setUpFixtures();
        setUpSaveTask();
    }

    void setUpFixtures() {
        givenProduct = Product.builder()
                .name(NAME)
                .maker(MAKER)
                .price(PRICE)
                .imageUrl(IMAGE_URL)
                .build();

        givenProductList.add(givenProduct);
        EXIST_ID = givenProduct.getId();
        NOT_EXIST_ID = EXIST_ID + 100L;

        given(productRepository.findAll()).willReturn(givenProductList);
        given(productRepository.findById(EXIST_ID)).willReturn(Optional.of(givenProduct));
        given(productRepository.findById(NOT_EXIST_ID)).willReturn(Optional.empty());
    }

    void setUpSaveTask() {
        given(productRepository.save(any(Product.class))).will(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(2L);
            givenProductList.add(product);
            return product;
        });
    }

    @Nested
    @DisplayName("getProducts 메서드는")
    class getAllProducts {
        @Test
        @DisplayName("장난감 목록 전체를 반환한다.")
        void getProducts() {
            List<Product> products = productService.getProducts();

            Assertions.assertThat(products.size()).isEqualTo(givenProductList.size());
        }
    }

    @Nested
    @DisplayName("findProductById 메서드는")
    class findProductById {
        @Test
        @DisplayName("존재하는 식별자일 때 일치하는 장난감을 반환한다.")
        void findProductByExistedId() {
            Product product = productService.findProductById(EXIST_ID);

            Assertions.assertThat(product.getName()).isEqualTo("Test Name");
        }

        @Test
        @DisplayName("식별자와 일치하는 장난감이 존재하지 않을 때 예외를 반환한다.")
        void findProductByNotExistedId() {
            assertThatThrownBy(() -> productService.findProductById(NOT_EXIST_ID))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("addProduct 메서드는")
    class addProduct {
        ProductData source = ProductData.builder()
                .name("Test Name2")
                .maker("Test Maker2")
                .price(20000)
                .imageUrl("test Image Url2")
                .build();

        @Test
        @DisplayName("요청된 장난감을 저장하고 반환한다.")
        void addProduct() {
            int oldSize = productService.getProducts().size();
            Product product = productService.addProduct(source);
            int newSize = productService.getProducts().size();

            Assertions.assertThat(product.getId()).isEqualTo(2L);
            Assertions.assertThat(newSize - oldSize).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("updateProduct 메서드는")
    class updateProduct {
        ProductData source = ProductData.builder()
                .name("Test Name2")
                .maker("Test Maker2")
                .price(20000)
                .imageUrl("test Image Url2")
                .build();

        @Test
        @DisplayName("존재하는 식별자일 때 요청된 장난감을 수정한다.")
        void updateProductWithExistedID() {
            Product product = productService.updateProduct(EXIST_ID, source);

            Assertions.assertThat(productService.findProductById(EXIST_ID).getName()).isEqualTo(product.getName());
        }

        @Test
        @DisplayName("존재하지 않는 식별자일 때 예외를 반환한다.")
        void updateProductWithNotExistedID() {
            assertThatThrownBy(() -> productService.updateProduct(NOT_EXIST_ID, source))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteProductById 메서드는")
    class deleteProductById {
        @BeforeEach
        void prepareTest() {
            given(productRepository.findById(EXIST_ID)).willReturn(Optional.of(givenProduct));
        }

        @Test
        @DisplayName("존재하는 식별자일 때 요청된 장난감을 삭제한다.")
        void deleteProductByExistedId() {
            productService.deleteProductById(EXIST_ID);
//            assertThatThrownBy(() -> productService.findProductById(EXIST_ID))
//                    .isInstanceOf(ProductNotFoundException.class);
        }

        @Test
        @DisplayName("존재하지 않는 식별자일 때 요청된 장난감을 삭제한다.")
        void deleteProductByNotExistedId() {
            assertThatThrownBy(() -> productService.deleteProductById(NOT_EXIST_ID))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }
}
