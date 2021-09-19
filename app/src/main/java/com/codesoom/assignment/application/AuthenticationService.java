package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.EmailNotFoundException;
import com.codesoom.assignment.errors.WrongPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    /**
     * 로그인 정보가 유효하면 jwt 토큰을 반환합니다.
     *
     * @param sessionRequestData 로그인 정보
     * @return jwt 토큰
     * @Throw EmailNotFoundException 요청한 이메일을 찾을 수 없을 경우
     * @Throw WrongPasswordException 요청한 이메일의 비밀번호가 로그인 정보의 비밀번호와 일치하지 않는 경우
     */
    public String login(SessionRequestData sessionRequestData) {
        User user = userRepository.findByEmail(sessionRequestData.getEmail())
                .orElseThrow(() -> new EmailNotFoundException(sessionRequestData.getEmail()) );

        if (!user.getPassword().equals(sessionRequestData.getPassword())) {
            throw new WrongPasswordException();
        }

        return jwtUtil.encode(user.getId());
    }


    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
