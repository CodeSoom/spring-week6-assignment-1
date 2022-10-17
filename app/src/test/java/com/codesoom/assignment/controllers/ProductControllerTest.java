package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.product.ProductCommand;
import com.codesoom.assignment.application.product.ProductCommand.Register;
import com.codesoom.assignment.application.product.ProductCommand.UpdateRequest;
import com.codesoom.assignment.application.product.ProductCommandService;
import com.codesoom.assignment.application.product.ProductQueryService;
import com.codesoom.assignment.utils.ProductSampleFactory;
import com.codesoom.assignment.common.exception.InvalidParamException;
import com.codesoom.assignment.common.exception.ProductNotFoundException;
import com.codesoom.assignment.common.mapper.ProductMapper;
import com.codesoom.assignment.dto.ProductDto;
import com.codesoom.assignment.dto.ProductDto.RequestParam;
import com.codesoom.assignment.domain.product.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static com.codesoom.assignment.utils.ProductSampleFactory.FieldName.MAKER;
import static com.codesoom.assignment.utils.ProductSampleFactory.FieldName.NAME;
import static com.codesoom.assignment.utils.ProductSampleFactory.FieldName.PRICE;
import static com.codesoom.assignment.utils.ProductSampleFactory.ValueType.BLANK;
import static com.codesoom.assignment.utils.ProductSampleFactory.ValueType.EMPTY;
import static com.codesoom.assignment.utils.ProductSampleFactory.ValueType.NULL;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductController 클래스")
class ProductControllerTest {
    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductCommandService productCommandService;

    @MockBean
    private ProductQueryService productQueryService;

