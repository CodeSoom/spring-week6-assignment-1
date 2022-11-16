package com.codesoom.assignment.session;

import com.codesoom.assignment.common.util.JwtUtil;
import com.codesoom.assignment.session.dto.SessionRequestDto;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(SessionRequestDto sessionRequestDto) {
        User user = userRepository.findByEmail(sessionRequestDto.getEmail()).get();

        return jwtUtil.encode(user.getId());
    }
}
