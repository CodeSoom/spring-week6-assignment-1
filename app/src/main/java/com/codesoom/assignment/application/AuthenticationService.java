package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginRequest;
import com.codesoom.assignment.errors.LoginNotMatchException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String login(UserLoginRequest userLoginRequest) {

        User user = userRepository.findByEmailAndPassword(
                        userLoginRequest.getEmail(),
                        userLoginRequest.getPassword())
                .orElseThrow(LoginNotMatchException::new);
        return jwtUtil.enCode(user.getId());
    }

    public Long parseToken(String accessToken) {
        return jwtUtil.decode(accessToken).get("userId", Long.class);
    }


}
