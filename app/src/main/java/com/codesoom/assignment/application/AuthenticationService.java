package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private UserService userService;
    private JwtUtil jwtUtil;

    public AuthenticationService(final UserService userService,
                                 final JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 사용자 식별자로 JWT를 발급해서 리턴합니다.
     *
     * @return JWT
     */
    public String login(final SessionRequestData requestData) {
        User user =
                userService.findUserByEmailAndPassword(requestData.getEmail(),
                                                       requestData.getPassword());
        return jwtUtil.encode(user.getId());
    }

    public Long parseToken(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new InvalidTokenException(authorization);
        }

        String token = authorization.substring("Bearer ".length());
        return jwtUtil.decode(token)
                      .get("userId", Long.class);
    }
}
