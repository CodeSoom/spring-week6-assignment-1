package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResult;
import com.codesoom.assignment.errors.BadPasswordException;
import com.codesoom.assignment.errors.UserEmailNotFoundException;
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
                .orElseThrow(() -> new UserEmailNotFoundException(loginData.getEmail()));

        if (!user.getPassword().equals(loginData.getPassword())) {
            throw new BadPasswordException("패스워드가 일치하지 않습니다.");
        }

        String accessToken = authJwtHelper.encode(user.getId());

        return new LoginResult(accessToken);
    }
}
