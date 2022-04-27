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
                .orElseThrow(() -> new UserNotFoundException("이메일과 일치하는 회원을 찾을 수 없습니다."));

        if (!user.authenticate(loginRequestDto.getPassword())) {
            throw new InvalidPasswordException("정확한 비밀번호를 입력하세요.");
        }

        return jwtUtil.encode(user.getId());
    }

}
