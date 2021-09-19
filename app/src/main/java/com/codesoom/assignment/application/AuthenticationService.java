package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.EmailNotFoundException;
import com.codesoom.assignment.errors.UnauthorizedException;
import com.codesoom.assignment.errors.WrongPasswordException;
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
     * 토큰을 decode한 후 속성정보를 반환한다.
     *
     * @param accessToken 인증토큰
     * @return 속성 정보
     * @throws UnauthorizedException 인증에 실패한 경우
     */
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

    /**
     * 로그인 데이터로 유저 정보 조회후, 사용자 정보로 생성한 인증토큰을 반환한다.
     *
     * @param loginData 로그인 데이터
     * @return 인증토큰
     * @throws WrongPasswordException 비밀번호가 틀린 경우
     */
    public String createToken(UserLoginData loginData) {
        User user = findUserByEmail(loginData);
        if (!user.authenticate(loginData.getPassword())) {
            throw new WrongPasswordException(loginData);
        }
        return jwtUtil.encode(user.getId());
    }

    /**
     * 주어진 로그인 데이터와 일치하는 유저를 반환한다.
     *
     * @param loginData 로그인 데이터
     * @return 유저
     * @throws EmailNotFoundException 로그인 정보의 이메일을 찾지 못한 경우
     */
    private User findUserByEmail(UserLoginData loginData) {
        return userRepository.findByEmail(loginData.getEmail())
                .orElseThrow(() -> new EmailNotFoundException(loginData));
    }


}

