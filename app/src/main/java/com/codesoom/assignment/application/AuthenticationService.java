package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.SessionLoginData;
import com.codesoom.assignment.errors.LoginWrongPasswordException;
import com.codesoom.assignment.utils.JwtCodec;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private JwtCodec jwtCodec;
    private UserService userService;

    public AuthenticationService(JwtCodec jwtCodec, UserService userService) {
        this.jwtCodec = jwtCodec;
        this.userService = userService;
    }

    public String login(SessionLoginData sessionLoginData){
        User user = userService.findUserByEmail(sessionLoginData.getEmail());

        if (!user.authenticate(sessionLoginData.getPassword())){
            throw new LoginWrongPasswordException();
        }

        return jwtCodec.encode(user.getId());
    }

    public Long parseToken(final String accessToken) {
        Claims claims = jwtCodec.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
