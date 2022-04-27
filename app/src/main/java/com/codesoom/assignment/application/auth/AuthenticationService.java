package com.codesoom.assignment.application.auth;

import com.codesoom.assignment.application.users.UserNotFoundException;
import com.codesoom.assignment.dto.LoginRequestDto;

/**
 * 회원 인증을 담당합니다.
 */
public interface AuthenticationService {

    /**
     * 로그인 정보로 인증하고, 토큰을 발급합니다.
     *
     * @param loginRequestDto 로그인 요청 정보
     * @return 발급된 토큰
     * @throws UserNotFoundException 이메일과 일치하는 회원이 없는 경우
     * @throws InvalidPasswordException 비밀번호가 일치하지 않을 경우
     */
    String login(LoginRequestDto loginRequestDto);

}
