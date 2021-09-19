package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.PasswordInvalidException;
import com.codesoom.assignment.errors.UserEmailNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * 입력된 user 정보가 일치하면 토큰 반환
     * @param 입력 받은 user정보
     * @return jwt 토큰
     * @Throw 입력된 email의 User정보 존재여부 확인
     * @Throw 입력된 email의 User정보 패스워드 일치확인
     *
     */
    public String login(User inputUser){
        User checkUser = userRepository.findByEmail(inputUser.getEmail())
                .orElseThrow(() -> new UserEmailNotFoundException(inputUser.getEmail()));
        if(!inputUser.getPassword().equals(checkUser.getPassword())){
            throw new PasswordInvalidException();
        }
        return jwtUtil.encode(inputUser.getId());
    }
}
