package com.codesoom.assignment.auth.application;

import com.codesoom.assignment.auth.infra.JwtTokenProvider;
import com.codesoom.assignment.user.application.UserEmailNotFoundException;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // TODO : 암호가 일치하는지 체크한다.
    // TODO : 유효한 사용자면 토큰을 발행한다.
    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));
        return jwtTokenProvider.createToken(user.getId());
    }
}
