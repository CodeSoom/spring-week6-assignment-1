package com.codesoom.assignment.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JsonWebTokenManager 클래스")
class JsonWebTokenManagerTest {

    private static final String TEST_SECRET_KEY = "12345678901234567890123456789010";

    private JsonWebTokenManager tokenManager;

    @BeforeEach
    void setUp() {
        tokenManager = new JsonWebTokenManager(TEST_SECRET_KEY);
    }

    @Nested
    @DisplayName("createJwt 메소드는")
    class Describe_createAccessToken {

        final JsonWebTokenAttribute jwtAttribute = JsonWebTokenAttribute.builder().build();

        @Test
        @DisplayName("Json Web Token 을 생성한다.")
        void it_generate_jwt() {
            String jwt = tokenManager.createToken(jwtAttribute);
            String[] tests = jwt.split("\\.");
            assertThat(tests.length).isEqualTo(3);
        }
    }
}
