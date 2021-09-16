package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.LoginNotMatchPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String login(UserLoginData userLoginData) {
        User findedUser = userRepository.findByEmail(userLoginData.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if(!findedUser.authenticate(userLoginData.getPassword())) {
            throw new LoginNotMatchPasswordException(findedUser.getEmail());
        }

        return jwtUtil.encode(findedUser.getId());
    }

    public void checkToken(String token) {
        jwtUtil.decode(token);
    }
}
