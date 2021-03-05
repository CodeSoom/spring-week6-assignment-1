package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductCreateData;
import com.codesoom.assignment.dto.ProductResultData;
import com.codesoom.assignment.dto.ProductUpdateData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.ProductBadRequestException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@AutoConfigureMockMvc
@WebMvcTest(ProductController.class)
@DisplayName("ProductController 테스트")
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private WebApplicationContext wac;

    private Mapper mapper;

    private static final String EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4aXN0ZWRFbWFpbCIsInBhc3N3b3JkIjoiZXhpc3RlZFBhc3N3b3JkIn0." +
            "iqS2XKpt7blLuhlACfLFdomPsjXzC9RGW67mJGB0NaA";
    private static final String NOT_EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4aXN0ZWRFbWFpbCIsInBhc3N3b3JkIjoiZXhpc3RlZFBhc3N3b3JkIn0." +
            "iqS2XKpt7blLuhlACfLFdomPsjXzC9RGW67mJGB0Naa";

    private final String SETUP_PRODUCT_NAME = "setupName";
    private final String SETUP_PRODUCT_MAKER = "setupMaker";
    private final Integer SETUP_PRODUCT_PRICE = 100;
    private final String SETUP_PRODUCT_IMAGEURL = "setupImage";

    private final String CREATED_PRODUCT_NAME = "createdName";
    private final String CREATED_PRODUCT_MAKER = "createdMaker";
    private final Integer CREATED_PRODUCT_PRICE = 200;
    private final String CREATED_PRODUCT_IMAGEURL = "createdImage";

    private final String UPDATED_PRODUCT_NAME = "updatedName";
    private final String UPDATED_PRODUCT_MAKER = "updatedMaker";
    private final Integer UPDATED_PRODUCT_PRICE = 300;
    private final String UPDATED_PRODUCT_IMAGEURL = "updatedImage";

    private final Long EXISTED_ID = 1L;
    private final Long CREATED_ID = 2L;
    private final Long NOT_EXISTED_ID = 100L;

    private List<Product> products;
    private Product setupProductOne;
    private Product setupProductTwo;

    private List<ProductResultData> resultProducts;
    private ProductResultData resultProductOne;
    private ProductResultData resultProductTwo;

    public ProductResultData getProductResultData(Product product) {
        return ProductResultData.builder()
                .id(product.getId())
                .name(product.getName())
                .maker(product.getMaker())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).addFilter(((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        })).build();

        this.mapper = DozerBeanMapperBuilder.buildDefault();

        setupProductOne = Product.builder()
                .id(EXISTED_ID)
                .name(SETUP_PRODUCT_NAME)
                .maker(SETUP_PRODUCT_MAKER)
                .price(SETUP_PRODUCT_PRICE)
                .imageUrl(SETUP_PRODUCT_IMAGEURL)
                .build();

        setupProductTwo = Product.builder()
                .id(CREATED_ID)
                .name(CREATED_PRODUCT_NAME)
                .maker(CREATED_PRODUCT_MAKER)
                .price(CREATED_PRODUCT_PRICE)
                .imageUrl(CREATED_PRODUCT_IMAGEURL)
                .build();

        products = Arrays.asList(setupProductOne, setupProductTwo);

        resultProductOne = getProductResultData(setupProductOne);

        resultProductTwo = getProductResultData(setupProductTwo);
        resultProducts = Arrays.asList(resultProductOne, resultProductTwo);
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {
        @Test
        @DisplayName("전체 상품 목록과 OK를 리턴한다")
        void itReturnsProductsAndOKHttpStatus() throws Exception {
            given(productService.getProducts()).willReturn(resultProducts);

            mockMvc.perform(get("/products"))
                    .andExpect(content().string(containsString(SETUP_PRODUCT_NAME)))
                    .andExpect(content().string(containsString(CREATED_PRODUCT_NAME)))
                    .andExpect(status().isOk());

            verify(productService).getProducts();
        }
    }

    @Nested
    @DisplayName("detail 메서드는")
    class Describe_detail {
        @Nested
        @DisplayName("만약 저장되어 있는 상품의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 상품과 OK를 리턴한다")
            void itReturnsProductAndOKHttpStatus() throws Exception {
                given(productService.getProduct(givenExistedId)).willReturn(resultProductOne);

                mockMvc.perform(get("/products/"+ givenExistedId))
                        .andDo(print())
                        .andExpect(jsonPath("id").value(givenExistedId))
                        .andExpect(status().isOk());

                verify(productService).getProduct(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 상품의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("상품을 수 없다는 메세지와 NOT_FOUND를 응답한다")
            void itReturnsProductNotFoundMessageAndNOT_FOUNDHttpStatus() throws Exception {
                given(productService.getProduct(givenNotExistedId))
                        .willThrow(new ProductNotFoundException(givenNotExistedId));

                mockMvc.perform(get("/products/"+ givenNotExistedId))
                        .andDo(print())
                        .andExpect(content().string(containsString("Product not found")))
                        .andExpect(status().isNotFound());

                verify(productService).getProduct(givenNotExistedId);
            }
        }
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {
        @Nested
        @DisplayName("만약 상품이 주어진다면")
        class Context_WithProduct {
            private ProductCreateData productCreateData;
            private ProductResultData productResultData;

            @BeforeEach
            void setUp() {
                productCreateData = ProductCreateData.builder()
                        .name(CREATED_PRODUCT_NAME)
                        .maker(CREATED_PRODUCT_MAKER)
                        .price(CREATED_PRODUCT_PRICE)
                        .imageUrl(CREATED_PRODUCT_IMAGEURL)
                        .build();

                productResultData = mapper.map(productCreateData, ProductResultData.class);
            }

            @Test
            @DisplayName("상품을 저장하고 저장된 상품과 CREATED를 리턴한다")
            void itSaveProductAndReturnsSavedProductAndCreatedHttpStatus() throws Exception {
                given(productService.createProduct(any(ProductCreateData.class)))
                        .will(invocation -> {
                            ProductCreateData productCreateData = invocation.getArgument(0);
                            return ProductResultData.builder()
                                    .id(CREATED_ID)
                                    .name(productCreateData.getName())
                                    .maker(productCreateData.getMaker())
                                    .price(productCreateData.getPrice())
                                    .imageUrl(productCreateData.getImageUrl())
                                    .build();
                        });

                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
                        .content("{\"name\":\"createdName\" , \"maker\":\"createdMaker\", \"price\":200, \"imageUrl\":\"createdImage\"}")
                )
                        .andDo(print())
                        .andExpect(content().string(containsString("\"id\":2")))
                        .andExpect(content().string(containsString("\"name\":\"createdName\"")))
                        .andExpect(content().string(containsString("\"maker\":\"createdMaker\"")))
                        .andExpect(content().string(containsString("\"price\":200")))
                        .andExpect(content().string(containsString("\"imageUrl\":\"createdImage\"")))
                        .andExpect(status().isCreated());

                verify(productService).createProduct(any(ProductCreateData.class));
            }
        }

        @Nested
        @DisplayName("만약 이름값이 비어있는 상품이 주어진다면")
        class Context_WithProductWithoutName {
            @Test
            @DisplayName("요청이 잘못 되었다는 메세지와 BAD_REQUEST를 리턴한다")
            void itReturnsBadRequestMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.createProduct(any(ProductCreateData.class)))
                        .willThrow(new ProductBadRequestException("name"));

                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
                        .content("{\"name\":\"\" , \"maker\":\"createdMaker\", \"price\":200, \"imageUrl\":\"createdImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("name 값은 필수입니다")));
            }
        }

        @Nested
        @DisplayName("만약 메이커값이 비어있는 상품이 주어진다면")
        class Context_WithProductWithoutMaker {
            @Test
            @DisplayName("요청이 잘못 되었다는 메세지와 BAD_REQUEST를 리턴한다")
            void itReturnsBadRequestMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.createProduct(any(ProductCreateData.class)))
                        .willThrow(new ProductBadRequestException("maker"));

                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
                        .content("{\"name\":\"createdName\" , \"maker\":\"\", \"price\":200, \"imageUrl\":\"createdImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("maker 값은 필수입니다")));
            }
        }

        @Nested
        @DisplayName("만약 가격값이 비어있는 상품이 주어진다면")
        class Context_WithProductWithoutPrice {
            @Test
            @DisplayName("요청이 잘못 되었다는 메세지와 BAD_REQUEST를 리턴한다")
            void itReturnsBadRequestMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.createProduct(any(ProductCreateData.class)))
                        .willThrow(new ProductBadRequestException("price"));

                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
                        .content("{\"name\":\"createdName\" , \"maker\":\"createdMaker\", \"price\": null, \"imageUrl\":\"createdImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("price 값은 필수입니다")));
            }
        }

        @Nested
        @DisplayName("만약에 유효하지 않은 토큰이 주어진다면")
        class Context_WithInvalidToken {
            private final String givenNotExistedToken = NOT_EXISTED_TOKEN;

            @Test
            @DisplayName("토큰이 유효하지 않다는 메세지와 UNAUTHORIZED를 리턴한다")
            void itReturnsInvalidTokenMessageAndUNAUTHORIZEDHttpStatus() throws Exception {
                given(authenticationService.parseToken(eq(givenNotExistedToken)))
                        .willThrow(new InvalidTokenException(givenNotExistedToken));

                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + givenNotExistedToken)
                        .content("{\"name\":\"createdName\" , \"maker\":\"createdMaker\", \"price\":200, \"imageUrl\":\"createdImage\"}"))
                        .andDo(print())
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().string(containsString("Invalid token")));
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        @Nested
        @DisplayName("만약 저장되어 있는 상품의 아이디와 수정 할 상품이 주어진다면")
        class Context_WithExistedIdAndProduct {
            private final Long givenExistedId = EXISTED_ID;
            private ProductUpdateData productUpdateData;
            private ProductResultData productResultData;

            @BeforeEach
            void setUp() {
                productUpdateData = ProductUpdateData.builder()
                        .name(UPDATED_PRODUCT_NAME)
                        .maker(UPDATED_PRODUCT_MAKER)
                        .price(UPDATED_PRODUCT_PRICE)
                        .imageUrl(UPDATED_PRODUCT_IMAGEURL)
                        .build();

                productResultData = mapper.map(productUpdateData, ProductResultData.class);
            }

            @Test
            @DisplayName("주어진 아이디에 해당하는 상품을 수정하고 수정된 상품과 OK를 리턴한다")
            void itUpdatesProductAndReturnsUpdatedProductAndOKHttpStatus() throws Exception {
                given(productService.updateProduct(eq(givenExistedId), any(ProductUpdateData.class)))
                        .will(invocation -> {
                            Long givenExistedId = invocation.getArgument(0);
                            ProductUpdateData productUpdateData = invocation.getArgument(1);
                            return ProductResultData.builder()
                                    .id(givenExistedId)
                                    .name(productUpdateData.getName())
                                    .maker(productUpdateData.getMaker())
                                    .price(productUpdateData.getPrice())
                                    .imageUrl(resultProductOne.getImageUrl())
                                    .build();
                        });

                mockMvc.perform(patch("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
                        .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\":300, \"imageUrl\":\"updatedImage\"}"))
                        .andDo(print())
                        .andExpect(jsonPath("name").value(productResultData.getName()))
                        .andExpect(jsonPath("maker").value(productResultData.getMaker()))
                        .andExpect(jsonPath("price").value(productResultData.getPrice()))
                        .andExpect(jsonPath("imageUrl").value(resultProductOne.getImageUrl()))
                        .andExpect(status().isOk());

                verify(productService).updateProduct(eq(givenExistedId), any(ProductUpdateData.class));
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않는 상품의 아이디가 주어진다면")
        class Context_WithOutExistedId {
            private final Long givenNotExisted = NOT_EXISTED_ID;

            @Test
            @DisplayName("상품을 찾을 수 없다는 메세지와 NOT_FOUND를 응답한다")
            void itReturnsNotFoundMessageAndNOT_FOUNDHttpStatus() throws Exception {
                given(productService.updateProduct(eq(givenNotExisted), any(ProductUpdateData.class)))
                        .willThrow(new ProductNotFoundException(givenNotExisted));

                mockMvc.perform(patch("/products/" + givenNotExisted)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
                        .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\":300, \"imageUrl\":\"updatedImage\"}"))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(containsString("Product not found")));

                verify(productService).updateProduct(eq(givenNotExisted), any(ProductUpdateData.class));
            }
        }

        @Nested
        @DisplayName("만약 이름값이 비어있는 상품이 주어진다면")
        class Context_WithOutName {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("요청이 잘못 되었다는 메세지와 BAD_REQUEST를 응답한다")
            void itReturnsNotFoundMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.updateProduct(eq(givenExistedId), any(ProductUpdateData.class)))
                        .willThrow(new ProductBadRequestException("name"));

                mockMvc.perform(patch("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
                        .content("{\"name\":\"\" , \"maker\":\"updatedMaker\", \"price\":300, \"imageUrl\":\"updatedImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("name 값은 필수입니다")));
            }
        }

        @Nested
        @DisplayName("만약 메이커값이 비어있는 고양이 장난감 객체가 주어진다면")
        class Context_WithOutMaker {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("요청이 잘못 되었다는 메세지와 BAD_REQUEST를 응답한다")
            void itThrowsProductNotFoundExceptionAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.updateProduct(eq(givenExistedId), any(ProductUpdateData.class)))
                        .willThrow(new ProductBadRequestException("maker"));

                mockMvc.perform(patch("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
                        .content("{\"name\":\"updatedName\" , \"maker\":\"\", \"price\":300, \"imageUrl\":\"updatedImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("maker 값은 필수입니다")));
            }
        }

        @Nested
        @DisplayName("만약 가격값이 비어있는 고양이 장난감 객체가 주어진다면")
        class Context_WithOutPrice {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("요청이 잘못 되었다는 메세지와 BAD_REQUEST를 응답한다")
            void itReturnsNotFoundMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.updateProduct(eq(givenExistedId), any(ProductUpdateData.class)))
                        .willThrow(new ProductBadRequestException("price"));

                mockMvc.perform(patch("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
                        .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\": null, \"imageUrl\":\"updatedImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("price 값은 필수입니다")));
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 토큰이 주어진다면")
        class Context_WithNotValidToken {
            private final Long givenExistedId = EXISTED_ID;
            private final String givenNotExistedToken = NOT_EXISTED_TOKEN;

            @Test
            @DisplayName("토큰이 유효하지 않다는 메세지와 UNAHTORHIZED를 리턴한다")
            void itReturnsInvalidTokenMessageAndUNAUTHORIZEDHttpStatus() throws Exception {
                given(authenticationService.parseToken(eq(givenNotExistedToken)))
                        .willThrow(new InvalidTokenException(givenNotExistedToken));

                mockMvc.perform(patch("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + givenNotExistedToken)
                        .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\":300, \"imageUrl\":\"updatedImage\"}"))
                        .andDo(print())
                        .andExpect(content().string(containsString("Invalid token")))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("만약 저장되어 있는 상품의 아이디가 주어진다면")
        class Context_WithExistedId {
            private final Long givenExistedId = EXISTED_ID;

            @Test
            @DisplayName("주어진 아이디에 해당하는 상품을 삭제하고 삭제된 상품과 NO_CONTENT를 리턴한다")
            void itDeleteProductAndReturnsNO_CONTENTHttpStatus() throws Exception {
                given(productService.deleteProduct(givenExistedId)).willReturn(resultProductOne);

                mockMvc.perform(delete("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN))
                        .andDo(print())
                        .andExpect(jsonPath("id").value(givenExistedId))
                        .andExpect(status().isNoContent());

                verify(productService).deleteProduct(givenExistedId);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 상품의 아이디가 주어진다면")
        class Context_WithNotExistedId {
            private final Long givenNotExistedId = NOT_EXISTED_ID;

            @Test
            @DisplayName("상품을 찾을 수 없다는 메세지와 NOT_FOUND를 리턴한다")
            void itReturnsNotFoundMessageAndNOT_FOUNDHttpStatus() throws Exception {
                given(productService.deleteProduct(givenNotExistedId))
                        .willThrow(new ProductNotFoundException(givenNotExistedId));

                mockMvc.perform(delete("/products/" + givenNotExistedId)
                        .header("Authorization", "Bearer " + EXISTED_TOKEN))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(containsString("Product not found")));

                verify(productService).deleteProduct(givenNotExistedId);
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 토큰이 주어진다면")
        class ContextWith_NotValidToken {
            private final Long givenExistedId = EXISTED_ID;
            private final String givenNotExistedToken = NOT_EXISTED_TOKEN;

            @Test
            @DisplayName("토큰이 유효하지 않다는 메세지와 UNAUTHORIZED를 리턴한다")
            void itReturnsInvalidTokenMessageAndUNAUTHROIZEDHttpStatus() throws Exception {
                given(authenticationService.parseToken(eq(givenNotExistedToken)))
                        .willThrow(new InvalidTokenException(givenNotExistedToken));

                mockMvc.perform((delete("/products/" + givenExistedId))
                        .header("Authorization", "Bearer " + givenNotExistedToken))
                        .andDo(print())
                        .andExpect(content().string(containsString("Invalid token")))
                        .andExpect(status().isUnauthorized());
            }
        }
    }
}
