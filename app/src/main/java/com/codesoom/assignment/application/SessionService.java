package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginRequest;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtEncoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;

    public SessionService(JwtEncoder jwtEncoder, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
    }

    public String login(LoginRequest loginRequest) {
        User findUser = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("찾을 수 없는 email 입니다"));

        if (isIncorrectPassword(loginRequest.getPassword(), findUser.getPassword())) {
            throw new LoginFailException("잘못된 password 입니다");
        }

        return jwtEncoder.encode(findUser.getId());
    }

    private boolean isIncorrectPassword(String requestPassword, String findUserPassword) {
        return !findUserPassword.equals(requestPassword);
    }

    public Long parseToken(String token) {
        Claims claims = jwtEncoder.decode(token);

        Long userId = getUserId(claims);

        isExistUser(userId);

        return userId;
    }

    private Long getUserId(Claims claims) {
        try {
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidTokenException("userId 가 존재하지 않습니다");
        }
    }

    private void isExistUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
    }
}
