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
    private static final String TEST_LONG_PASSWORD = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut dignissim ex vitae congue congue. Nunc fermentum tellus leo. Donec malesuada, dolor non euismod suscipit, quam elit scelerisque ligula, in finibus eros justo eu justo. Duis tempor porta odio, id finibus nibh pellentesque congue. Ut et velit eget nibh tincidunt porta et id risus. Vestibulum suscipit ullamcorper varius. Proin eget arcu quam. Cras id feugiat libero. Integer auctor sem nec tempor pellentesque. Donec tempor molestie ex in viverra. Aliquam nec purus consequat purus ullamcorper tristique eu sodales erat. Nunc vitae accumsan orci. Vestibulum dictum ante non hendrerit convallis. Ut eu interdum nisl.\n" +
            "\n" +
            "Vestibulum et tellus tortor. Maecenas vulputate urna eu massa mattis, eu vulputate magna pretium. Vestibulum at sapien vitae mi tempus elementum at eget ante. Morbi risus dolor, eleifend eu ante sed, commodo aliquam augue. Pellentesque aliquet, tellus ultrices fermentum bibendum, turpis urna mollis mauris, sagittis posuere dolor mi et enim. Quisque mollis vulputate est vel eleifend. Donec nec sollicitudin massa. Sed mattis posuere metus sed dictum. Pellentesque varius est a arcu vulputate sollicitudin.\n" +
            "\n" +
            "Cras ac diam vehicula, elementum mauris tempus, accumsan lacus. Sed lectus diam, hendrerit a consequat id, eleifend eget libero. Praesent laoreet tempor magna et imperdiet. Aenean dictum non velit id lacinia. Donec congue ante dui, id rutrum ex accumsan at. Nulla ut massa elementum, posuere nunc sit amet, ornare nisl. Pellentesque in dui ipsum. Vivamus placerat velit sit amet tempus efficitur.\n" +
            "\n" +
            "Donec auctor lacus sit amet neque luctus, vitae tincidunt tortor lobortis. Fusce aliquam sem ut magna sollicitudin, ac vulputate est placerat. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nullam scelerisque augue elit, at bibendum libero efficitur ac. Sed fringilla purus pretium tortor condimentum imperdiet. Praesent in nibh lacinia, euismod enim eu, bibendum felis. Aliquam quis placerat ipsum. Integer dictum volutpat.";

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

    public static UserLoginData AUTH_USER_LOGIN_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .password(AUTH_PASSWORD)
            .build();

    public static UserLoginData EMAIL_NULL_LOGIN_USER_DATA = UserLoginData.builder()
            .password(AUTH_PASSWORD)
            .build();

    public static UserLoginData PASSWORD_NULL_LOGIN_USER_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .build();

    public static UserLoginData EMPTY_LOGIN_USER_DATA = UserLoginData.builder()
            .email("")
            .password("")
            .build();

    public static UserLoginData EMAIL_IS_SHORT_LOGIN_USER_DATA = UserLoginData.builder()
            .email("aa")
            .password(AUTH_PASSWORD)
            .build();

    public static UserLoginData PASSWORD_IS_TOO_SHORT_LOGIN_USER_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .password("111")
            .build();

    public static UserLoginData PASSWORD_IS_TOO_LONG_LOGIN_USER_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .password(TEST_LONG_PASSWORD)
            .build();

    public static Stream<Arguments> provideInvalidUserLoginRequests() {
        return Stream.of(
                Arguments.of(EMAIL_NULL_LOGIN_USER_DATA),
                Arguments.of(PASSWORD_NULL_LOGIN_USER_DATA),
                Arguments.of(EMPTY_LOGIN_USER_DATA),
                Arguments.of(EMAIL_IS_SHORT_LOGIN_USER_DATA),
                Arguments.of(PASSWORD_IS_TOO_SHORT_LOGIN_USER_DATA),
                Arguments.of(PASSWORD_IS_TOO_LONG_LOGIN_USER_DATA)
        );
    }
}
