package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private UserService userService;
    private JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil, UserService userService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 로그인 처리 로
     *
     * @return JWT
     */
    public String login(final SessionRequestData requestData) {
        User user = userService.findUserByEmailAndPassword(requestData.getEmail(),
                                                           requestData.getPassword());
        return jwtUtil.customEncode(user.getId());
    }

    public Long parseToken(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new InvalidTokenException(authorization);
        }

        String token = authorization.substring("Bearer ".length());
        return jwtUtil.customDecode(token)
                .get("userId", Long.class);
    }
}
