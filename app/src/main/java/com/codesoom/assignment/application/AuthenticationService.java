package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.Identifier;
import com.codesoom.assignment.errors.LoginDataNotMatchedException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 인증 token 정보를 관리한다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * 로그인 정보를 조회해서 토큰을 발급한다.
     *
     * @param identifier 이메일과 비밀번호가 포함된 로그인 정보
     * @return 토큰
     * @throws LoginDataNotMatchedException 아이디 혹은 비밀번호가 유효하지 않다.
     */
    public String login(Identifier identifier) {

        final User foundUser = userRepository.findByEmailAndDeletedIsFalse(identifier.getEmail())
                .orElseThrow(LoginDataNotMatchedException::new);

        if (!foundUser.authenticate(identifier.getPassword())) {
            throw new LoginDataNotMatchedException();
        }

        return jwtUtil.encode(foundUser.getId());
    }
}
