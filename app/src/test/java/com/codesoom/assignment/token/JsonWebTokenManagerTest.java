package com.codesoom.assignment.token;

import com.codesoom.assignment.errors.InvalidTokenException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JsonWebTokenManager 클래스")
class JsonWebTokenManagerTest {

    private static final String TEST_SECRET_KEY = "12345678901234567890123456789010";

    private static final String TEST_VALID_USER_ID = "1";

    private JsonWebTokenManager tokenManager;

    @BeforeEach
    void setUp() {
        tokenManager = new JsonWebTokenManager(TEST_SECRET_KEY);
    }

    @Nested
    @DisplayName("createToken 메소드는")
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

    @Nested
    @DisplayName("getJwtId 메소드는")
    class Describe_getJwtId {

        final JsonWebTokenAttribute jwtAttribute = JsonWebTokenAttribute.builder().jwtId(TEST_VALID_USER_ID).build();

        @Nested
        @DisplayName("유효한 토큰이 주어졌을 경우")
        class Context_validToken {

            String validToken;

            @BeforeEach
            void setUp() {
                validToken = tokenManager.createToken(jwtAttribute);
            }

            @Test
            @DisplayName("고유 아이디를 리턴한다.")
            void it_unique_id() {
                String uniqueId = tokenManager.getJwtId(validToken);

                assertThat(uniqueId).isEqualTo(TEST_VALID_USER_ID);
            }
        }

        @Nested
        @DisplayName("유효하지 않는 토큰이 주어졌을 경우")
        class Context_invalidToken {

            final String invalidToken = "aaaa.bbbb.cccc";

            @Test
            @DisplayName("예외를 던진다.")
            void it_throw_exception() {
                assertThatThrownBy(
                        () -> tokenManager.getJwtId(invalidToken)
                ).isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("null 이나 \"\" 문자열이 주어졌을 경우")
        class Context_nullOrEmptyArgument {

            @ParameterizedTest(name = "예외를 던진다. (\"{0}\")")
            @NullAndEmptySource
            void it_throw_exception(String emptyString) throws Exception {
                assertThatThrownBy(
                        () -> tokenManager.getJwtId(emptyString)
                ).isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
