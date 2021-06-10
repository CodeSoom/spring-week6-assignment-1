package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    public String login() {
        return jwtUtil.customEncode(1L);
    }
}
