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
//        authenticationService = new AuthenticationService(jwtUtil);
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
