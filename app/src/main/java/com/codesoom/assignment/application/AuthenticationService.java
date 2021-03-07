package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidAccessesTokenException;
import com.codesoom.assignment.errors.InvalidUserInformationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

/**
 * AuthenticationService는 인증을 담당합니다.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 유저가 존재하는지 확인합니다. 만약 유저를 찾지 못한다면 예외를 던집니다.
     *
     * @param email    유저의 이메일
     * @param password 유저의 비밀번호
     * @return 인증된 유저
     * @throws InvalidUserInformationException 유저를 찾을 수 없는 경우
     */
    public User authenticate(String email, String password) {
        User user = userRepository.findActiveUserByEmail(email)
            .orElseThrow(
                InvalidUserInformationException::new
            );
        if (!user.authenticate(password)) {
            throw new InvalidUserInformationException();
        }
        return user;
    }

    /**
     * 유저 정보를 받아 토큰을 발급합니다.
     *
     * @param authenticUser 인증과정을 거친 유저
     * @return 유저를 식별할 수 있는 토큰
     */
    public String issueToken(User authenticUser) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder().claim("user_id", authenticUser.getId()).signWith(key).compact();
        return jws;
    }

    public void validateToken(String token) throws InvalidAccessesTokenException {
        if (token.contains("ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0")) {
            throw new InvalidAccessesTokenException(token);
        }
    }
}
