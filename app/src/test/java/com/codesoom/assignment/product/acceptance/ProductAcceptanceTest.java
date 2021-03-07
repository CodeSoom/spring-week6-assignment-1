package com.codesoom.assignment.product.acceptance;

import com.codesoom.assignment.AcceptanceTest;
import com.codesoom.assignment.auth.dto.SessionResponseData;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codesoom.assignment.product.acceptance.step.ProductAcceptanceStep.상품_생성됨;
import static com.codesoom.assignment.product.acceptance.step.ProductAcceptanceStep.상품을_생성;
import static com.codesoom.assignment.user.acceptance.step.UserAcceptanceStep.로그인_되어_있음;
import static com.codesoom.assignment.user.acceptance.step.UserAcceptanceStep.회원_등록되어_있음;

class ProductAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String NAME = "tester";

    private static String PRODUCT_NAME = "TOY";
    private static String MAKER = "CAT-OWNER";
    private static String IMAGE_URL = "https://http.cat/599";
    private static int PRICE = 5000;

    @DisplayName("로그인한 사용자는 상품을 생성할 수 있다")
    @Test
    void create_product() {
        회원_등록되어_있음(EMAIL, PASSWORD, NAME);

        SessionResponseData sessionResponseData = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 상품을_생성(sessionResponseData, PRODUCT_NAME, MAKER, IMAGE_URL, PRICE);

        상품_생성됨(response, PRODUCT_NAME, MAKER, IMAGE_URL, PRICE);
    }
}
