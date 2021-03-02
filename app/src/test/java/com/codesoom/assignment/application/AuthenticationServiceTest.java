package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
  final Long userId = 1L;
  private AuthenticationService authService = new AuthenticationService();

  @Nested
  @DisplayName("encode()")
  class Describe_encode {
    @Nested
    @DisplayName("user id가 주어졌을 때")
    class Context_user_id {
      Long givenUserId = userId;

      @DisplayName("인코딩된 token을 반환한다.")
      @Test
      void it_returns_encoded_token() {
        String code = authService.encode(givenUserId);
        assertThat(code).contains(".");
      }
    }
  }
}