package com.codesoom.assignment.dto;

import com.codesoom.assignment.application.product.ProductCommand;
import com.codesoom.assignment.utils.ProductSampleFactory;
import com.codesoom.assignment.common.mapper.ProductMapper;
import com.codesoom.assignment.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
@DisplayName("ProductDtoMapper 클래스")
class ProductMapperTest {

    @Nested
    @DisplayName("of(RequestParam) 메소드는")
    class Describe_of_request_param {
        @Nested
        @DisplayName("유효한 요청 파라미터가 주어지면")
        class Context_with_valid_request_parameter {
            @Test
            @DisplayName("Register 객체를 리턴한다")
            void it_returns_register() {
                final Product product = ProductSampleFactory.createProduct(1L);

                final ProductCommand.Register actual = ProductSampleFactory.of(product);

                assertThat(actual).isInstanceOf(ProductCommand.Register.class);
            }
        }

        @Nested
        @DisplayName("요청 파라미터가 Null이면")
        class Context_with_invalid_request_parameter {
            @Test
            @DisplayName("Null을 리턴한다")
            void it_returns_register() {
                final ProductDto.RequestParam request = null;

                final ProductCommand.Register actual = ProductMapper.INSTANCE.of(request);

                assertThat(actual).isNull();
            }
        }

    }

    @Nested
    @DisplayName("of(id, RequestParam) 메소드는")
    class Describe_of_id_and_request_param {
        @Nested
        @DisplayName("유효한 요청 파라미터가 주어지면")
        class Context_with_valid_request_parameter {
            @Test
            @DisplayName("UpdateReq 객체를 리턴한다")
            void it_returns_register() {
                final Long id = 1L;

                final ProductCommand.UpdateRequest actual = ProductMapper.INSTANCE.of(id, ProductSampleFactory.createRequestParam());

                assertThat(actual).isInstanceOf(ProductCommand.UpdateRequest.class);
            }
        }

        @Nested
        @DisplayName("모든 파라미터가 Null이면")
        class Context_with_invalid_request_parameter {
            @Test
            @DisplayName("Null을 리턴한다")
            void it_returns_null() {
                final Long id = null;
                final ProductDto.RequestParam request = null;

                final ProductCommand.UpdateRequest actual = ProductMapper.INSTANCE.of(id, request);

                assertThat(actual).isNull();
            }
        }

        @Nested
        @DisplayName("ID가 Null이면")
        class Context_with_id_null {
            @Test
            @DisplayName("ID필드가 Null인 객체를 리턴한다")
            void it_returns_null() {
                final Long id = null;

                final ProductCommand.UpdateRequest actual = ProductMapper.INSTANCE.of(id, ProductSampleFactory.createRequestParam());
                System.out.println(actual);

                assertThat(actual.getId()).isNull();
            }
        }

        @Nested
        @DisplayName("RequestParam이 Null이면")
        class Context_with_requestparam_null {
            @Test
            @DisplayName("RequestParam의 필드들이 Null인 객체를 리턴한다")
            void it_returns_null() {
                final Long id = 1L;
                final ProductDto.RequestParam request = null;

                final ProductCommand.UpdateRequest actual = ProductMapper.INSTANCE.of(id, request);

                assertThat(actual.getName()).isNull();
                assertThat(actual.getMaker()).isNull();
                assertThat(actual.getPrice()).isNull();
            }
        }
    }


    @Nested
    @DisplayName("toEntity(ProductCommand.UpdateRequest) 메소드는")
    class Describe_toEntity_updaterequest {
        @Nested
        @DisplayName("유효한 파라미터가 주어지면")
        class Context_with_valid_param {
            @Test
            @DisplayName("Product 객체를 리턴한다")
            void it_returns_update_request_object() {
                final Long id = 1L;
                final ProductCommand.UpdateRequest command = ProductMapper.INSTANCE.of(id, ProductSampleFactory.createRequestParam());

                assertThat(ProductMapper.INSTANCE.toEntity(command)).isInstanceOf(Product.class);
            }
        }

        @Nested
        @DisplayName("입력 파라미터가 Null이면")
        class Context_with_null {
            @Test
            @DisplayName("Null을 리턴한다")
            void it_returns_user_object() {
                final ProductCommand.UpdateRequest command = null;

                final Product actual = ProductMapper.INSTANCE.toEntity(command);

                assertThat(actual).isNull();
            }
        }

    }

    @Nested
    @DisplayName("toEntity(ProductCommand.Register) 메소드는")
    class Describe_toEntity_register {
        @Nested
        @DisplayName("유효한 Register 객체가 주어지면")
        class Context_with_valid_param {
            @Test
            @DisplayName("Product 객체를 리턴한다")
            void it_returns_user_object() {
                final ProductDto.RequestParam user = ProductSampleFactory.createRequestParam();

                final ProductCommand.Register actual = ProductMapper.INSTANCE.of(user);

                assertThat(ProductMapper.INSTANCE.toEntity(actual)).isInstanceOf(Product.class);
            }
        }

        @Nested
        @DisplayName("입력 파라미터가 Null이면")
        class Context_with_null {
            @Test
            @DisplayName("Null을 리턴한다")
            void it_returns_update_request_object() {
                final ProductCommand.Register command = null;

                Product actual = ProductMapper.INSTANCE.toEntity(command);

                assertThat(actual).isNull();
            }
        }

    }
}
