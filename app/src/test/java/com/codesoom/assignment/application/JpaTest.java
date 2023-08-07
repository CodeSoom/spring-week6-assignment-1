package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class JpaTest {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public JwtUtil jwtUtil;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }
}
