package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginDto;
import com.codesoom.assignment.errors.InvalidUserException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 회원 인증 로직을 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    /**
     * 검증된 회원이면 access token을 반환합니다.
     *
     * @param userLoginDto 로그인 정보
     * @return JWT Access Token
     * @throws InvalidUserException 로그인정보가 유효하지 않는 경우
     */
    public String login(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new InvalidUserException());

        if (!user.getPassword().equals(userLoginDto.getPassword()) || user.isDeleted()) {
            throw new InvalidUserException();
        }
        return jwtUtil.encode(user.getId());
    }
}
