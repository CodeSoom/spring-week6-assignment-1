package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.UserAuthenticationFailedException;
import com.codesoom.assignment.errors.UserEmailNotExistException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  public String encode(Long userId) {
    return jwtUtil.encode(userId);
  }

  public Long decode(String givenToken) {
    return jwtUtil.decode(givenToken);
  }

  public String createSession(String email, String password) {
    User user = findUserByEmail(email);
    if (!user.authenticate(password)) {
      throw new UserAuthenticationFailedException(email);
    }

    return encode(user.getId());
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserEmailNotExistException(email));
  }
}
