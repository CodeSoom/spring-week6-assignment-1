package com.codesoom.assignment.application.auth;

import com.codesoom.assignment.application.users.UserNotFoundException;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.dto.LoginRequestDto;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService implements AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserRepository repository;

    public UserLoginService(JwtUtil jwtUtil, UserRepository repository) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        User user = repository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없으므로 로그인에 실패했습니다."));

        if (!user.authenticate(loginRequestDto.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않아 로그인에 실패했습니다.");
        }

        return jwtUtil.encode(user.getId());
    }

}
