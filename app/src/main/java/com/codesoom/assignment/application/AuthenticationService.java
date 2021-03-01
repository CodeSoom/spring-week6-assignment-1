package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UserAuthenticationFailException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 인증을 위한 비즈니스 로직을 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    /**
     * 주어진 회원 로그인 정보에 해당하는 액세스 토큰을 리턴합니다.
     *
     * @param userLoginData 회원 로그인 정보
     * @return 생성된 액세스 토큰
     * @throws UserAuthenticationFailException 주어진 회원 로그인 정보가 유효하지 않을 경우
     */
    public String login(UserLoginData userLoginData) throws UserAuthenticationFailException {
        User user = userService.findUserByEmail(userLoginData.getEmail());

        user.authenticate(userLoginData.getPassword());

        return jwtUtil.encode(user.getId());
    }

}
