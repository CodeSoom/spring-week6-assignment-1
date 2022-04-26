package com.codesoom.assignment.application.auth;

import com.codesoom.assignment.application.users.UserNotFoundException;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.dto.LoginRequestDto;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserRepository repository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository repository) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }

    /**
     * 로그인 정보로 인증하고, 토큰을 발급합니다.
     *
     * @param loginRequestDto 로그인 요청 정보
     * @return 발급된 토큰
     * @throws UserNotFoundException 이메일과 일치하는 회원이 없는 경우
     * @throws InvalidPasswordException 비밀번호가 일치하지 않을 경우
     */
    public String login(LoginRequestDto loginRequestDto) {
        User user = repository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("이메일과 일치하는 회원을 찾을 수 없습니다."));

        if (!user.authenticate(loginRequestDto.getPassword())) {
            throw new InvalidPasswordException("정확한 비밀번호를 입력하세요.");
        }

        return jwtUtil.encode(user.getId());
    }

}
