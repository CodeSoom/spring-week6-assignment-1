package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductCreateData;
import com.codesoom.assignment.dto.ProductResultData;
import com.codesoom.assignment.dto.ProductUpdateData;
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

    @Autowired
    private WebApplicationContext wac;

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

    private final Mapper mapper = DozerBeanMapperBuilder.buildDefault();
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
                given(productService.createProduct(any(ProductCreateData.class))).willReturn(productResultData);

                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"createdName\" , \"maker\":\"createdMaker\", \"price\":200, \"image\":\"createdImage\"}"))
                        .andDo(print())
                        .andExpect(jsonPath("name").value(productResultData.getName()))
                        .andExpect(jsonPath("maker").value(productResultData.getMaker()))
                        .andExpect(jsonPath("price").value(productResultData.getPrice()))
                        .andExpect(jsonPath("imageUrl").value(productResultData.getImageUrl()))
                        .andExpect(status().isCreated());

                verify(productService).createProduct(any(ProductCreateData.class));
            }
        }

        @Nested
        @DisplayName("만약 이름값이 비어있는 상품이 주어진다면")
        class Context_WithProductWithoutName {
            @Test
            @DisplayName("요청이 잘못됐다는 메세지와 BAD_REQUEST를 리턴한다")
            void itReturnsBadRequestMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.createProduct(any(ProductCreateData.class)))
                        .willThrow(new ProductBadRequestException("name"));

                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\" , \"maker\":\"createdMaker\", \"price\":200, \"image\":\"createdImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("name 값은 필수입니다")));
            }
        }

        @Nested
        @DisplayName("만약 메이커값이 비어있는 상품이 주어진다면")
        class Context_WithProductWithoutMaker {
            @Test
            @DisplayName("요청이 잘못됐다는 메세지와 BAD_REQUEST를 리턴한다")
            void itReturnsBadRequestMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.createProduct(any(ProductCreateData.class)))
                        .willThrow(new ProductBadRequestException("maker"));

                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"createdName\" , \"maker\":\"\", \"price\":200, \"image\":\"createdImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("maker 값은 필수입니다")));
            }
        }

        @Nested
        @DisplayName("만약 가격값이 비어있는 상품이 주어진다면")
        class Context_WithProductWithoutPrice {
            @Test
            @DisplayName("요청이 잘못됐다는 메세지와 BAD_REQUEST를 리턴한다")
            void itReturnsBadRequestMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.createProduct(any(ProductCreateData.class)))
                        .willThrow(new ProductBadRequestException("price"));

                mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"createdName\" , \"maker\":\"createdMaker\", \"price\": null, \"image\":\"createdImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("price 값은 필수입니다")));
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
                productUpdateData = productUpdateData.builder()
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
                given(productService.updateProduct(eq(givenExistedId), any(ProductUpdateData.class))).willReturn(productResultData);

                mockMvc.perform(patch("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\":300, \"image\":\"updatedImage\"}"))
                        .andDo(print())
                        .andExpect(jsonPath("name").value(productResultData.getName()))
                        .andExpect(jsonPath("maker").value(productResultData.getMaker()))
                        .andExpect(jsonPath("price").value(productResultData.getPrice()))
                        .andExpect(jsonPath("imageUrl").value(productResultData.getImageUrl()))
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
                        .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\":300, \"image\":\"updatedImage\"}"))
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
            @DisplayName("요청이 잘못 됐다는 메세지와 BAD_REQUEST를 응답한다")
            void itReturnsNotFoundMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.updateProduct(eq(givenExistedId), any(ProductUpdateData.class)))
                        .willThrow(new ProductBadRequestException("name"));

                mockMvc.perform(patch("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\" , \"maker\":\"updatedMaker\", \"price\":300, \"image\":\"updatedImage\"}"))
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
            @DisplayName("요청이 잘못 됐다는 메세지와 BAD_REQUEST를 응답한다")
            void itThrowsProductNotFoundExceptionAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.updateProduct(eq(givenExistedId), any(ProductUpdateData.class)))
                        .willThrow(new ProductBadRequestException("maker"));

                mockMvc.perform(patch("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"updatedName\" , \"maker\":\"\", \"price\":300, \"image\":\"updatedImage\"}"))
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
            @DisplayName("요청이 잘못 됐다는 메세지와 BAD_REQUEST를 응답한다")
            void itReturnsNotFoundMessageAndBAD_REQUESTHttpStatus() throws Exception {
                given(productService.updateProduct(eq(givenExistedId), any(ProductUpdateData.class)))
                        .willThrow(new ProductBadRequestException("price"));

                mockMvc.perform(patch("/products/" + givenExistedId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"updatedName\" , \"maker\":\"updatedMaker\", \"price\": null, \"image\":\"updatedImage\"}"))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("price 값은 필수입니다")));
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

                mockMvc.perform(delete("/products/" + givenExistedId))
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

                mockMvc.perform(delete("/products/" + givenNotExistedId))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(containsString("Product not found")));

                verify(productService).deleteProduct(givenNotExistedId);
            }
        }
    }
}
//package com.codesoom.assignment.controllers;
//
//        import com.codesoom.assignment.application.AuthenticationService;
//        import com.codesoom.assignment.application.ProductService;
//        import com.codesoom.assignment.domain.Product;
//        import com.codesoom.assignment.dto.ProductData;
//        import com.codesoom.assignment.errors.InvalidAccessTokenException;
//        import com.codesoom.assignment.errors.ProductNotFoundException;
//        import com.codesoom.assignment.util.JwtUtil;
//        import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//        import org.junit.jupiter.api.BeforeEach;
//        import org.junit.jupiter.api.Test;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//        import org.springframework.boot.test.mock.mockito.MockBean;
//        import org.springframework.http.MediaType;
//        import org.springframework.test.web.servlet.MockMvc;
//
//        import java.util.List;
//
//        import static org.hamcrest.Matchers.containsString;
//        import static org.mockito.ArgumentMatchers.any;
//        import static org.mockito.ArgumentMatchers.eq;
//        import static org.mockito.BDDMockito.given;
//        import static org.mockito.Mockito.verify;
//        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ProductController.class)
//class ProductControllerTest {
//    private static final String EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
//            "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
//    private static final String NOT_EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
//            "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGW";
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductService productService;
//
//    @MockBean
//    private AuthenticationService authenticationService;
//
//    @BeforeEach
//    void setUp() {
//        Product product = Product.builder()
//                .id(1L)
//                .name("쥐돌이")
//                .maker("냥이월드")
//                .price(5000)
//                .build();
//        given(productService.getProducts()).willReturn(List.of(product));
//
//        given(productService.getProduct(1L)).willReturn(product);
//
//        given(productService.getProduct(1000L))
//                .willThrow(new ProductNotFoundException(1000L));
//
//        given(productService.createProduct(any(ProductData.class)))
//                .willReturn(product);
//
//        given(productService.updateProduct(eq(1L), any(ProductData.class)))
//                .will(invocation -> {
//                    Long id = invocation.getArgument(0);
//                    ProductData productData = invocation.getArgument(1);
//                    return Product.builder()
//                            .id(id)
//                            .name(productData.getName())
//                            .maker(productData.getMaker())
//                            .price(productData.getPrice())
//                            .build();
//                });
//
//        given(productService.updateProduct(eq(1000L), any(ProductData.class)))
//                .willThrow(new ProductNotFoundException(1000L));
//
//        given(productService.deleteProduct(1000L))
//                .willThrow(new ProductNotFoundException(1000L));
//
////        given(jwtUtil.decode(NOT_EXISTED_TOKEN)).will(invocation -> {
////                    String token = invocation.getArgument(0);
////                    return new JwtUtil("12345678901234567890123456789010")
////                            .decode(token);
////                });
//        given(authenticationService.parseToken(EXISTED_TOKEN)).willReturn(1L);
//
//        given((authenticationService.parseToken(NOT_EXISTED_TOKEN)))
//                .willThrow(new InvalidAccessTokenException(NOT_EXISTED_TOKEN));
//
//        given((authenticationService.parseToken(null)))
//                .willThrow(new InvalidAccessTokenException(null));
//    }
//
//    @Test
//    void list() throws Exception {
//        mockMvc.perform(
//                get("/products")
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//        )
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("쥐돌이")));
//    }
//
//    @Test
//    void deatilWithExsitedProduct() throws Exception {
//        mockMvc.perform(
//                get("/products/1")
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//        )
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("쥐돌이")));
//    }
//
//    @Test
//    void deatilWithNotExsitedProduct() throws Exception {
//        mockMvc.perform(get("/products/1000"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void createWithValidAttributes() throws Exception {
//        mockMvc.perform(
//                post("/products")
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
//                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
//                                "\"price\":5000}")
//        )
//                .andExpect(status().isCreated())
//                .andExpect(content().string(containsString("쥐돌이")));
//
//        verify(productService).createProduct(any(ProductData.class));
//    }
//
//    @Test
//    void createWithInvalidAttributes() throws Exception {
//        mockMvc.perform(
//                post("/products")
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
//                        .content("{\"name\":\"\",\"maker\":\"\"," +
//                                "\"price\":0}")
//        )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void createWithAccessToken() throws Exception {
//        mockMvc.perform(
//                post("/products")
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + EXISTED_TOKEN)
//                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
//                                "\"price\":5000}")
//        )
//                .andExpect(status().isCreated())
//                .andExpect(content().string(containsString("쥐돌이")));
//
//        verify(productService).createProduct(any(ProductData.class));
//    }
//
//    @Test
//    void createWithWrongAccessToken() throws Exception {
//        mockMvc.perform(
//                post("/products")
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + NOT_EXISTED_TOKEN)
//                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
//                                "\"price\":5000}")
//        )
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void createWithoutAccessToken() throws Exception {
//        mockMvc.perform(
//                post("/products")
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\"," +
//                                "\"price\":5000}")
//        )
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void updateWithExistedProduct() throws Exception {
//        mockMvc.perform(
//                patch("/products/1")
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
//                                "\"price\":5000}")
//        )
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("쥐순이")));
//
//        verify(productService).updateProduct(eq(1L), any(ProductData.class));
//    }
//
//    @Test
//    void updateWithNotExistedProduct() throws Exception {
//        mockMvc.perform(
//                patch("/products/1000")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\"," +
//                                "\"price\":5000}")
//        )
//                .andExpect(status().isNotFound());
//
//        verify(productService).updateProduct(eq(1000L), any(ProductData.class));
//    }
//
//    @Test
//    void updateWithInvalidAttributes() throws Exception {
//        mockMvc.perform(
//                patch("/products/1")
//                        .accept(MediaType.APPLICATION_JSON_UTF8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"\",\"maker\":\"\"," +
//                                "\"price\":0}")
//        )
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void destroyWithExistedProduct() throws Exception {
//        mockMvc.perform(
//                delete("/products/1")
//        )
//                .andExpect(status().isNoContent());
//
//        verify(productService).deleteProduct(1L);
//    }
//
//    @Test
//    void destroyWithNotExistedProduct() throws Exception {
//        mockMvc.perform(
//                delete("/products/1000")
//        )
//                .andExpect(status().isNotFound());
//
//        verify(productService).deleteProduct(1000L);
//    }
//}
