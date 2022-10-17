package com.codesoom.assignment.domain.product;

import com.codesoom.assignment.utils.ProductSampleFactory;
import com.codesoom.assignment.common.exception.InvalidParamException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codesoom.assignment.utils.ProductSampleFactory.FieldName.*;
import static com.codesoom.assignment.utils.ProductSampleFactory.ValueType.*;

@DisplayName("Product 클래스")
class ProductTest {

    @Nested
    @DisplayName("빌더는")
    class Describe_builder {
        @Nested
        @DisplayName("이름 or 제조사 or 가격이 Null 이면")
        class Context_with_empty_name {
            List<Product.ProductBuilder> givenProducts = new ArrayList<>();

            @BeforeEach
            void prepare() {
                givenProducts.add(ProductSampleFactory.createProductParamWith(NAME, NULL));
                givenProducts.add(ProductSampleFactory.createProductParamWith(MAKER, NULL));
                givenProducts.add(ProductSampleFactory.createProductParamWith(PRICE, NULL));
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                givenProducts.forEach(this::test);
            }

            private void test(Product.ProductBuilder builder) {
                Assertions.assertThatThrownBy(builder::build).isInstanceOf(InvalidParamException.class);
            }
        }
    }
}
