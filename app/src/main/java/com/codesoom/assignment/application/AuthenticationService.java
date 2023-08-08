package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil,UserRepository userRepository){
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String login(UserLoginData userLoginData){
        User user = userRepository.findByEmail(userLoginData.getEmail()).orElseThrow(() -> new UserNotFoundException());

        if(!user.getPassword().equals(userLoginData.getPassword())){
            throw new LoginFailException();
        }

        return jwtUtil.encode(user.getId());
    }

    public Long parseToken(String accessToken) {
        if(accessToken == null || accessToken.isBlank()){
            throw new InvalidAccessTokenException();
        }

        try{
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        }catch (SignatureException e){
            throw new InvalidAccessTokenException();
        }
    }

}
