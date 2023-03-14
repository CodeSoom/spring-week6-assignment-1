package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private JwtUtil jwtUtil;

    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    public String login(UserLoginData userLoginData) {

        String inputEmail = userLoginData.getEmail();
        String inputPassword = userLoginData.getPassword();

        User user = userRepository.findByEmailAndDeletedIsFalse(inputEmail)
                .orElseThrow(() -> new LoginFailException());

        if(!user.getPassword().equals(inputPassword)){
            throw new LoginFailException();
        }
        return jwtUtil.encode(user.getId());
    }

    public Long parseToken(String accessToken) {

        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);

    }

}
