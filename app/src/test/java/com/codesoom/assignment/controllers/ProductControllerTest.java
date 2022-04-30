package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.FakeProductService;
import com.codesoom.assignment.controllers.product.ProductController;
import com.codesoom.assignment.domain.product.Product;
import com.codesoom.assignment.domain.product.ProductRepository;
import com.codesoom.assignment.dto.product.CreateProductRequest;
import com.codesoom.assignment.dto.product.SearchOneProductRequest;
import com.codesoom.assignment.errors.ProductNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ProductControllerTest {


    private MockMvc mockMvc;
    private FakeProductService fakeProductService;

    @MockBean
    private ProductRepository repository;


    @BeforeEach
    public void setUp() {
        fakeProductService = new FakeProductService();
        ProductController productController = new ProductController(fakeProductService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(ControllerErrorAdvice.class)
                .build();
    }


    @Nested
    @DisplayName("상품 조회 요청은")
    class WhenProductDetailRequest {

        private Product setUpProduct;

        @BeforeEach
        public void setUpForRemoveTest() {
            CreateProductRequest createProductRequest = new CreateProductRequest("홍길동전", "허균", 400, null);
            setUpProduct = fakeProductService.createProduct(createProductRequest);
        }

        @Nested
        @DisplayName("조회 요청한 ID에 상품이 존재한다면")
        class IfExistProductSearchById {
            @Test
            @DisplayName("상태코드 200을 반환하며 상품에 대한 정보를 전달한다")
            void returnStatusOkAndProductInfo() throws Exception {
                mockMvc.perform(get("/products/" + setUpProduct.getId()))
                        .andExpect(status().isOk());

                SearchOneProductRequest searchOneProductRequest = SearchOneProductRequest.createSearchRequestObjectFrom(setUpProduct.getId());
                Product searchProduct = fakeProductService.getProduct(searchOneProductRequest);

                assertThat(searchProduct.equals(setUpProduct)).isTrue();
            }
        }

    }

    @Nested
    @DisplayName("상품 삭제 요청은")
    class WhenProductRemoveRequest {
        private long productId;

        @BeforeEach
        public void setUpForRemoveTest() {
            CreateProductRequest createProductRequest = new CreateProductRequest("홍길동전", "허균", 400, null);
            productId = fakeProductService.createProduct(createProductRequest)
                    .getId();

        }

        @Nested
        @DisplayName("등록된 상품 존재한다면")
        class IfExistProduct {
            @Test
            @DisplayName("상태코드 204를 반환하며 상품을 삭제한다")
            void returnStatusCodeNoContentAndRemoveProduct() throws Exception {
                mockMvc.perform(delete("/products/" + productId))
                        .andExpect(status().isNoContent());

                Assertions.assertThrows(ProductNotFoundException.class, () -> {
                    fakeProductService.findProduct(productId);
                });
            }
        }

        @Nested
        @DisplayName("등록된 상품이 없다면")
        class IfDontAlreadyExistProduct {
            private final Long notExistId = 100L;

            @Test
            @DisplayName("상태코드 404를 반환하며 상품 삭제가 실패한다")
            void returnStatusCodeNotFountAndFailRemoveProduct() throws Exception {
                mockMvc.perform(delete("/products/" + notExistId))
                        .andExpect(status().isNotFound());
            }

        }

    }

}
