package com.codesoom.assignment.utils;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.dto.UserLoginData;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.stream.Stream;

public class TestHelper {

    public static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    public static final String INVALID_TOKEN = VALID_TOKEN + "INVALID";
    public static final String AUTH_NAME = "AUTH_NAME";
    public static final String AUTH_EMAIL = "auth@foo.com";
    public static final String INVALID_EMAIL = AUTH_EMAIL + "INVALID";
    public static final String AUTH_PASSWORD = "12345678";
    public static final String TEST_PRODUCT_NAME = "쥐돌이";
    public static final String TEST_UPDATE_PRODUCT_NAME = "쥐순이";
    public static final String TEST_PRODUCT_MAKER = "냥이월드";
    public static final int TEST_PRODUCT_PRICE = 5000;
    public static final String INVALID_PASSWORD = AUTH_PASSWORD + "INVALID";
    public static final MockHttpServletRequest INVALID_SERVLET_REQUEST = new MockHttpServletRequest();

    public static final UserLoginData IS_NOT_EXISTS_USER_DATA = UserLoginData.builder()
            .email(INVALID_EMAIL)
            .password(AUTH_PASSWORD).build();

    public static final UserLoginData INVALID_PASSWORD_USER_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .password(INVALID_PASSWORD)
            .build();

    public static final UserLoginData AUTH_USER_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .password(AUTH_PASSWORD)
            .build();

    public static final User AUTH_USER = User.builder()
            .name(AUTH_NAME)
            .email(AUTH_EMAIL)
            .password(AUTH_PASSWORD)
            .build();

    public static final Product TEST_PRODUCT = Product.builder()
            .id(1L)
            .name(TEST_PRODUCT_NAME)
            .maker(TEST_PRODUCT_MAKER)
            .price(TEST_PRODUCT_PRICE)
            .build();

    public static final ProductData TEST_PRODUCT_DATA = ProductData.builder()
            .name(TEST_PRODUCT_NAME)
            .maker(TEST_PRODUCT_MAKER)
            .price(TEST_PRODUCT_PRICE)
            .build();

    public static Stream<Arguments> provideInvalidProductRequests() {
        return Stream.of(
                Arguments.of(ProductData.builder().name("").maker("").price(0).build()),
                Arguments.of(ProductData.builder().name("").maker(TEST_PRODUCT_MAKER).price(TEST_PRODUCT_PRICE).build()),
                Arguments.of(ProductData.builder().name(TEST_PRODUCT_NAME).maker("").price(TEST_PRODUCT_PRICE).build()),
                Arguments.of(ProductData.builder().name(TEST_PRODUCT_NAME).maker(TEST_PRODUCT_MAKER).price(null).build())
        );
    }

    public static final ProductData UPDATE_PRODUCT_DATA = ProductData.builder()
            .name(TEST_UPDATE_PRODUCT_NAME)
            .maker(TEST_PRODUCT_MAKER)
            .price(TEST_PRODUCT_PRICE)
            .build();

    public static MockHttpServletRequest getInvalidTokenServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + INVALID_TOKEN);
        return request;
    }

    public static MockHttpServletRequest getValidTokenServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + VALID_TOKEN);
        return request;
    }
}