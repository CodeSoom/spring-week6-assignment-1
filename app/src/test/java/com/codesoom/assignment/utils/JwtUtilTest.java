package com.codesoom.assignment.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.codesoom.assignment.errors.UnauthorizedException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

  private static final String SECRET = "01234567reterter8912rfqedc01234567";
  private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.HLQ9r9Devo7ni2MaJIBFWr_t-2GcZWO4H5nYwLjIpJc";
  private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.HLQ9r9Devo7ni2MaJIBFWr_t-2GcZWO4H5nYwLjIsdfsdc";

  private JwtUtil jwtUtil;

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil(SECRET);
  }


  @Test
  void encode() {
    String accessToken = jwtUtil.encode(1L);

    assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
  }

  @Test
  void decodeWithValidToken() {
    Claims claims = jwtUtil.decode(ACCESS_TOKEN);

    assertThat(claims.get("userId", Long.class)).isEqualTo(1L);

  }


  @Test
  void decodeWithInvalidToken() {
    assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
        .isInstanceOf(UnauthorizedException.class);

  }

  @Test
  void empty_token_decode() {
    assertAll(
        () -> {
          assertThatThrownBy(() -> {
            jwtUtil.decode("");
          });
        },
        () -> {
          assertThatThrownBy(() -> {
            jwtUtil.decode(" ");
          });
        },
        () -> {
          assertThatThrownBy(() -> {
            jwtUtil.decode(null);
          });
        });
  }


}
