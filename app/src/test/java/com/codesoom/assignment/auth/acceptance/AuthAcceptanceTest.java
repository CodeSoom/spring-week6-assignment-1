package com.codesoom.assignment.auth.acceptance;

import com.codesoom.assignment.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codesoom.assignment.user.acceptance.step.UserAcceptanceStep.로그인_됨;
import static com.codesoom.assignment.user.acceptance.step.UserAcceptanceStep.로그인_요청;
import static com.codesoom.assignment.user.acceptance.step.UserAcceptanceStep.회원_등록되어_있음;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "password";
    private static final String NAME = "tester";

    @DisplayName("회원 가입 후 로그인 할 수 있다")
    @Test
    void login() {
        회원_등록되어_있음(EMAIL, PASSWORD, NAME);

        ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        로그인_됨(response);
    }
}
