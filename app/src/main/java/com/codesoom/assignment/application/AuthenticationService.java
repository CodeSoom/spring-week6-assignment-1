package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginNotMatchPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utills.JwtUtill;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 사용자 인증, 인가와 관련된 처리를 합니다.
 */
@Service
public class AuthenticationService {
    private final JwtUtill jwtUtill;
    private final UserRepository userRepository;

    public AuthenticationService(final JwtUtill jwtUtill, final UserRepository userRepository) {
        this.jwtUtill = jwtUtill;
        this.userRepository = userRepository;
    }

    /**
     * 사용자가 로그인하면 아이디를 인코딩하여 리턴합니다.
     *
     * @return 인코딩된 아이디
     * @throws UserNotFoundException          사용자 이메일을 찾을 수 없는 경우 던집니다.
     * @throws LoginNotMatchPasswordException 로그인 시 password가 일치하지 않은 경우 던집니다.
     */
    public String login(final UserLoginData userLoginData) {
        String userEmail = userLoginData.getEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        if (!user.authenticate(userLoginData.getPassword())) {
            throw new LoginNotMatchPasswordException(user.getEmail());
        }

        return jwtUtill.encode(user.getId());
    }

    /**
     * 인증된 토큰을 받아 아이디를 디코딩하여 리턴합니다.
     *
     * @return 사용자 아이디
     * @throws InvalidTokenException 인증되지 않은 토큰일 경우 던집니다.
     */
    public Long parseToken(String token) {
        Claims claims = jwtUtill.decode(token);
        return claims.get("userId", Long.class);
    }
}
