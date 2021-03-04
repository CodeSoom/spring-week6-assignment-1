package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionCreateData;
import com.codesoom.assignment.dto.SessionResultData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserBadRequestException;
import com.codesoom.assignment.errors.UserEmailNotExistedException;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public SessionResultData login(SessionCreateData sessionCreateData) {
        String sessionEmail = sessionCreateData.getEmail();
        String sessionPassword = sessionCreateData.getPassword();

        User user = userRepository.findByEmail(sessionEmail)
                .orElseThrow(UserEmailNotExistedException::new);

        if(!user.authenticate(sessionPassword)) {
            throw new UserBadRequestException("password");
        }

        String accessToken = jwtUtil.encode(sessionCreateData.getEmail(), sessionCreateData.getPassword());
        return SessionResultData.builder()
                .accessToken(accessToken)
                .build();
    }

    public Long parseToken(String accessToken) {
        if(accessToken == null || accessToken.isBlank()) {
            throw new InvalidTokenException(accessToken);
        }
        try {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidTokenException(accessToken);
        }
    }
}
