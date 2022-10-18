package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginRequestDTO;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public SessionService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        User findUser = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("잘못된 email 입니다"));

        if (!findUser.getPassword().equals(loginRequestDTO.getPassword())) {
            throw new UserNotFoundException("잘못된 password 입니다");
        }

        return jwtUtil.encode(findUser.getId());
    }
}
