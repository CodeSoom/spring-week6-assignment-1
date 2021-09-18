package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.EmailNotFoundException;
import com.codesoom.assignment.errors.UnauthorizedException;
import com.codesoom.assignment.errors.WrongPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;


@DisplayName("AuthenticationService 테스트")
class AuthenticationServiceTest {

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9..JUJzrutozG-Ss4MpK1bnUqmsnLn1K7rKGFAqxzBYc3k";
    private static final String INVALID_TOKEN = VALID_TOKEN + "invalid";

    private static String SECRETE_KEY= "12345678901234567890123456789010";
    private User user;
    private UserLoginData loginData;
    private AuthenticationService authenticationService;
    private   User givenUser;
    private Mapper mapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRETE_KEY);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        authenticationService = new AuthenticationService(jwtUtil, userRepository);
        loginData = UserLoginData.builder()
                .email("test@test.codesoom")
                .password("password")
                .build();

        user = User.builder()
                .email("test@test.codesoom")
                .password("password")
                .build();

        given(userRepository.findByEmail(loginData.getEmail()))
                .willReturn(Optional.of(user));
    }


    @Nested
    @DisplayName("parseToken메소드는")
    class Describe_parseToken {
        @Nested
        @DisplayName("유효한 토큰이 주어질 때")
        class Context_WithValidToken {
            String givenValidToken = VALID_TOKEN;

            @DisplayName("userId를 반환한다.")
            @Test
            void it_ReturnsUserId() {
                Long userId = authenticationService.parsetoken(givenValidToken);
                System.out.println("userId = " + userId);
                assertThat(userId).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("유효하지않은 토큰이 주어질 때")
        class Context_WithInvalidToken {
            String givenInvalidToken = INVALID_TOKEN;


            @DisplayName("UnauthorizedException을 던진다.")
            @Test
            void it_ReturnsUserId() {
                assertThatThrownBy(
                        () -> authenticationService.parsetoken(givenInvalidToken)
                ).isInstanceOf(UnauthorizedException.class);
            }
        }

        @Nested
        @DisplayName("토큰이 비어있을 때")
        class Context_WithBlank {
            String blankToken= "";
            String nullToken ="";

            @DisplayName("UnauthorizedException을 던진다.")
            @Test
            void parseTokenWithBlank() {
                assertThatThrownBy(
                        () -> authenticationService.parsetoken(nullToken)
                ).isInstanceOf(UnauthorizedException.class);


                assertThatThrownBy(
                        () -> authenticationService.parsetoken(blankToken)
                ).isInstanceOf(UnauthorizedException.class);
            }
        }
    }



    @Nested
    @DisplayName("createToken 메소드는 ")
    class Describe_createToken {
        @Nested
        @DisplayName("유효한 로그인 데이터가 주어졌을 때")
        class Context_WithValidUserLoginData {


            @DisplayName("jwt토큰을 반환한다.")
            @Test
            void It_ReturnJwtToken() {
                String accessToken = authenticationService.createToken(loginData);
                System.out.println("accessToken = " + accessToken);
                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("유효하지않은 로그인 데이터가 주어졌을 때")
        class Context_WithInvalidUserLoginData {
            private User givenUser;


            @BeforeEach
            void setUp(){
                givenUser = userRepository.save(user);
                User userWithWrongPassword = User.builder().password("wrong password").build();
                givenUser.changeWith(userWithWrongPassword);
            }


            @DisplayName("jwt토큰을 반환한다.")
            @Test
            void createToken() {
                UserLoginData userLoginData = mapper.map(givenUser, UserLoginData.class);

                assertThatThrownBy(() -> authenticationService.createToken(userLoginData))
                        .isInstanceOf(WrongPasswordException.class);
            }
        }
    }
}
