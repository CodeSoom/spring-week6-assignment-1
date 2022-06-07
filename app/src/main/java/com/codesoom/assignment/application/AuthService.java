package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResult;
import com.codesoom.assignment.helper.AuthJwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthJwtHelper authJwtHelper;

    public LoginResult login(LoginData loginData) {
        String accessToken = authJwtHelper.encode(1L);
        return new LoginResult(accessToken);
    }
}
