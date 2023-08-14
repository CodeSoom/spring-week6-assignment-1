package com.codesoom.assignment.interceptor;

import com.codesoom.assignment.application.JpaTest;
import com.codesoom.assignment.errors.AccessTokenNotFoundException;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static com.codesoom.assignment.utils.TestHelper.*;

@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("AuthInterceptor 클래스")
class AuthInterceptorTest extends JpaTest {
    AuthInterceptor authInterceptor = new AuthInterceptor(getJwtUtil());

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class preHandle_메서드는 {

        @DisplayName("인증토큰이 없으면 AccessTokenNotFoundException을 반환한다.")
        @Test
        void It_returns_AccessTokenNotFoundException() {
            Assertions.assertThatThrownBy(() -> authInterceptor.preHandle(INVALID_SERVLET_REQUEST, null, null))
                    .isInstanceOf(AccessTokenNotFoundException.class);
        }

        @DisplayName("인증토큰이 유효하지 않으면 InvalidAccessTokenException을 반환한다.")
        @Test
        void It_returns_InvalidAccessTokenException() {
            Assertions.assertThatThrownBy(() -> authInterceptor.preHandle(getInvalidTokenServletRequest(), null, null))
                    .isInstanceOf(InvalidAccessTokenException.class);
        }

        @DisplayName("인증토큰이 유효하면 true를 반환한다.")
        @Test
        void It_returns_true() throws Exception {
            Assertions.assertThat(authInterceptor.preHandle(getValidTokenServletRequest(), null, null))
                    .isTrue();
        }
    }

}
