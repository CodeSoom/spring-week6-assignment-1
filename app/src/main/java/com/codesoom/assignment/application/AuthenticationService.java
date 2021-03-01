package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UserAuthenticationFailException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public String login(UserLoginData userLoginData) throws UserAuthenticationFailException {
        User user = userService.findUserByEmail(userLoginData.getEmail());

        user.authenticate(userLoginData.getPassword());

        return jwtUtil.encode(user.getId());
    }

}
