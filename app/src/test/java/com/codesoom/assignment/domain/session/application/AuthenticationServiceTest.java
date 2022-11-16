package com.codesoom.assignment.domain.session.application;

import com.codesoom.assignment.common.util.JwtUtil;
import com.codesoom.assignment.domain.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.codesoom.assignment.support.IdFixture.ID_MIN;
import static com.codesoom.assignment.support.TokenFixture.ACCESS_TOKEN_1_VALID;
import static com.codesoom.assignment.support.UserFixture.USER_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/*
    - login 메서드는
        - 유효한 회원 정보가 주어지면
            - 토큰을 반환한다
        - 유효하지 않는 회원 정보가 주어지면
            - 찾을 수 없는 Email일 경우
                - 예외를 던진다 (UserNotFoundException)
            - 비밀번호가 틀렸을 경우
                - 예외를 던진다 (InvalidUserPasswordException)
*/
@DisplayName("AuthenticationService 단위 테스트")
class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);

    @BeforeEach
    void setUpVariable() {
        authenticationService = new AuthenticationService(userRepository, jwtUtil);
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class login_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_회원_정보가_주어지면 {
            @BeforeEach
            void setUpGiven() {
                given(userRepository.findByEmail(USER_1.EMAIL()))
                        .willReturn(
                                Optional.of(USER_1.회원_엔티티_생성(ID_MIN.value()))
                        );

                given(jwtUtil.encode(ID_MIN.value()))
                        .willReturn(ACCESS_TOKEN_1_VALID.토큰());
            }

            @Test
            @DisplayName("토큰을 반환한다")
            void it_returns_token() {
                String accessToken = authenticationService.login(USER_1.로그인_요청_데이터_생성());


                assertThat(accessToken).isNotBlank()
                        .contains(".");

                verify(userRepository).findByEmail(USER_1.EMAIL());
                verify(jwtUtil).encode(ID_MIN.value());
            }
        }
    }
}
