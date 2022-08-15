package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidInformationException;
import com.codesoom.assignment.errors.UnAuthorizedException;
import com.codesoom.assignment.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtService implements AuthenticationService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtService(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    public String login(UserLoginData data) {
        Optional<User> user = userRepository.findByEmail(data.getEmail());

        if (user.isEmpty() || user.get().isUnMatch(data)) {
            throw new InvalidInformationException();
        }

        return jwtUtils.encode(data.getEmail());
    }

    @Override
    public Claims parseToken(String token) {
        if (token.isBlank() && token.length() < "Bearer ".length()) {
            throw new UnAuthorizedException();
        }

        return jwtUtils.decode(token);
    }
}
