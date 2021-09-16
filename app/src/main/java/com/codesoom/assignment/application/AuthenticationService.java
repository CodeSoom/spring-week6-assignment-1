package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.LoginInconsistencyException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.errors.UnauthorizedException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 로그인을 검증합니다.
     * @param loginData 로그인 데이터
     * @return 로그인에 성공할 경우 토큰발급
     */
    public String login(UserLoginData loginData) {

        User user = findUser(loginData);

        if(user.getPassword().equals(loginData.getPassword())) {
            return jwtUtil.encode(user.getId());
        } else {
            throw new LoginInconsistencyException();
        }

    }

    /**
     * 토큰을 데이터로 변환합니다.
     * @param accessToken 토큰값
     * @return 유저 아이디
     */
    public Long parseToken(String accessToken) {

        if(accessToken==null || accessToken.isBlank()) {
            throw new UnauthorizedException();
        }

        try {
            Claims decode = jwtUtil.decode(accessToken);
            Long userId = decode.get("userId", Long.class);
            return userId;
        }catch (SignatureException e) {
            throw new UnauthorizedException();
        }

     }

    /**
     * 이메일이 존재하는지 확인합니다.
     * @param loginData 로그인 데이터
     * @return 이메일이 존재할 경우 해당 User 정보를 리턴합니다.
     * @throws UserNotFoundException 찾지 못할경우 예외발생
     */
    public User findUser(UserLoginData loginData) {

        return userRepository.findByEmail(loginData.getEmail()).orElseThrow(() -> new UserNotFoundException());

    }

}
