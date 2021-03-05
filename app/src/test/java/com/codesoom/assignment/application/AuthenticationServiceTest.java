package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.AuthenticationCreateData;
import com.codesoom.assignment.dto.AuthenticationResultData;
import com.codesoom.assignment.dto.SessionResultData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserBadRequestException;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("AuthenticationService 테스트")
class AuthenticationServiceTest {
    private JwtUtil jwtUtil;
    private AuthenticationService authenticationService;

    private static final String SECRET = "12345678901234567890123456789010";
    private static final String EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4aXN0ZWRFbWFpbCIsInBhc3N3b3JkIjoiZXhpc3RlZFBhc3N3b3JkIn0." +
            "iqS2XKpt7blLuhlACfLFdomPsjXzC9RGW67mJGB0NaA";
    private static final String NOT_EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4aXN0ZWRFbWFpbCIsInBhc3N3b3JkIjoiZXhpc3RlZFBhc3N3b3JkIn0." +
            "iqS2XKpt7blLuhlACfLFdomPsjXzC9RGW67mJGB0Naa";

    private final String EXISTED_EMAIL = "existedEmail";
    private final String EXISTED_PASSWORD = "existedPassword";

    private final String NOT_EXISTED_EMAIL = "existedEmail";
    private final String NOT_EXISTED_PASSWORD = "existedPassword";

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {
        @Nested
        @DisplayName("만약 저장되어 있는 사용자가 주어진다면")
        class Context_WithExistedUser {
            private AuthenticationCreateData givenUser;
            private SessionResultData sessionResultData;

            @BeforeEach
            void setUp() {
                givenUser = AuthenticationCreateData.builder()
                        .email(EXISTED_EMAIL)
                        .password(EXISTED_PASSWORD)
                        .build();

                sessionResultData = SessionResultData.builder()
                        .accessToken(EXISTED_TOKEN)
                        .build();
            }

            @Test
            @DisplayName("주어진 사용자를 이용하여 토큰을 생성하고 해당 토큰을 리턴한다")
            void itCreatesTokenAndReturnsToken() {
                given(jwtUtil.encode(givenUser.getEmail(), givenUser.getPassword()))
                        .willReturn(EXISTED_TOKEN);

                SessionResultData token = authenticationService.createToken(givenUser);

                assertThat(token).isEqualTo(sessionResultData);
            }
        }

        @Nested
        @DisplayName("만약 이메일이 저장되어 있지 않은 사용자가 주어진다면")
        class Context_WithUserWithoutEmail {
            private AuthenticationCreateData givenUser;

            @BeforeEach
            void setUp() {
                givenUser = AuthenticationCreateData.builder()
                        .email(NOT_EXISTED_EMAIL)
                        .password(EXISTED_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("요청이 잘못 되었다는 메세지를 리턴한다")
            void itReturnsUserNotFoundMessage() {
                given(jwtUtil.encode(givenUser.getEmail(), givenUser.getPassword()))
                        .willThrow(new UserBadRequestException());

                assertThatThrownBy(() -> authenticationService.createToken(givenUser))
                        .isInstanceOf(UserBadRequestException.class)
                        .hasMessageContaining("User bad request");
            }
        }

        @Nested
        @DisplayName("만약 비밀번호가 올바르지 않는 사용자가 주어진다면")
        class Context_WithUserWithoutPassword {
            private AuthenticationCreateData givenUser;

            @BeforeEach
            void setUp() {
                givenUser = AuthenticationCreateData.builder()
                        .email(EXISTED_EMAIL)
                        .password(NOT_EXISTED_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("요청이 잘못 되었다는 메세지를 리턴한다")
            void itReturnsUserNotFoundMessage() {
                given(jwtUtil.encode(givenUser.getEmail(), givenUser.getPassword()))
                        .willThrow(new UserBadRequestException());

                assertThatThrownBy(() -> authenticationService.createToken(givenUser))
                        .isInstanceOf(UserBadRequestException.class)
                        .hasMessageContaining("User bad request");
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_parseToken {
        @Nested
        @DisplayName("만약 유효한 토큰이 주어진다면")
        class Context_WithValidToken {
            private final String givenValidToken = EXISTED_TOKEN;
            private Claims claims;
            private AuthenticationResultData authenticationResultData;

            @BeforeEach
            void setUp() {
                claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                        .build()
                        .parseClaimsJws(EXISTED_TOKEN)
                        .getBody();

                authenticationResultData = AuthenticationResultData.builder()
                        .email(EXISTED_EMAIL)
                        .password(EXISTED_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("주어진 토큰을 파싱하여 안에 담긴 사용자 정보를 리턴한다")
            void itParsesTokenAndReturnsUser() {
                given(jwtUtil.decode(givenValidToken)).willReturn(claims);

                AuthenticationResultData authenticationResultData = authenticationService.parseToken(givenValidToken);

                assertThat(authenticationResultData.getEmail()).isEqualTo(EXISTED_EMAIL);
                assertThat(authenticationResultData.getPassword()).isEqualTo(EXISTED_PASSWORD);
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 토큰이 주어진다면")
        class Context_WithNotValidToken {
            private final String givenNotValidToken = NOT_EXISTED_TOKEN;

            @Test
            @DisplayName("토큰이 유효하지 않다는 메세지를 리턴한다")
            void itParsesTokenAndReturnsUser() {
                given(jwtUtil.decode(givenNotValidToken))
                        .willThrow(new InvalidTokenException(givenNotValidToken));

                assertThatThrownBy(() -> authenticationService.parseToken(givenNotValidToken))
                        .isInstanceOf(InvalidTokenException.class)
                        .hasMessageContaining("Invalid token");
            }
        }

        @Nested
        @DisplayName("만약 null 이 주어진다면")
        class Context_WithNull {
            @Test
            @DisplayName("토큰이 유효하지 않다는 메세지를 리턴한다")
            void itParsesTokenAndReturnsUser() {
                given(jwtUtil.decode(null))
                        .willThrow(new InvalidTokenException(null));

                assertThatThrownBy(() -> authenticationService.parseToken(null))
                        .isInstanceOf(InvalidTokenException.class)
                        .hasMessageContaining("Invalid token");
            }
        }
    }
}




//package com.codesoom.assignment.application;
//
//import com.codesoom.assignment.errors.InvalidAccessTokenException;
//import com.codesoom.assignment.util.JwtUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//class AuthenticationServiceTest {
//    private static final String SECRET = "12345678901234567890123456789010";
//    private static final String EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
//            "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
//    private static final String NOT_EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
//            "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGW";
//
//    private AuthenticationService authenticationService;
//    private final Long EXISTED_ID = 1L;
//
//    @BeforeEach
//    void setUp() {
//        JwtUtil jwtUtil = new JwtUtil(SECRET);
//        authentaicationService = new AuthenticationService(jwtUtil);
//    }
//
//    @Test
//    void login(){
//        String accessToken = authenticationService.login(EXISTED_ID);
//
//        assertThat(accessToken).isEqualTo(EXISTED_TOKEN);
//    }
//
//    @Test
//    void parseTokenWithValidToken() {
//        Long userId = authenticationService.parseToken(EXISTED_TOKEN);
//
//        assertThat(userId).isEqualTo(1L);
//    }
//
//    @Test
//    void parseTokenWithInvalidToken() {
//        assertThatThrownBy(() -> authenticationService.parseToken(NOT_EXISTED_TOKEN))
//                .isInstanceOf(InvalidAccessTokenException.class);
//    }
//
//    @Test
//    void parseTokenWithBlankToken() {
//        assertThatThrownBy(() -> authenticationService.parseToken(""))
//                .isInstanceOf(InvalidAccessTokenException.class);
//    }
//
//    @Test
//    void parseTokenWithNullToken() {
//        assertThatThrownBy(() -> authenticationService.parseToken(null))
//                .isInstanceOf(InvalidAccessTokenException.class);
//    }
//}
