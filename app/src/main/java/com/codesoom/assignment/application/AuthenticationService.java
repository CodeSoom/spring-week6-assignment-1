package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.EmailNotFoundException;
import com.codesoom.assignment.errors.UnauthorizedException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

/**
 * 유저 인증 로직 담당.
 */
@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    /**
     * 유저 id로 인코딩한 토큰 return.
     * @param userId
     * @return 인증 토큰
     */
    public String login(Long userId) {
        return jwtUtil.encode(userId);
    }


    public Long parsetoken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new UnauthorizedException(accessToken);
        }
        try {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new UnauthorizedException(accessToken);
        }
    }

    public String createToken(UserLoginData loginData) {
        User user = findUserByEmail(loginData);

        return encode(user.getId());
    }

    private User findUserByEmail(UserLoginData loginData) {
        return userRepository.findByEmail(loginData.getEmail())
                .orElseThrow(() -> new EmailNotFoundException(loginData));
    }


}

