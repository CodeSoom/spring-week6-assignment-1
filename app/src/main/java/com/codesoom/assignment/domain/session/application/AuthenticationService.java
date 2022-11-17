package com.codesoom.assignment.domain.session.application;

import com.codesoom.assignment.common.util.JwtUtil;
import com.codesoom.assignment.domain.session.controller.dto.SessionRequestDto;
import com.codesoom.assignment.domain.user.domain.User;
import com.codesoom.assignment.domain.user.domain.UserRepository;
import com.codesoom.assignment.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationService(final UserRepository userRepository, final JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(final SessionRequestDto sessionRequestDto) {
        User user = userRepository.findByEmail(sessionRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(sessionRequestDto.getEmail()));

        return jwtUtil.encode(user.getId());
    }
}
