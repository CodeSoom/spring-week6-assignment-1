package com.codesoom.assignment.product.acceptance.step;

import com.codesoom.assignment.auth.dto.SessionResponseData;
import com.codesoom.assignment.product.domain.Product;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceStep {

    public static ExtractableResponse<Response> 상품을_생성(SessionResponseData sessionResponseData,
                                                       String name, String maker, String imageUrl, int price) {
        Map<String, Object> params = new HashMap<>();

        params.put("name", name);
        params.put("maker", maker);
        params.put("imageUrl", imageUrl);
        params.put("price", price);

        return RestAssured.given().log().all().
                auth().oauth2(sessionResponseData.getAccessToken()).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/products").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract();
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response, String name, String maker, String imageUrl, int price) {
        Product product = response.as(Product.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getMaker()).isEqualTo(maker);
        assertThat(product.getImageUrl()).isEqualTo(imageUrl);
        assertThat(product.getPrice()).isEqualTo(price);
    }
}
