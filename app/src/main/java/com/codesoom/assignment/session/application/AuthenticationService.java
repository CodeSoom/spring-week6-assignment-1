package com.codesoom.assignment.session.application;

import com.codesoom.assignment.common.util.JwtUtil;
import com.codesoom.assignment.session.controller.dto.SessionRequestDto;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import com.codesoom.assignment.user.exception.UserInvalidPasswordException;
import com.codesoom.assignment.user.exception.UserNotFoundException;
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

        if (isIncorrectPassword(sessionRequestDto, user)) {
            throw new UserInvalidPasswordException(sessionRequestDto.getEmail());
        }

        return jwtUtil.encode(user.getId());
    }

    private boolean isIncorrectPassword(final SessionRequestDto sessionRequestDto, final User user) {
        return !sessionRequestDto.getPassword().equals(user.getPassword());
    }
}
