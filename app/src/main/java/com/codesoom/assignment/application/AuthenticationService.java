package com.codesoom.assignment.application;

import com.codesoom.assignment.common.exception.InvalidParamException;
import com.codesoom.assignment.common.response.ErrorCode;
import com.codesoom.assignment.common.utils.JwtUtil;
import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(AuthCommand.Login command) {
        if (isNotExistEmail(command.getEmail())) {
            throw new InvalidParamException(ErrorCode.INVALID_EMAIL);
        }

        final User loginUser = findUser(command);
        
        return jwtUtil.encode(loginUser.getId());
    }

    private User findUser(AuthCommand.Login command) {
        return userRepository.findByEmailAndPassword(command.getEmail(), command.getPassword())
                .orElseThrow(throwIncorrectPasswordException());
    }

    private static Supplier<InvalidParamException> throwIncorrectPasswordException() {
        return () -> new InvalidParamException(ErrorCode.INVALID_PASSWORD);
    }

    private boolean isNotExistEmail(String email) {
        return !userRepository.existsByEmail(email);
    }
}
