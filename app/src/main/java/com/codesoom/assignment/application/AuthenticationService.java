package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 로그인 데이터로 유저 존재 여부를 확인하고, access token을 리턴합니다.
     *
     * @param sessionRequestData 로그인 데이터
     * @return access token
     * @throws UserNotFoundException 이메일, 패스워드에 해당되는 유저가 없을 경우
     */
    public String login(SessionRequestData sessionRequestData) {
        User user = userRepository.findByEmailAndPassword(sessionRequestData.getEmail(), sessionRequestData.getPassword())
                .orElseThrow(() -> new LoginFailException(sessionRequestData.getEmail()));

        return jwtUtil.encode(user.getId());
    }

    /**
     * access token을 디코드하여 userId 값을 리턴합니다.
     *
     * @param accessToken 전달받은 access token
     * @return user의 id
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);

        return claims.get("userId", Long.class);
    }
}