    @BeforeEach
    void setup() {
        // ResponseBody JSON에 한글이 깨지는 문제로 추가
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Nested
    @DisplayName("list[/products::GET] 메소드는")
    class Describe_list {
        ResultActions subject() throws Exception {
            return mockMvc.perform(get("/products"));
        }

        @Nested
        @DisplayName("상품이 존재하면")
        class Context_with_products {
            private final List<Product> givenProducts = new ArrayList<>();

            @BeforeEach
            void prepare() {
                givenProducts.add(ProductSampleFactory.createProduct(1L));
                givenProducts.add(ProductSampleFactory.createProduct(2L));

                given(productQueryService.getProducts()).willReturn(givenProducts);
            }

            @Test
            @DisplayName("OK(200)와 모든 상품을 리턴한다")
            void it_returns_200_and_all_products() throws Exception {
                final ResultActions resultActions = subject();

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].name").value(equalTo(givenProducts.get(0).getName())))
                        .andExpect(jsonPath("$[1].name").value(equalTo(givenProducts.get(1).getName())))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("상품이 존재하지 않으면")
        class Context_with_empty_db {
            @BeforeEach
            void prepare() {
                given(productQueryService.getProducts()).willReturn(new ArrayList<>());
            }

            @Test
            @DisplayName("OK(200)와 빈 데이터를 리턴한다")
            void it_return_200_and_empty_array() throws Exception {
                final ResultActions resultActions = subject();

                resultActions.andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string("[]"))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("detail[/products/id::GET] 메소드는")
    class Describe_detail {
        ResultActions subject(Long id) throws Exception {
            return mockMvc.perform(get("/products/" + id));
        }

        @Nested
        @DisplayName("유효한 ID가 주어지면")
        class Context_with_valid_id {
            private final Long PRODUCT_ID = 1L;
            private final Product givenProduct = ProductSampleFactory.createProduct(1L);

            @BeforeEach
            void prepare() {
                given(productQueryService.getProduct(PRODUCT_ID)).willReturn(givenProduct);
            }

            @Test
            @DisplayName("OK(200)와 요청한 상품을 리턴한다")
            void it_returns_200_and_searched_product() throws Exception {
                final ResultActions resultActions = subject(PRODUCT_ID);

                resultActions.andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("name").value(equalTo(givenProduct.getName())))
                        .andExpect(jsonPath("maker").value(equalTo(givenProduct.getMaker())))
                        .andDo(print());


            }
        }

        @Nested
        @DisplayName("유효하지않은 ID가 주어지면")
        class Context_with_invalid_id {
            private final Long PRODUCT_ID = 100L;

            @BeforeEach
            void prepare() {
                given(productQueryService.getProduct(PRODUCT_ID)).willThrow(new ProductNotFoundException(PRODUCT_ID));
            }

            @Test
            @DisplayName("NOT_FOUND(404)와 예외 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                final ResultActions resultActions = subject(PRODUCT_ID);

                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("요청하신 상품이 없습니다.")))
                        .andDo(print());

            }
        }

    }

    @Nested
    @DisplayName("registerProduct[/products::POST] 메소드는")
    class Describe_registerProduct {
        ResultActions subject(RequestParam request) throws Exception {
            return mockMvc.perform(post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }
        @Nested
        @DisplayName("새로운 상품을 등록하면")
        class Context_with_new_product {
            private final RequestParam givenRequest = ProductSampleFactory.createRequestParam();

            @BeforeEach
            void prepare() {
                given(productCommandService.createProduct(any(Register.class)))
                        .willReturn(ProductMapper.INSTANCE.toEntity(ProductMapper.INSTANCE.of(1L, givenRequest)));
            }

            @Test
            @DisplayName("CREATED(201)와 등록된 상품을 리턴한다")
            void it_returns_201_and_registered_product() throws Exception {
                final ResultActions resultActions = subject(givenRequest);

                resultActions.andExpect(status().isCreated())
                        .andExpect(jsonPath("name").value(equalTo(givenRequest.getName())))
                        .andExpect(jsonPath("maker").value(equalTo(givenRequest.getMaker())))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("필수항목에 빈 값이 주어지면")
        class Context_with_blank_field {
            private final List<ProductDto.RequestParam> testList = new ArrayList<>();

            @BeforeEach
            void prepare() {
                testList.add(ProductSampleFactory.createRequestParamWith(NAME, NULL));
                testList.add(ProductSampleFactory.createRequestParamWith(NAME, EMPTY));
                testList.add(ProductSampleFactory.createRequestParamWith(NAME, BLANK));
                testList.add(ProductSampleFactory.createRequestParamWith(MAKER, NULL));
                testList.add(ProductSampleFactory.createRequestParamWith(MAKER, EMPTY));
                testList.add(ProductSampleFactory.createRequestParamWith(MAKER, BLANK));
                testList.add(ProductSampleFactory.createRequestParamWith(PRICE, NULL));
            }

            @Test
            @DisplayName("BAD_REQUEST(400)와 에러메시지를 리턴한다")
            void it_returns_400_and_error_message() {
                testList.forEach(this::test);
            }

            private void test(ProductDto.RequestParam request) {
                try {
                    ResultActions resultActions = subject(request);

                    resultActions.andExpect(status().isBadRequest())
                            .andDo(print());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Nested
        @DisplayName("금액 범위가 벗어나면")
        class Context_with_invalid_range_price {
            private final ProductDto.RequestParam givenRequest = new ProductDto.RequestParam();

            @BeforeEach
            void prepare() {
                givenRequest.setName("뉴 고양이 장난감");
                givenRequest.setMaker("애플");
                givenRequest.setPrice(100L);
            }

            @Test
            @DisplayName("BAD_REQUEST(400)와 에러메시지를 리턴한다")
            void it_returns_400_and_error_message() throws Exception {
                ResultActions resultActions = subject(givenRequest);

                resultActions.andExpect(status().isBadRequest())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("InvalidParamException이 발생하면")
        class Context_with_illegal_argument_exception {
            @BeforeEach
            void prepare() {
                given(productCommandService.createProduct(any(ProductCommand.Register.class)))
                        .willThrow(new InvalidParamException("입력값이 비어있습니다."));
            }
            @Test
            @DisplayName("BAD_REQUEST(400)과 에러 메시지를 리턴한다")
            void it_returns_400_and_error_message() throws Exception {
                ResultActions resultActions = subject(ProductSampleFactory.createRequestParam());

                resultActions.andExpect(status().isBadRequest())
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("updateProduct[/products/id::PATCH] 메소드는")
    class Describe_updateProduct {
        ResultActions subject(Long id, RequestParam request) throws Exception {
            return mockMvc.perform(patch("/products/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        }

        @Nested
        @DisplayName("유효한 ID가 주어지면")
        class Context_with_valid_id {
            private final Long PRODUCT_ID = 1L;
            private final Product savedProduct = ProductSampleFactory.createProduct(PRODUCT_ID);
            private final RequestParam givenRequest = new RequestParam();

            @BeforeEach
            void prepare() {
                givenRequest.setName("수정_" + savedProduct.getName());
                givenRequest.setMaker("수정_" + savedProduct.getMaker());
                givenRequest.setPrice(savedProduct.getPrice() + 5000);

                given(productCommandService.updateProduct(any(UpdateRequest.class)))
                        .willReturn(ProductMapper.INSTANCE.toEntity(ProductMapper.INSTANCE.of(PRODUCT_ID, givenRequest)));
            }

            @Test
            @DisplayName("상품을 수정하고 OK(200)와 수정된 상품을 리턴한다")
            void it_returns_200_and_modified_product() throws Exception {
                final ResultActions resultActions = subject(PRODUCT_ID, givenRequest);

                resultActions.andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("name").value(equalTo(givenRequest.getName())))
                        .andExpect(jsonPath("maker").value(equalTo(givenRequest.getMaker())))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("유효하지않은 ID가 주어지면")
        class Context_with_invalid_id {
            private final Long PRODUCT_ID = 100L;
            private final RequestParam givenRequest = ProductSampleFactory.createRequestParam();

            @BeforeEach
            void prepare() {
                given(productCommandService.updateProduct(any(UpdateRequest.class))).willThrow(new ProductNotFoundException(PRODUCT_ID));
            }

            @Test
            @DisplayName("NOT_FOUND(404)와 예외 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                ResultActions resultActions = subject(PRODUCT_ID, givenRequest);

                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("요청하신 상품이 없습니다.")))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("필수항목에 빈 값이 주어지면")
        class Context_with_blank_field {
            private final Long PRODUCT_ID = 1L;
            private final List<ProductDto.RequestParam> testList = new ArrayList<>();

            @BeforeEach
            void prepare() {
                testList.add(ProductSampleFactory.createRequestParamWith(NAME, NULL));
                testList.add(ProductSampleFactory.createRequestParamWith(NAME, EMPTY));
                testList.add(ProductSampleFactory.createRequestParamWith(NAME, BLANK));
                testList.add(ProductSampleFactory.createRequestParamWith(MAKER, NULL));
                testList.add(ProductSampleFactory.createRequestParamWith(MAKER, EMPTY));
                testList.add(ProductSampleFactory.createRequestParamWith(MAKER, BLANK));
                testList.add(ProductSampleFactory.createRequestParamWith(PRICE, NULL));
            }

            @Test
            @DisplayName("BAD_REQUEST(400)와 에러메시지를 리턴한다")
            void it_returns_400_and_error_message() {
                testList.forEach(this::test);
            }

            private void test(ProductDto.RequestParam request) {
                try {
                    ResultActions resultActions = subject(PRODUCT_ID, request);

                    resultActions.andExpect(status().isBadRequest())
                            .andDo(print());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Nested
        @DisplayName("금액 범위가 벗어나면")
        class Context_with_invalid_range_price {
            private final Long PRODUCT_ID = 1L;
            private final ProductDto.RequestParam givenRequest = new ProductDto.RequestParam();

            @BeforeEach
            void prepare() {
                givenRequest.setName("뉴 고양이 장난감");
                givenRequest.setMaker("애플");
                givenRequest.setPrice(100L);
            }

            @Test
            @DisplayName("BAD_REQUEST(400)와 에러메시지를 리턴한다")
            void it_returns_400_and_error_message() throws Exception {
                ResultActions resultActions = subject(PRODUCT_ID, givenRequest);

                resultActions.andExpect(status().isBadRequest())
                        .andDo(print());
            }
        }

    }

    @Nested
    @DisplayName("deleteProduct[/products/id::DELETE] 메소드는")
    class Describe_deleteProduct {
        ResultActions subject(Long id) throws Exception {
            return mockMvc.perform(delete("/products/" + id)
                    .contentType(MediaType.APPLICATION_JSON));
        }

        @Nested
        @DisplayName("유효한 ID가 주어지면")
        class Context_with_valid_id {
            private final Long PRODUCT_ID = 1L;

            @BeforeEach
            void prepare() {
                doNothing().when(productCommandService).deleteProduct(PRODUCT_ID);
            }

            @Test
            @DisplayName("상품을 삭제하고 NO_CONTENT(204)를 리턴한다")
            void it_returns_204() throws Exception {
                final ResultActions resultActions = subject(PRODUCT_ID);

                resultActions.andExpect(status().isNoContent())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("유효하지않은 ID가 주어지면")
        class Context_with_invalid_id {
            private final Long PRODUCT_ID = 100L;
            @BeforeEach
            void prepare() {
                doThrow(new ProductNotFoundException(PRODUCT_ID)).when(productCommandService).deleteProduct(PRODUCT_ID);
            }

            @Test
            @DisplayName("NOT_FOUND(404)와 예외 메시지를 리턴한다")
            void it_returns_404_and_message() throws Exception {
                final ResultActions resultActions = subject(PRODUCT_ID);

                resultActions.andExpect(status().isNotFound())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("message", containsString("요청하신 상품이 없습니다.")))
                        .andDo(print());
            }
        }

    }
}
