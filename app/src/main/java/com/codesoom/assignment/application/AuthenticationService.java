package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthenticationService(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    public String login(){
        return jwtUtil.encode(1L);
    }

    public void tokenValidation(String token){
        Long id = jwtUtil.getUserIdFromToken(token);
        userService.findUser(id);
    }
}
