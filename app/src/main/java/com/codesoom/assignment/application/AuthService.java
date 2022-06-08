package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResult;
import com.codesoom.assignment.errors.AuthenticationException;
import com.codesoom.assignment.helper.AuthJwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthJwtHelper authJwtHelper;
    private final UserRepository userRepository;

    public LoginResult login(LoginData loginData) {
        User user = userRepository.findByEmail(loginData.getEmail())
                .orElseThrow(() ->
                        new AuthenticationException(loginData.getEmail() + "은 존재하지 않는 이메일입니다."));

        if (!user.getPassword().equals(loginData.getPassword())) {
            throw new AuthenticationException("패스워드가 일치하지 않습니다.");
        }

        String accessToken = authJwtHelper.encode(user.getId());

        return new LoginResult(accessToken);
    }
}
