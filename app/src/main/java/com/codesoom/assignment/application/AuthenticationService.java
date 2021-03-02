package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final JwtUtil jwtUtil;

  public AuthenticationService(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  public String encode(Long userId) {
    return jwtUtil.encode(userId);
  }
}
