package com.codesoom.assignment.user.acceptance.step;

import com.codesoom.assignment.AcceptanceTest;
import com.codesoom.assignment.auth.dto.SessionResponseData;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceStep extends AcceptanceTest {

    public static ExtractableResponse<Response> 회원_등록되어_있음(String email, String password, String name) {
        return 회원_생성을_요청(email, password, name);
    }

    public static ExtractableResponse<Response> 회원_생성을_요청(String email, String password, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("name", name);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/users").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();

        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/session").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract();
    }
    public static SessionResponseData 로그인_되어_있음(String email, String password) {
        ExtractableResponse<Response> response = 로그인_요청(email, password);

        SessionResponseData sessionResponseData = response.as(SessionResponseData.class);

        return sessionResponseData;
    }

    public static void 로그인_됨(ExtractableResponse<Response> response) {
        SessionResponseData sessionResponseData = response.as(SessionResponseData.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(sessionResponseData.getAccessToken()).isNotBlank();
    }

}
