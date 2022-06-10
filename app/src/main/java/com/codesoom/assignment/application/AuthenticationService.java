package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UserAuthenticationFailException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증을 위한 비지니스 로직을 담당한다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * 주어진 회원을 로그인해 액세스 토큰을 리턴한다.
     *
     * @param userLoginData - 사용자 로그인 정보
     * @return 생성된 액세스 토큰
     * @throws UserAuthenticationFailException 주어진 회원 로그인 정보가 유효하지 않을 경우
     */
    @Transactional
    public String login(UserLoginData userLoginData) throws UserAuthenticationFailException {
        User user = findUserByEmail(userLoginData.getEmail());

        if (!user.matchPassword(userLoginData.getPassword())) {
            throw new UserAuthenticationFailException("잘못된 비밀번호입니다.");
        }

        if (user.isDeleted()) {
            throw new UserAuthenticationFailException("이미 삭제된 회원입니다.");
        }

        return jwtUtil.encode(user.getId());
    }

    /**
     * 주어진 액세스 토큰을 파싱한 값을 리턴한다.
     *
     * @param accessToken - 액세스 토큰
     * @return 파싱한 값
     */
    public Long parseToken(String accessToken) {
        return jwtUtil.decode(accessToken)
                .get("userId", Long.class);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserAuthenticationFailException("잘못된 이메일 주소입니다."));
    }
}
