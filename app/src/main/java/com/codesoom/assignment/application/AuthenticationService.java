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
 * 인증을 위한 비즈니스 로직을 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    /**
     * 주어진 회원 로그인 정보에 해당하는 액세스 토큰을 리턴합니다.
     *
     * @param userLoginData 회원 로그인 정보
     * @return 생성된 액세스 토큰
     * @throws UserAuthenticationFailException 주어진 회원 로그인 정보가 유효하지 않을 경우
     */
    @Transactional(readOnly = true)
    public String login(UserLoginData userLoginData)
            throws UserAuthenticationFailException {
        User user = findUserByEmail(userLoginData.getEmail());

        if (!user.matchPassword(userLoginData.getPassword())) {
            throw new UserAuthenticationFailException("비밀번호가 일치하지 않습니다.");
        }

        if (user.isDeleted()) {
            throw new UserAuthenticationFailException("이미 삭제된 회원입니다.");
        }

        return jwtUtil.encode(user.getId());
    }

    /**
     * 주어진 액세스 토큰을 파싱한 뒤 파싱된 값을 리턴합니다.
     *
     * @param accessToken 액세스 토큰
     * @return 파싱된 값
     */
    public Long parseToken(String accessToken) {
        return jwtUtil.decode(accessToken)
                .get("userId", Long.class);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserAuthenticationFailException("주어진 이메일에 해당하는 회원이 없습니다."));
    }

}
