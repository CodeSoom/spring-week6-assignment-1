package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
  final Long USER_ID = 1L;
  final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.5VoLz2Ug0E6jQK_anP6RND1YHpzlBdxsR2ORgYef_aQ";
  final String secretKey = "qwertyuiopqwertyuiopqwertyuiopqw";
  final String VALID_EMAIL = "rlawlstjd@gmail.com";
  final String VALID_PASSWORD = "12345";

  final User GIVEN_USER = User.builder()
      .id(1L)
      .name("rlawlstjd")
      .email(VALID_EMAIL)
      .password(VALID_PASSWORD)
      .build();

  AuthenticationService authService;

  @BeforeEach
  void setUp() {
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    authService = new AuthenticationService(new JwtUtil(secretKey), userRepository);

    given(userRepository.findByEmail(VALID_EMAIL)).willReturn(Optional.of(GIVEN_USER));
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

  @Nested
  @DisplayName("decode()")
  class Describe_decode {
    @Nested
    @DisplayName("token이 주어졌을 때")
    class Context_with_token {
      String givenToken = VALID_TOKEN;

      @DisplayName("user id를 반환한다.")
      @Test
      void it_returns_user_id() {
        Long userId = authService.decode(givenToken);
        assertThat(userId).isEqualTo(USER_ID);
      }
    }
  }

  @Nested
  @DisplayName("createSession()")
  class Describe_create_session {
    @Nested
    @DisplayName("유효한 email, password가 주어진다면")
    class Context_with_valid_email_password {
      String givenEmail = VALID_EMAIL;
      String givenPassword = VALID_PASSWORD;

      @DisplayName("token을 반환한다")
      @Test
      void it_returns_token() {
        String token = authService.createSession(givenEmail, givenPassword);
        assertThat(token).isEqualTo(VALID_TOKEN);
      }
    }
  }
}