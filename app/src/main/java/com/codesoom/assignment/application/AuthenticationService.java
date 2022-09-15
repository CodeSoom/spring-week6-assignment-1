package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.errors.WrongPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import com.github.dozermapper.core.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final Mapper mapper;

    public AuthenticationService(JwtUtil jwtUtil, UserService userService , Mapper dozerMapper) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.mapper = dozerMapper;

    }

    /**
     * JWT 토큰을 반환한다.
     *
     * @param loginData 로그인 정보
     * @throws UserNotFoundException 로그인 정보에 해당하는 사용자가 존재하지 않을 경우
     * @throws WrongPasswordException 사용자의 패스워드 정보가 일치하지 않은 경우
     * @return JWT 반환
     */
    public String login(UserLoginData loginData){
        User user = mapper.map(loginData , User.class);
        User findUser = userService.findByEmail(user.getEmail());
        if(!findUser.authenticate(user.getPassword())){
            throw new WrongPasswordException();
        }
        return jwtUtil.encode(findUser.getId());
    }

    /**
     * JWT를 검증한다.
     *
     * @param token JWT
     * @throws InvalidTokenException 토큰 정보가 null 또는 사이즈가 0이거나 첫 글자가 공백 , 유효하지 않은 토큰이라면 예외를 던진다.
     * @throws UserNotFoundException 페이로드에 담긴 식별자에 해당하는 사용자가 없는 경우
     */
    public void tokenValidation(String token){
        Long id = jwtUtil.getUserIdFromToken(token);
        userService.findUser(id);
    }
}
