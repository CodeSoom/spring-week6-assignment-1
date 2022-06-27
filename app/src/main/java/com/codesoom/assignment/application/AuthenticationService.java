package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UserNotAuthenticationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthenticationService {
    private JwtUtil jwtUtil;
    private UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 회원 인증 후 엑세스 토큰 생성해 반환한다
     *
     * @param userLoginData 인증할 user
     * @return 생성된 엑세스 토큰
     * @throws UserNotFoundException 회원을 찾지 못한 경우
     * @throws UserNotAuthenticationException 비밀번호가 틀린 경우
     */
    public SessionResponseData login(UserLoginData userLoginData) {
        String email = userLoginData.getEmail();
        String password = userLoginData.getPassword();

        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if(!findUser.authenticate(password)) {
            throw new UserNotAuthenticationException(email);
        }

        return SessionResponseData.builder()
                .accessToken(jwtUtil.encode(
                        findUser.getId(),
                        findUser.getName(),
                        findUser.getEmail()
                ))
                .build();
    }
    
    public Long parseToken(String accessToken) {
        return jwtUtil.decode(accessToken)
                .get("userId", Long.class);
    }
}
