package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.request.LoginRequest;
import com.codesoom.assignment.exception.UserNotFoundException;
import com.codesoom.assignment.exception.WrongUserPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthenticationService(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * 로그인을 처리하고 Token을 반환합니다.
     *
     * @param loginRequest 로그인할 회원 정보
     * @return 생성된 Token
     * @throws UserNotFoundException 입력한 이메일로 회원을 찾지 못한 경우
     * @throws WrongUserPasswordException 입력한 비밀번호가 저장된 비밀번호와 다른 경우
     */
    public String login(LoginRequest loginRequest) {
        User user = userService.findUserByEmail(loginRequest.getEmail());

        String password = user.getPassword();
        if (!password.equals(loginRequest.getPassword())) {
            throw new WrongUserPasswordException("로그인에 실패하였습니다.");
        }

        return jwtUtil.createToken(user.getId());
    }
}
