package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.PasswordInValidException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    /**
     * 로그인 요청 데이터로 사용자를 인증한 후 토큰을 만들어 리턴합니다.
     *
     * @param loginRequestData 로그인 요청 데이터
     * @return 토큰을 리턴합니다.
     */
    public String login(LoginRequestData loginRequestData) {
        User user = authenticateLoginRequest(loginRequestData);
        return jwtUtil.encode(user.getId());
    }

    /**
     * 로그인에 대한 인증을 진행합니다.
     *
     * @param loginRequestData 로그인 요청 데이터
     * @return 로그인 된 사용자를 리턴합니다.
     */
    private User authenticateLoginRequest(LoginRequestData loginRequestData) {
        String email = loginRequestData.getEmail();
        String password = loginRequestData.getPassword();

        User user = userService.findUserByEmail(email);
        if (!password.equals(user.getPassword())) {
            throw new PasswordInValidException();
        }
        return user;
    }

    /**
     * 토큰을 디코딩해 userId를 리턴합니다.
     *
     * @param accessToken 액세스 토큰
     * @return 사용자 아이디
     */
    public Long parseToken(String accessToken) {
        if ("".equals(accessToken) || accessToken==null) {
            throw new InvalidAccessTokenException(accessToken);
        }

        try {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidAccessTokenException(accessToken);
        }

    }
}
