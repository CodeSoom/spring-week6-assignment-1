package com.codesoom.assignment.util;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserResultData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserBadRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JwtUtilTest {
    private JwtUtil jwtUtil;
    private UserRepository userRepository;

    private static final String SECRET = "12345678901234567890123456789010";
    private static final String EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4aXN0ZWRFbWFpbCIsInBhc3N3b3JkIjoiZXhpc3RlZFBhc3N3b3JkIn0." +
            "iqS2XKpt7blLuhlACfLFdomPsjXzC9RGW67mJGB0NaA";
    private static final String NOT_EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4aXN0ZWRFbWFpbCIsInBhc3N3b3JkIjoiZXhpc3RlZFBhc3N3b3JkIn0." +
            "iqS2XKpt7blLuhlACfLFdomPsjXzC9RGW67mJGB0Naa";

    private final Long EXISTED_ID = 1L;
    private final String SETUP_NAME = "setUpName";
    private final String EXISTED_EMAIL = "existedEmail";
    private final String EXISTED_PASSWORD = "existedPassword";

    private final String NOT_EXISTED_EMAIL = "notExistedEmail";
    private final String NOT_EXISTED_PASSWORD ="notExistedPassword";

    private String setUpToken;
    private User setUpUser;
    private UserResultData setUpUserResultData;

    public UserResultData getUserResultData(User user) {
        return UserResultData.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .deleted(user.isDeleted())
                .build();
    }

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        jwtUtil = new JwtUtil(SECRET, userRepository);

        setUpToken = Jwts.builder()
                .claim("email", EXISTED_EMAIL)
                .claim("password", EXISTED_PASSWORD)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();

        setUpUser = User.builder()
                .id(EXISTED_ID)
                .name(SETUP_NAME)
                .email(EXISTED_EMAIL)
                .password(EXISTED_PASSWORD)
                .build();

        setUpUserResultData = getUserResultData(setUpUser);
    }

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {
        @Nested
        @DisplayName("만약 저장되어 있는 이메일과 비밀번호가 주어진다면")
        class Context_WithExistedEmailAndExistedPassword {
            private final String givenExistedEmail = EXISTED_EMAIL;
            private final String givenExistedPassword = EXISTED_PASSWORD;

            @Test
            @DisplayName("주어진 이메일과 비밀번호로 토큰을 생성하고 리턴한다")
            void itCreatesTokenAndReturnsToken () {
                given(userRepository.findByEmail(givenExistedEmail)).willReturn(Optional.of(setUpUser));

                String token = jwtUtil.encode(givenExistedEmail, givenExistedPassword);

                assertThat(token).isEqualTo(EXISTED_TOKEN);
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 이메일이 주어진다면")
        class Context_WithNotExistedEmail {
            private final String givenNotExistedEmail = NOT_EXISTED_EMAIL;
            private final String givenExistedPassword = EXISTED_PASSWORD;

            @Test
            @DisplayName("이메일이 저장되어 있지 않다는 메세지를 리턴한다")
            void itReturnsEmailNotExistedMessage() {
                given(userRepository.findByEmail(givenNotExistedEmail))
                        .willReturn(Optional.empty());

                assertThatThrownBy(() -> jwtUtil.encode(givenNotExistedEmail,givenExistedPassword))
                        .isInstanceOf(UserBadRequestException.class)
                        .hasMessageContaining("User bad request");
            }
        }

        @Nested
        @DisplayName("만약 저장되어 있지 않은 비밀번호가 주어진다면")
        class Context_WithNotExistedPassword {
            private final String givenExistedEmail = EXISTED_EMAIL;
            private final String givenNotExistedPassword = NOT_EXISTED_PASSWORD;

            @Test
            @DisplayName("요청이 잘못되었다는 메세지를 리턴한다")
            void itReturnsUserBadRequestMessage() {
                given(userRepository.findByEmail(givenExistedEmail)).willReturn(Optional.of(setUpUser));

                UserResultData user = jwtUtil.getUser(givenExistedEmail);

                assertThat(user.getPassword()).isEqualTo(EXISTED_PASSWORD);
                assertThatThrownBy(() -> jwtUtil.encode(givenExistedEmail,givenNotExistedPassword))
                        .isInstanceOf(UserBadRequestException.class)
                        .hasMessageContaining("User bad request");
            }
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {
        @Nested
        @DisplayName("만약 유효한 토큰이 주어진다면")
        class Context_WithExistedToken {
            private final String givenExistedToken = EXISTED_TOKEN;

            @Test
            @DisplayName("주어진 토큰을 파싱하여 이메일과 비밀번호를 리턴한다")
            void itReturnsEmailAndPassword() {
                Claims claims = jwtUtil.decode(givenExistedToken);

                assertThat(claims.get("email", String.class)).isEqualTo(EXISTED_EMAIL);
                assertThat(claims.get("password", String.class)).isEqualTo(EXISTED_PASSWORD);
            }
        }

        @Nested
        @DisplayName("만약 내용이 비어 있는 토큰이 주어진다면")
        class Context_WithEmptyToken {
            private final String token = "";

            @Test
            @DisplayName("토큰이 유효하지 않다는 메세지를 리턴한다")
            void itReturnsInValidTokenMessage() {
                assertThatThrownBy(() -> jwtUtil.decode(token))
                        .isInstanceOf(InvalidTokenException.class)
                        .hasMessageContaining("Invalid token");
            }
       }

       @Nested
       @DisplayName("만약 아무런 값이 없는 null 이 주어진다면")
       class Context_WithNull {
           @Test
           @DisplayName("토큰이 유효하지 않다는 메세지를 리턴한다")
           void itReturnsInvalidTokenMessage() {
               assertThatThrownBy(() -> jwtUtil.decode(null))
                       .isInstanceOf(InvalidTokenException.class)
                       .hasMessageContaining("Invalid token");
           }
       }
    }
}
