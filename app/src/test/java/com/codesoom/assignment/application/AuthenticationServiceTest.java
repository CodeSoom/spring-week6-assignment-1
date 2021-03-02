package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
  final Long USER_ID = 1L;
  final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.5VoLz2Ug0E6jQK_anP6RND1YHpzlBdxsR2ORgYef_aQ";
  final String secretKey = "qwertyuiopqwertyuiopqwertyuiopqw";

  AuthenticationService authService;

  @BeforeEach
  void setUp() {
    authService = new AuthenticationService(new JwtUtil(secretKey));
  }

  @Nested
  @DisplayName("encode()")
  class Describe_encode {
    @Nested
    @DisplayName("user id가 주어졌을 때")
    class Context_user_id {
      Long givenUserId = USER_ID;

      @DisplayName("인코딩된 token을 반환한다.")
      @Test
      void it_returns_encoded_token() {
        String code = authService.encode(givenUserId);
        assertThat(code).isEqualTo(VALID_TOKEN);
      }
    }
  }
}