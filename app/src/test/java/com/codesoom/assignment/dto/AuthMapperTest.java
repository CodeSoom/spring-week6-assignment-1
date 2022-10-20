package com.codesoom.assignment.dto;

import com.codesoom.assignment.application.AuthCommand;
import com.codesoom.assignment.common.mapper.AuthMapper;
import com.codesoom.assignment.utils.LoginSampleFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthMapper 클래스")
class AuthMapperTest {

    @Nested
    @DisplayName("of(LoginParam) 메소드는")
    class Describe_of_login_param {
        @Nested
        @DisplayName("유효한 로그인 파라미터가 주어지면")
        class Context_with_valid_login_parameter {
            @Test
            @DisplayName("Login 객체를 리턴한다")
            void it_returns_login() {
                final AuthDto.LoginParam loginParam = LoginSampleFactory.createLoginParam();

                final AuthCommand.Login actual = AuthMapper.INSTANCE.of(loginParam);

                assertThat(actual).isInstanceOf(AuthCommand.Login.class);
            }
        }

        @Nested
        @DisplayName("로그인 파라미터가 Null이면")
        class Context_with_invalid_login_parameter {
            @Test
            @DisplayName("Null을 리턴한다")
            void it_returns_null() {
                final AuthCommand.Login actual = AuthMapper.INSTANCE.of(null);

                assertThat(actual).isNull();
            }
        }

    }
}
