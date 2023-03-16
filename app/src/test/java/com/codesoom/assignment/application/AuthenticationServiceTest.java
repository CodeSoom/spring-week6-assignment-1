package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginRequest;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginNotMatchException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJ1c2VySWQiOjF9.bOQfc4mYhcmbCH6JjPuzSc6eRJEr_A0d8tVrPtxE9aU";
    private static final String INVALID_TOKEN = VALID_TOKEN + "WRONG";


    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);

    private UserLoginRequest userLoginRequest , request;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(userRepository, jwtUtil);

        userLoginRequest = UserLoginRequest.builder()
                .email("mugeon@email.com")
                .password("1234")
                .build();

        request = UserLoginRequest.builder()
                .email("111")
                .password("111")
                .build();

        User user = User.builder()
                .id(1L)
                .name("김무건")
                .email("mugeon@email.com")
                .password("1234")
                .build();

        given(userRepository.findByEmailAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword()))
                .willReturn(Optional.of(user));

        given(userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()))
                .willReturn(Optional.empty());

    }

    @Test
    @DisplayName("login")
    public void login() throws Exception {
        String accessToken = authenticationService.login(userLoginRequest);
        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    @DisplayName("login")
    public void laaogin() throws Exception {

        Assertions.assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(LoginNotMatchException.class)
                .hasMessage("NOT MATCH LOGIN DATA");

    }

    @Test
    @DisplayName("parseTokenWithValidToken")
    public void parseTokenWithValidToken() throws Exception {
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("parseTokenWithInvalidToken")
    public void parseTokenWithInvalidToken() throws Exception {
        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Invalid Access Token");
    }

    @Test
    @DisplayName("parseTokenWithBlankToken")
    public void parseTokenWithBlankToken() throws Exception {
        assertThatThrownBy(() -> authenticationService.parseToken(""))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> authenticationService.parseToken(null))
                .isInstanceOf(InvalidTokenException.class);
    }

}