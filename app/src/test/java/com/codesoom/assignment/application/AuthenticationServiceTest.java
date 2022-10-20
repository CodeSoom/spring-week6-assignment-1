package com.codesoom.assignment.application;

import com.codesoom.assignment.common.exception.InvalidParamException;
import com.codesoom.assignment.common.mapper.AuthMapper;
import com.codesoom.assignment.common.utils.JwtUtil;
import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import com.codesoom.assignment.dto.AuthDto;
import com.codesoom.assignment.utils.LoginSampleFactory;
import com.codesoom.assignment.utils.UserSampleFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    private final AuthMapper authMapper = AuthMapper.INSTANCE;
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, jwtUtil);
    }


    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {
        private final User existUser = userRepository.save(UserSampleFactory.createUser());
        private final String VALID_TOKEN = jwtUtil.encode(existUser.getId());

        @Nested
        @DisplayName("존재하지않는 이메일이 주어지면")
        class Context_with_not_existed_email {
            AuthDto.LoginParam loginParam = LoginSampleFactory.createLoginParam();
            AuthCommand.Login loginCommand = authMapper.of(loginParam);

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                Assertions.assertThatThrownBy(() -> authenticationService.login(loginCommand)).isInstanceOf(InvalidParamException.class);
            }
        }

        @Nested
        @DisplayName("잘못된 패스워드가 주어지면")
        class Context_with_wrong_password {
            AuthCommand.Login loginCommand;

            @BeforeEach
            void prepare() {
                AuthDto.LoginParam loginParam = new AuthDto.LoginParam();
                loginParam.setEmail(existUser.getEmail());
                loginParam.setPassword("wrongpassword");

                loginCommand = authMapper.of(loginParam);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                Assertions.assertThatThrownBy(() -> authenticationService.login(loginCommand)).isInstanceOf(InvalidParamException.class);
            }
        }

        @Nested
        @DisplayName("유효한 ID, 패스워드가 주어지면")
        class Context_with_id_and_password {
            AuthCommand.Login loginCommand;

            @BeforeEach
            void prepare() {
                AuthCommand.Login.LoginBuilder builder = AuthCommand.Login.builder();
                System.out.println("builder.toString() = " + builder.toString());

                loginCommand = builder.email(existUser.getEmail())
                        .password(existUser.getPassword())
                        .build();
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                String accessToken = authenticationService.login(loginCommand);

                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("isNotExistId 메소드는")
    class Describe_isNotExistId {
        @Nested
        @DisplayName("아이디가 주어지면")
        class Context_with_id {
            private final User existUser = userRepository.save(UserSampleFactory.createUser());

            @Test
            @DisplayName("존재여부를 확인한다")
            void it_confirms_exist() {
                assertThat(authenticationService.isNotExistId(existUser.getId())).isFalse();
                assertThat(authenticationService.isNotExistId(-1L)).isTrue();
            }
        }
    }
}