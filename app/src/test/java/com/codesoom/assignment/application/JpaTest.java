package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class JpaTest {
    private final String SECRET = "12345678901234567890123456789010";
    private final JwtUtil jwtUtil = new JwtUtil(SECRET);

    @Autowired
    public UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }
}
